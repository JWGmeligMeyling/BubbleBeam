package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
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
	
	public GameController(final GameModel model,
			final CannonController cannonController, final GameTick gameTick) {

		this.model = model;
		this.cannonController = cannonController;
		
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
		model.setLoadedBubble(new ColouredBubble(this.getRandomRemainingColor()));
		model.setNextBubble(new ColouredBubble(this.getRandomRemainingColor()));
		
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
		
		ColouredBubble loadedBubble = model.getLoadedBubble();
		Point loadedBubblePosition = loadedBubble.getPosition();

		MovingBubble shotBubble = new MovingBubble(new Point(loadedBubblePosition.x, loadedBubblePosition.y),
				new Vector2f(direction.multiply(MOVING_BUBBLE_SPEED)),
				model.getScreenSize(), new ColouredBubble(loadedBubble.getColor()));

		updateBubbles();
		model.setShotBubble(shotBubble);
		model.notifyObservers();
	}
	
	protected void updateBubbles() {
		ColouredBubble nextBubble = model.getNextBubble();
		Color nextColor = getRandomRemainingColor();
		log.info("New bubble created with color {}", nextColor);
		model.setLoadedBubbleColor(nextBubble.getColor());
		model.setNextBubbleColor(nextColor);
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
			bubbleMesh.replaceBubble(snapPosition, shotBubble);
			log.info("Bullet collided with {}", snapPosition);

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

}
