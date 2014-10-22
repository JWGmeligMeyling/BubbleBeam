package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.cannon.CannonShootState;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.game.backend.mode.GameMode;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.util.mvc.Controller;
import nl.tudelft.util.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.game.event.GameListener.AmmoLoadEvent;
import nl.tudelft.ti2206.game.event.GameListener.GameOverEvent;
import nl.tudelft.ti2206.game.event.GameListener.ShotMissedEvent;

public class GameController implements Controller<GameModel>, Tickable {
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	
	private static final float MOVING_BUBBLE_SPEED = 15f;
	
	protected final GameModel model;
	protected final GameMode gameMode;
	protected final CannonController cannonController;
	protected final boolean intelligent;
	
	protected GameTick gameTick;

	public GameController(final GameModel model, final CannonController cannonController) {
		this(model, cannonController, false);
	}
	
	public GameController(final GameModel model, final CannonController cannonController,
			boolean intelligent) {

		log.info("Contructed {} with {} and {}", this, model, cannonController);
		
		this.intelligent = intelligent;
		this.model = model;
		this.cannonController = cannonController;
		this.gameMode = constructGameMode();
		this.model.addEventListener(gameMode);

		model.getBubbleMesh().calculatePositions();
		prepareAmmo();
		
		cannonController
			.getModel()
			.addEventListener(new GameControllerCannonListener(this));
		
		model.getBubbleMesh()
			.getEventTarget()
			.addEventListener(new GameControllerBubbleMeshListener(model));
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
		if(model.getLoadedBubble() == null ||
			model.getNextBubble() == null ) {
			log.info("Prepare initial ammo for {}", this);
			model.setLoadedBubble(createAmmoBubble());
			model.setNextBubble(createAmmoBubble());
		}
	}
	
	/**
	 * Bind a {@link GameTick} to this {@code GameController}
	 * @param gameTick
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

		if (model.isShooting()) {
			MovingBubble shotBubble = model.getShotBubble();
			
			shotBubble.addVelocity();
			shotBubble.gameTick();
			
			
			bubbleMesh
					.stream()
					.filter(bubble -> bubble.intersect(shotBubble)
							&& (bubble.isHittable() || bubbleMesh.bubbleIsTop(bubble))).findAny()
					.ifPresent(bubble -> this.collide(bubbleMesh, shotBubble, bubble));
		}
		
		if(model.isGameOver()) {
			gameOver(false);
		}
		else {
			try {
				gameMode.gameTick();
			}
			catch (GameOver e) {
				gameOver(false);
			}
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
		
		if (model.isShooting()) {
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
		if(!intelligent) {
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
				throw new GameOver();
			}

			cannonController.load();
		} catch (GameOver e) {
			gameOver(false);
		} finally {
			model.setShotBubble(null);
		}
		
		
	}
	
	/**
	 * Trigger {@code GameOver} on this {@code GameController}
	 * @param win
	 */
	public void gameOver(boolean win) {
		if(model.isGameOver()) return;
		
		if(win)
			log.info("Hurray, you won the game");
		else
			log.info("Sorry dawg, the game is over");
		
		cannonController.setState(new CannonShootState());
		if(gameTick != null) gameTick.removeObserver(this);
		
		model.setGameOver(true);
		model.setWon(win);
		model.notifyObservers();
		model.trigger(listener -> listener.gameOver(new GameOverEvent(this)));
	}
	
	/**
	 * Insert a new row after several misses
	 */

	public void insertRow() {
		if(!intelligent) {
			try {
				model.getBubbleMesh().insertRow(this);
			} catch (GameOver e) {
				gameOver(false);
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
	 * Bind a connector for multiplayer
	 * @param connector
	 */
	public void bindConnectorAsMaster(final Connector connector) {
		log.info("Binding {} to connector {}", this, connector);
		model.addEventListener(new ConnectorGameListener(connector));
	}
	
	public void bindConnectorAsSlave(final Connector connector) {
		connector.addEventListener(new SlaveGamePacketListener(this, cannonController));
	}
	
}
