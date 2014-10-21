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
import nl.tudelft.ti2206.network.packets.AmmoPacket;
import nl.tudelft.ti2206.network.packets.BubbleMeshSync;
import nl.tudelft.ti2206.network.packets.CannonShoot;
import nl.tudelft.ti2206.network.packets.PacketHandler;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.ti2206.util.mvc.Controller;
import nl.tudelft.util.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameController implements Controller<GameModel>, Tickable {
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	
	private static final float MOVING_BUBBLE_SPEED = 15f;
	
	protected final GameModel model;
	protected final GameMode gameMode;
	protected final CannonController cannonController;
	protected final boolean kill;
	protected final GameTick tick;
	
	public GameController(final GameModel model, final CannonController cannonController,
			final GameTick gameTick) {
		this(model, cannonController, gameTick, false);
	}
	
	public GameController(final GameModel model, final CannonController cannonController,
			final GameTick gameTick, boolean kill) {

		log.info("Contructed {} with {} and {}", this, model, cannonController);
		
		this.kill = kill;
		this.model = model;
		this.tick = gameTick;
		this.cannonController = cannonController;
		
		try {
			this.gameMode = model.getGameMode().getConstructor(GameController.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Failed to instantiate GameController", e);
		}
		
		model.getBubbleMesh().calculatePositions();
		prepareAmmo();
		
		cannonController.getModel().addEventListener((direction) -> {
			GameController.this.shoot(direction);
		});
		
		model.getBubbleMesh().getEventTarget().addScoreListener((bubbleMesh, amount) -> {
			model.incrementScore(amount);
			model.retainRemainingColors(bubbleMesh.getRemainingColours());
			model.notifyObservers();
		});
		
		gameTick.registerObserver(this);
	}
	
	protected void prepareAmmo() {
		log.info("Prepare initial ammo for {}", this);
		model.setLoadedBubble(createAmmoBubble());
		model.setNextBubble(createAmmoBubble());
	}
	
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
			gameOver();
		}
		else {
			try {
				gameMode.gameTick();
			}
			catch (GameOver e) {
				gameOver();
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
		model.triggerShootEvent(direction);
	}
	
	protected void updateBubbles() {
		if(!kill) {
			Bubble nextBubble = createAmmoBubble();
			Bubble previousNextBubble = model.getNextBubble();
			model.setNextBubble(nextBubble);
			model.setLoadedBubble(previousNextBubble);
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
				gameMode.missed();
			}
			else if(bubbleMesh.isEmpty()){
				throw new GameOver();
			}

			cannonController.load();
		} catch (GameOver e) {
			gameOver();
		} finally {
			model.setShotBubble(null);
		}
		
		
	}
	
	protected void gameOver() {
		log.info("Sorry dawg, the game is over");
		cannonController.setState(new CannonShootState());
		model.setGameOver(true);
		model.notifyObservers();
		model.trigger(GameOverEventListener.class, GameOverEventListener::gameOver);
		tick.removeObserver(this);
	}
	
	/**
	 * Insert a new row after several misses
	 */

	public void insertRow() {
		if(!kill) {
			try {
				model.getBubbleMesh().insertRow(this);
			} catch (GameOver e) {
				gameOver();
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
		sendInitialData(connector);
		
		model.getBubbleMesh().getEventTarget().addRowInsertedListener((bubbleMesh) -> {
			log.info("Sending insert row");
			connector.sendPacket(new BubbleMeshSync(bubbleMesh));
		});
		
		model.addShootEventListener((direction) -> {
			log.info("Sending shoot packet");
			connector.sendPacket(new CannonShoot(direction));
			
			Bubble loadedBubble = model.getLoadedBubble();
			Bubble nextBubble = model.getNextBubble();
			log.info("Sending ammo packet with [{}, {}]", loadedBubble, nextBubble);
			connector.sendPacket(new AmmoPacket(loadedBubble, nextBubble));
		});
		
	}
	
	public void bindConnectorAsSlave(final Connector connector) {
		PacketHandler packetHandlerCollection = connector.getPacketHandlerCollection();
		
		packetHandlerCollection.addEventListener(
				PacketListener.BubbleMeshSyncListener.class, (packet) -> {
					log.info("Processing packet {}", packet);
					model.setBubbleMesh(packet.bubbleMesh);
					model.notifyObservers();
				});

		packetHandlerCollection.addEventListener(
				PacketListener.AmmoPacketListener.class, (packet) -> {
					log.info("Processing packet {}", packet);
					model.setLoadedBubble(packet.loadedBubble);
					model.setNextBubble(packet.nextBubble);
					model.notifyObservers();
				});
	}
	
	protected void sendInitialData(final Connector connector) {
		log.info("Sending initial data to {}", connector);
		connector.sendPacket(new BubbleMeshSync(model.getBubbleMesh()));
		connector.sendPacket(new AmmoPacket(
				model.getLoadedBubble(),
				model.getNextBubble()));
	}
	
	public GameMode getGameMode() {
		return gameMode;
	}
	
}
