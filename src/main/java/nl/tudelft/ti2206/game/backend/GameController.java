package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.cannon.CannonShootState;
import nl.tudelft.ti2206.game.backend.mode.GameMode;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.util.mvc.Controller;
import nl.tudelft.util.Vector2f;
import nl.tudelft.ti2206.game.event.GameListener;
import nl.tudelft.ti2206.game.event.GameListener.AmmoLoadEvent;
import nl.tudelft.ti2206.game.event.GameListener.GameOverEvent;
import nl.tudelft.ti2206.game.event.GameListener.ShotMissedEvent;

/**
 * The {@link GameController} is responsible for generic game logic
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class GameController implements Controller<GameModel>, Tickable {
	
	public enum GameControllerType {
		SINGLEPLAYER, MULTIPLAYER_MASTER, MULTIPLAYER_SLAVE;
	}
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	
	private static final float MOVING_BUBBLE_SPEED = 15f;
	
	protected final GameModel model;
	protected final GameMode gameMode;
	protected final CannonController cannonController;
	protected final GameControllerType type;
	
	protected GameTick gameTick;
	
	/**
	 * Construct a new {@link GameController}
	 * 
	 * @param model
	 *            {@link GameModel} to use with the controller
	 * @param cannonController
	 *            {@link CannonController} to use with the controller
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public GameController(final GameModel model, final CannonController cannonController) throws IOException {
		this(model, cannonController, GameControllerType.SINGLEPLAYER);
	}
	
	/**
	 * Construct a new {@link GameController}
	 * 
	 * @param model
	 *            {@link GameModel} to use with the controller
	 * @param cannonController
	 *            {@link CannonController} to use with the controller
	 * @param type
	 *            the {@link GameControllerType} for this {@link GameController}
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public GameController(final GameModel model, final CannonController cannonController,
			GameControllerType type) throws IOException {

		log.info("Contructed {} with {} and {}", this, model, cannonController);
		
		this.type = type;
		this.model = model;
		this.cannonController = cannonController;
		
		this.gameMode = constructGameMode();
		
		this.model
			.addEventListener(gameMode);
		
		cannonController
			.getModel()
			.addEventListener(new GameControllerCannonListener(this));
		
		if(model.getBubbleMesh() != null) {
			setBubbleMesh(model.getBubbleMesh());
		}
		else {
			loadNextBubbleMesh();
		}
		
		prepareAmmo();
	}
	
	/**
	 * Load the next {@link BubbleMesh} from the {@link GameMode}
	 * @throws IOException If an I/O error occurs
	 */
	public void loadNextBubbleMesh() throws IOException {
		BubbleMesh bubbleMesh = gameMode.nextMap();
		setBubbleMesh(bubbleMesh);
	}
	
	protected void setBubbleMesh(BubbleMesh bubbleMesh) {
		bubbleMesh.calculatePositions();
		bubbleMesh.addEventListener(new GameControllerBubbleMeshListener(model));
		model.setGameState(GameState.PLAYING);
		model.setBubbleMesh(bubbleMesh);
		cannonController.load();
	}
	
	protected GameMode constructGameMode() {
		try {
			return model.getGameMode().getConstructor(GameController.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Failed to instantiate GameController", e);
		}
	}
	
	/**
	 * Prepare initial ammo
	 */
	protected void prepareAmmo() {
		if(model.getLoadedBubble() == null) {
			log.info("Prepare initial ammo for {}", this);
			model.setLoadedBubble(createAmmoBubble());
			model.setNextBubble(createAmmoBubble());
		}
		assert model.getNextBubble() != null &&
				model.getLoadedBubble() != null : "Ammo should not be empty!";
	}
	
	/**
	 * Bind a {@link GameTick} to this {@code GameController}
	 * @param gameTick The {@link GameTick} for this {@code GameController}
	 */
	public void bindGameTick(GameTick gameTick) {
		// Store the gameTick so we can unbind after game over
		this.gameTick = gameTick;
		gameTick.registerObserver(this);
	}

	/**
	 * Get the {@link GameMode} for this {@code GameController}
	 * @return the {@code GameMode}
	 */
	public GameMode getGameMode() {
		return gameMode;
	}
	
	/**
	 * Get the {@link CannonController} for this {@code GameController}
	 * @return the {@code CannonController}
	 */
	public CannonController getCannonController() {
		return cannonController;
	}

	@Override
	public void gameTick() {
		BubbleMesh bubbleMesh = model.getBubbleMesh();
		final MovingBubble shotBubble = model.getShotBubble();
		
		if (shotBubble != null) {
			
			shotBubble.addVelocity();
			shotBubble.gameTick();
			
			
			bubbleMesh
					.stream()
					.filter(bubble -> bubble.intersect(shotBubble)
							&& (bubble.isHittable() || bubbleMesh.bubbleIsTop(bubble))).findAny()
					.ifPresent(bubble -> this.collide(bubbleMesh, shotBubble, bubble));
		}
		
		try {
			if(model.getGameState().equals(GameState.PLAYING)) {
				gameMode.gameTick();
			}
		}
		catch (GameOver e) {
			gameOver(e);
		}
		
	}
	
	/**
	 * Shoot a bubble
	 * 
	 * @param direction
	 *            direction in which to fire the bubble
	 * @throws IllegalStateException
	 *             if there already is a bubble being shot
	 */
	protected void shoot(final Vector2f direction) {
		
		direction.normalise();
		
		if (model.getShotBubble() != null) {
			throw new IllegalStateException("Wait for another shoot");
		}
		
		Bubble loadedBubble = model.getLoadedBubble();
		
		MovingBubble shotBubble = new MovingBubble(new Vector2f(
				direction.multiply(MOVING_BUBBLE_SPEED)), model.getScreenSize(), loadedBubble);
		
		updateBubbles();
		model.setShotBubble(shotBubble);
		model.notifyObservers();
	}
	
	protected void updateBubbles() {
		if(!type.equals(GameControllerType.MULTIPLAYER_SLAVE)) {
			Bubble nextBubble = createAmmoBubble();
			Bubble previousNextBubble = model.getNextBubble();
			model.setNextBubble(nextBubble);
			model.setLoadedBubble(previousNextBubble);
			
			AmmoLoadEvent event = new AmmoLoadEvent(this, previousNextBubble, nextBubble);
			model.trigger(listener -> listener.ammo(event));
		}
	}
	
	/**
	 * Hit a certain {@link Bubble} and snap to the closest
	 * {@link BubblePlaceholder}
	 * 
	 * @param hitTarget
	 * @throws GameOver
	 */
	protected void collide(final BubbleMesh bubbleMesh, final MovingBubble movingBubble,
			final Bubble hitTarget) {
		
		Bubble shotBubble = movingBubble;
		Bubble snapPosition = hitTarget.getSnapPosition(shotBubble);
		
		try {
			log.info("Bullet collided with {}", hitTarget);
			shotBubble.collideHook(hitTarget);
			
			log.info("Bullet snapped to {}", snapPosition);
			bubbleMesh.replaceBubble(snapPosition, shotBubble);
		
			
			if (!bubbleMesh.pop(shotBubble)) {
				ShotMissedEvent event = new ShotMissedEvent(this);
				model.trigger(listener -> listener.shotMissed(event));
			}
			else if(bubbleMesh.isEmpty()){
				throw new GameOver(true);
			}
			
			if(model.getGameState().equals(GameState.PLAYING))
				cannonController.load();
			
		} catch (GameOver e) {
			gameOver(e);
		} finally {
			model.setShotBubble(null);
		}
		
		
	}
	
	/**
	 * Trigger {@code GameOver} on this {@code GameController}
	 * @param gameOver {@link GameOver} exception that was thrown
	 */
	public void gameOver(GameOver gameOver) {
		if(!model.getGameState().equals(GameState.PLAYING)) return;
		
		if(gameOver.isWin()) {
			log.info("Hurray, you won the game");
			model.setGameState(GameState.WIN);
		}
		else {
			log.info("Sorry dawg, the game is over");
			model.setGameState(GameState.LOSE);
		}
		
		cannonController.setState(new CannonShootState());
		model.notifyObservers();
		
		if(type.equals(GameControllerType.SINGLEPLAYER) && gameOver.isWin() && gameMode.hasNextMap()) {
			try {
				loadNextBubbleMesh();
				return;
			}
			catch (IOException e) {
				log.warn("Failed to instantiate new level", e);
			}
		}
		
		model.trigger(listener -> listener.gameOver(new GameOverEvent(this, gameOver)));
	}
	
	/**
	 * Insert a new row after several misses
	 */

	public void insertRow() {
		if(!type.equals(GameControllerType.MULTIPLAYER_SLAVE)) {
			try {
				model.getBubbleMesh().insertRow(this);
			} catch (GameOver e) {
				gameOver(e);
			}
		}
	}
	
	@Override
	public GameModel getModel() {
		return model;
	}
	
	protected final Random RANDOM_GENERATOR = new Random(); 
	
	/**
	 * @return a random remaining Color
	 */
	public Color getRandomRemainingColor() {
		Set<Color> remainingColors = model.getRemainingColors();
		final int index = RANDOM_GENERATOR.nextInt(remainingColors.size());
		final Iterator<Color> iterator = remainingColors.iterator();
		for (int i = 0; i <= index; i++)
			if (i == index) {
				return iterator.next();
			} else {
				iterator.next();
			}
		throw new IndexOutOfBoundsException();
	}
	
	protected Bubble createAmmoBubble() {
		Bubble bubble = gameMode.getBubbleFactory()
				.createBubble(model.getRemainingColors());
		log.info("Created new ammo: " + bubble.toString());
		return bubble;
	}
	
	/**
	 * Increment the score for the {@code GameController}. This first updates
	 * the {@link GameModel} and then triggers a {@code ScoreEvent} on the
	 * {@link GameListener GameListeners}.
	 * 
	 * @param amount
	 *            Amount of points awarded to the player
	 */
	public void incrementScore(int amount) {
		log.info("Awarded {} points", amount);
		model.incrementScore(amount);
		model.notifyObservers();
		GameListener.ScoreEvent event = new GameListener.ScoreEvent(this, amount);
		model.trigger(listener -> listener.score(event));
	}
	
	/**
	 * Bind a connector for multiplayer
	 * 
	 * @param connector
	 *            {@link Connector} to bind to this {@code GameController}
	 */
	public void bindConnectorAsMaster(final Connector connector) {
		log.info("Binding {} to connector {}", this, connector);
		model.addEventListener(new ConnectorGameListener(connector));
	}
	
	/**
	 * Bind a connector for multiplayer
	 * 
	 * @param connector
	 *            {@link Connector} to bind to this {@code GameController}
	 */
	public void bindConnectorAsSlave(final Connector connector) {
		connector.addEventListener(new SlaveGamePacketListener(this, cannonController));
	}
	
}
