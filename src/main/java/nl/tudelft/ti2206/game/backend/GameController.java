package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;
import nl.tudelft.ti2206.util.mvc.Controller;
import nl.tudelft.util.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameController implements Controller<GameModel>, Tickable {
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	
	private static final float MOVING_BUBBLE_SPEED = 15f;
	private final static int MAX_MISSES = 5;
	
	protected final GameModel model;
	protected final CannonController cannonController;
	protected final boolean kill;
	
	public GameController(final GameModel model, final CannonController cannonController,
			final GameTick gameTick) {
		this(model, cannonController, gameTick, false);
	}
	
	public GameController(final GameModel model, final CannonController cannonController,
			final GameTick gameTick, boolean kill) {

		this.kill = kill;
		this.model = model;
		this.cannonController = cannonController;
		
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
		if (model.isShooting()) {
			MovingBubble shotBubble = model.getShotBubble();
			BubbleMesh bubbleMesh = model.getBubbleMesh();
			
			shotBubble.addVelocity();
			shotBubble.gameTick();
			
			
			bubbleMesh
					.stream()
					.filter(bubble -> bubble.intersect(shotBubble)
							&& (bubble.isHittable() || bubbleMesh.bubbleIsTop(bubble))).findAny()
					.ifPresent(bubble -> this.collide(bubbleMesh, shotBubble, bubble));
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
				incrementMisses();
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
		model.setGameOver(true);
		model.notifyObservers();
		model.trigger(GameOverEventListener.class, GameOverEventListener::gameOver);
	}
	
	/**
	 * No bubbles popped, increment misses
	 * 
	 * @throws GameOver
	 */
	protected void incrementMisses() throws GameOver {
		int misses = model.getMisses();
		if (++misses == MAX_MISSES) {
			misses = 0;
			insertRow();
		}
		model.setMisses(misses);
	}
	
	/**
	 * Insert a new row after several misses
	 */

	public void insertRow() {
		if(!kill) {
			model.getBubbleMesh().insertRow(this);
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
		Bubble bubble = model.getGameMode().getBubbleFactory()
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
			connector.sendPacket(new Packet.BubbleMeshSync(bubbleMesh));
		});
		
		model.addShootEventListener((direction) -> {
			log.info("Sending shoot packet");
			connector.sendPacket(new Packet.CannonShoot(direction));
			
			Bubble loadedBubble = model.getLoadedBubble();
			Bubble nextBubble = model.getNextBubble();
			log.info("Sending ammo packet with [{}, {}]", loadedBubble, nextBubble);
			connector.sendPacket(new Packet.AmmoPacket(loadedBubble, nextBubble));
		});
		
	}
	
	public void bindConnectorAsSlave(final Connector connector) {
		PacketHandlerCollection packetHandlerCollection = connector.getPacketHandlerCollection();
		
		packetHandlerCollection.registerBubbleMeshSyncListener((packet) -> {
			log.info("Processing packet {}", packet);
			model.setBubbleMesh(packet.bubbleMesh);
			model.notifyObservers();
		});

		packetHandlerCollection.registerLoadNewBubbleListener((packet) -> {
			log.info("Processing packet {}", packet);
			model.setLoadedBubble(packet.loadedBubble);
			model.setNextBubble(packet.nextBubble);
			model.notifyObservers();
		});
	}
	
	protected void sendInitialData(final Connector connector) {
		log.info("Sending initial data to {}", connector);
		connector.sendPacket(new Packet.BubbleMeshSync(model.getBubbleMesh()));
		connector.sendPacket(new Packet.AmmoPacket(
				model.getLoadedBubble(),
				model.getNextBubble()));
	}
	
}
