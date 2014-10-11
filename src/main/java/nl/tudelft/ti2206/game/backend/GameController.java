package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.BombBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.JokerBubble;
import nl.tudelft.ti2206.bubbles.MovingBubble;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.util.mvc.Controller;
import nl.tudelft.util.Vector2f;

public class GameController implements Controller<GameModel>, Tickable {
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);

	private static final float MOVING_BUBBLE_SPEED = 5f;
	private final static int MAX_MISSES = 5;
	
	protected final GameModel model;
	protected final CannonController cannonController;
	
	protected final BubbleFactory factory;
	
	public GameController(final GameModel model,
			final CannonController cannonController, final GameTick gameTick, final BubbleFactory factory) {

		this.model = model;
		this.cannonController = cannonController;
		this.factory = factory;
		
		model.getBubbleMesh().calculatePositions();
		prepareAmmo();
		
		cannonController.getModel().addEventListener((direction) -> {
			GameController.this.shoot(direction);	
		});
		
		model.getBubbleMesh().addScoreListener((amount) -> {
			model.incrementScore(amount);
			model.retainRemainingColors(model.getBubbleMesh().getRemainingColours());
			model.notifyObservers();
		});
		
		gameTick.registerObserver(this);
	}
	
	protected void prepareAmmo() {
		model.setLoadedBubble(createAmmoBubble());
		model.setNextBubble(createAmmoBubble());
	}

	public CannonController getCannonController() {
		return cannonController;
	}

	@Override
	public void gameTick() {
		if(model.isShooting()) {
			MovingBubble shotBubble = model.getShotBubble();
			BubbleMesh bubbleMesh = model.getBubbleMesh();
			
			shotBubble.gameTick();
			
			bubbleMesh
				.stream()
				.filter(bubble -> bubble.intersect(shotBubble)
						&& (bubble.isHittable() || bubbleMesh.bubbleIsTop(bubble)))
				.findAny().ifPresent(bubble -> this.collide(bubbleMesh, shotBubble, bubble));
			
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

		if(model.isShooting()) {
			throw new IllegalStateException("Wait for another shoot");
		}
		
		Bubble loadedBubble = model.getLoadedBubble();

		MovingBubble shotBubble = new MovingBubble(new Vector2f(direction.multiply(MOVING_BUBBLE_SPEED)),
				model.getScreenSize(), loadedBubble);

		updateBubbles();
		model.setShotBubble(shotBubble);
		model.notifyObservers();
	}
	
	protected void updateBubbles() {
		Bubble nextBubble = createAmmoBubble();
		Bubble previousNextBubble = model.getNextBubble();
		nextBubble.setPosition(previousNextBubble.getPosition());
		model.setNextBubble(nextBubble);
		previousNextBubble.setPosition(model.getLoadedBubble().getPosition());
		model.setLoadedBubble(previousNextBubble);
	}
	
	/**
	 * Hit a certain {@link Bubble} and snap to the closest
	 * {@link BubblePlaceholder}
	 * 
	 * @param hitTarget
	 * @throws GameOver
	 */
	protected void collide(final BubbleMesh bubbleMesh,
			final MovingBubble movingBubble, final Bubble hitTarget) {
		
		Bubble shotBubble = movingBubble.getBubble();
		BubblePlaceholder snapPosition = hitTarget.getSnapPosition(shotBubble);
		
		try {
			log.info("Bullet collided with {}", hitTarget);
			shotBubble.collideHook(hitTarget);
			
			log.info("Bullet snapped to {}", snapPosition);
			bubbleMesh.replaceBubble(snapPosition, shotBubble);

			if (!bubbleMesh.pop(shotBubble)) {
				incrementMisses();
			}

			cannonController.load();
		}
		catch (GameOver e) {
			gameOver();
		}
		finally {
			model.setShotBubble(null);
		}
		
	}
	
	protected void gameOver() {
		log.info("Sorry dawg, the game is over");
		model.setGameOver(true);
		model.notifyObservers();
	}

	/**
	 * No bubbles popped, increment misses
	 * 
	 * @throws GameOver
	 */
	protected void incrementMisses() throws GameOver {
		int misses = model.getMisses();
		if(++misses == MAX_MISSES) {
			misses = 0;
			insertRow();
		}
		model.setMisses(misses);
	}

	/**
	 * Insert a new row after several misses
	 */
	protected void insertRow() {
		model.getBubbleMesh().insertRow(this);
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
		for(int i = 0; i <= index; i++)
			if(i == index) {
				return iterator.next();
			} else {
				iterator.next();
			}
		throw new IndexOutOfBoundsException();
	}

	protected Bubble createAmmoBubble() {
		Bubble bubble;
		if(RANDOM_GENERATOR.nextInt(10) == 1) {			//1/10 chance for a special bubble
			bubble = factory.createSpecialBubble();
		} else {
			bubble = new ColouredBubble(getRandomRemainingColor());
		}
		log.info("Created new ammo: " + bubble.toString());
		return bubble;
	}

}
