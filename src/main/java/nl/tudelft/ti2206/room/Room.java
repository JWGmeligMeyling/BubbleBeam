package nl.tudelft.ti2206.room;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.MovingBubble;
import nl.tudelft.ti2206.cannon.Cannon;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.cannon.CannonControllerObserver;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.game.tick.Tickable;
import nl.tudelft.util.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Room is responsible for keeping track of all objects within the room.
 * 
 * @author Sam Smulders
 * @author Luka Bavdaz
 */

// TODO: Room shouldn't handle game over directly.
// TODO: Move the creation of the bubble mesh into the Room

public abstract class Room implements Tickable, CannonControllerObserver {
	
	private static final Logger log = LoggerFactory.getLogger(Room.class);
	
	// Cannon
	protected CannonController cannonController;
	protected final Cannon cannon;
	public final Point cannonPosition;
	
	// Bubble shooting
	protected static final int MAX_MISSES = 5;
	private static final float MOVING_BUBBLE_SPEED = 4f;
	protected int misses = 0;
	protected MovingBubble shotBubble;
	protected BubbleMesh bubbleMesh;
	
	// Bubble queue
	protected ArrayList<ColouredBubble> bubbleQueue = new ArrayList<ColouredBubble>();
	protected final Point LOADED_BUBBLE_POSITION;
	protected final Point NEXT_BUBBLE_POSITION;
	protected final int BUBBLE_QUEUE_SPACING = 60;
	
	// Room
	protected final Dimension screenSize;
	
	public Room(final Point cannonPosition, final Dimension dimension) {
		this.cannonPosition = cannonPosition;
		this.screenSize = dimension;
		
		this.cannon = new Cannon(cannonPosition, dimension);
		
		LOADED_BUBBLE_POSITION = new Point(cannonPosition.x
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING), cannonPosition.y
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING));
		NEXT_BUBBLE_POSITION = new Point(cannonPosition.x + BUBBLE_QUEUE_SPACING
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING), cannonPosition.y
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING));
	}
	
	public void setup() {
		cannonController.registerObserver(this);
	}
	
	public abstract void deconstruct();
	
	public boolean canShoot() {
		return shotBubble == null;
	}
	
	protected void drawQueue(final Graphics graphics) {
		bubbleQueue.forEach(bubble -> {
			bubble.render(graphics);
		});
	}
	
	// TODO: gameTick shouldn't throw GameOver.
	@Override
	public void gameTick() throws GameOver {
		if (shotBubble != null) {
			shotBubble.gameStep();
			
			bubbleMesh
				.stream()
				.filter(bubble -> bubble.intersect(shotBubble)
						&& (bubble.isHittable() || bubbleMesh.bubbleIsTop(bubble)))
				.findAny().ifPresent(bubble -> this.collide(bubble));
		}
	}
	
	/**
	 * Hit a certain {@link Bubble} and snap to the closest
	 * {@link BubblePlaceholder}
	 * 
	 * @param hitTarget
	 * @throws GameOver
	 */
	public void collide(final Bubble hitTarget) throws GameOver {
		BubblePlaceholder snapPosition = hitTarget.getSnapPosition(shotBubble);
		bubbleMesh.replaceBubble(snapPosition, shotBubble);
		log.debug("Bullet collided with {}", snapPosition);
		
		if (bubbleMesh.pop(shotBubble)) {
			// good shot
		} else {
			incrementMisses();
		}
		
		shotBubble = null;
	}
	
	protected void incrementMisses() throws GameOver {
		if (++misses == MAX_MISSES) {
			misses = 0;
			bubbleMesh.insertRow();
		}
	}
	
	// TODO: Handling bubbleQueue is empty
	public void shootBubble(final Vector2f direction) {
		direction.normalise();
		Point bubbleStartPosition = new Point((cannonPosition.x - AbstractBubble.WIDTH / 2)
				+ (int) (Cannon.CANNON_OUTPUT * direction.x), cannonPosition.y
				- AbstractBubble.HEIGHT / 2 + (int) (Cannon.CANNON_OUTPUT * direction.y));
		shotBubble = new MovingBubble(bubbleStartPosition, new Vector2f(
				direction.multiply(MOVING_BUBBLE_SPEED)), screenSize, bubbleQueue.remove(0)
				.getColor());
	}
	
	public void correctBubblePositions() {
		if (bubbleQueue.size() > 0) {
			bubbleQueue.get(0).setPosition(LOADED_BUBBLE_POSITION);
			if (bubbleQueue.size() > 1) {
				bubbleQueue.get(1).setPosition(NEXT_BUBBLE_POSITION);
			}
		}
	}
	
	public void render(Graphics g) {
		if (bubbleMesh != null) {
			bubbleMesh.forEach(bubble -> bubble.render(g));
		}
		cannon.render(g);
		if (shotBubble != null) {
			shotBubble.render(g);
		}
		if (bubbleQueue.size() > 0) {
			bubbleQueue.get(0).render(g);
			if (bubbleQueue.size() > 1) {
				bubbleQueue.get(1).render(g);
			}
		}
	}
	
	public void cannonRotate(double direction) {
	}
	
	public void cannonShoot(Vector2f direction) {
		shootBubble(direction);
	}
}
