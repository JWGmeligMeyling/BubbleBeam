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
import nl.tudelft.ti2206.network.packets.EventPacket;
import nl.tudelft.ti2206.network.packets.PacketListenerImpl;
import nl.tudelft.ti2206.util.mvc.Controller;
import nl.tudelft.util.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.game.event.BubbleMeshListener;
import nl.tudelft.ti2206.game.event.CannonListener;
import nl.tudelft.ti2206.game.event.GameListener;
import nl.tudelft.ti2206.game.event.GameListener.*;

public class GameController implements Controller<GameModel>, Tickable {
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	
	private static final float MOVING_BUBBLE_SPEED = 15f;
	
	protected final GameModel model;
	protected final GameMode gameMode;
	protected final CannonController cannonController;
	protected final boolean intelligent;
	
	public GameController(final GameModel model, final CannonController cannonController) {
		this(model, cannonController, false);
	}
	
	public GameController(final GameModel model, final CannonController cannonController,
			boolean intelligent) {

		log.info("Contructed {} with {} and {}", this, model, cannonController);
		
		this.intelligent = intelligent;
		this.model = model;
		this.cannonController = cannonController;
		
		try {
			this.gameMode = model.getGameMode().getConstructor(GameController.class).newInstance(this);
			this.model.addEventListener(gameMode);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Failed to instantiate GameController", e);
		}
		
		model.getBubbleMesh().calculatePositions();
		prepareAmmo();
		
		cannonController.getModel().addEventListener(new CannonListener() {

			@Override
			public void shoot(final CannonShootEvent event) {
				model.trigger(listener -> listener.shoot(event));
				GameController.this.shoot(event.getDirection());
			}

			@Override
			public void rotate(final CannonRotateEvent event) {
				model.trigger(listener -> listener.rotate(event));
			}
			
		});
		
		model.getBubbleMesh().getEventTarget().addEventListener(new BubbleMeshListener() {

			@Override
			public void rowInsert(RowInsertEvent event) {
				model.trigger(listener -> listener.rowInsert(event));
			}

			@Override
			public void pop(BubblePopEvent event) {
				model.trigger(listener -> listener.pop(event));
			}

			@Override
			public void score(ScoreEvent event) {
				int amount = event.getAmountOfPoints();
				model.incrementScore(amount);
				model.retainRemainingColors(event.getSource().getRemainingColours());
				model.notifyObservers();
				model.trigger(listener -> listener.score(event));
			}
			
		});
	}
	
	protected void prepareAmmo() {
		if(model.getLoadedBubble() == null ||
			model.getNextBubble() == null ) {
			log.info("Prepare initial ammo for {}", this);
			model.setLoadedBubble(createAmmoBubble());
			model.setNextBubble(createAmmoBubble());
		}
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
			gameOver();
		} finally {
			model.setShotBubble(null);
		}
		
		
	}
	
	private GameTick gameTick;
	
	public void bindGameTick(GameTick gameTick) {
		gameTick.registerObserver(this);
	}
	
	protected void gameOver() {
		log.info("Sorry dawg, the game is over");
		cannonController.setState(new CannonShootState());
		model.setGameOver(true);
		model.notifyObservers();
		model.trigger(listener -> listener.gameOver(new GameOverEvent(this)));
		if(gameTick != null) gameTick.removeObserver(this);
	}
	
	/**
	 * Insert a new row after several misses
	 */

	public void insertRow() {
		if(!intelligent) {
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

		model.addEventListener(new GameListener() {

			@Override
			public void rowInsert(RowInsertEvent event) {
				log.info("Sending insert row");
				connector.sendPacket(new EventPacket(event));
			}
			
			@Override
			public void shoot(CannonShootEvent event) {
				log.info("Sending shoot packet");
				connector.sendPacket(new EventPacket(event));
			}

			@Override
			public void gameOver(GameOverEvent event) {
			}

			@Override
			public void shotMissed(ShotMissedEvent event) {
			}
			
			@Override
			public void pop(BubblePopEvent event) {
				log.info("{} bubbles popped", event.amountOfPoppedBubbles());
				connector.sendPacket(new EventPacket(event));
			}
			
			@Override
			public void rotate(CannonRotateEvent event) {
				connector.sendPacket(new EventPacket(event));
			}
			
			@Override
			public void ammo(AmmoLoadEvent event) {
				log.info("Sending ammo packet with [{}, {}]", event.getLoadedBubble(), event.getNextBubble());
				connector.sendPacket(new EventPacket(event));
			}
			
		});
		
	}
	
	public void bindConnectorAsSlave(final Connector connector) {
		connector.addEventListener(new PacketListenerImpl(this, cannonController));
	}
	
	public GameMode getGameMode() {
		return gameMode;
	}
	
}
