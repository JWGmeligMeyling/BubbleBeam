package nl.tudelft.ti2206.room;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.MovingBubble;
import nl.tudelft.ti2206.cannon.Cannon2;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.network.Client;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.util.Vector2f;

/**
 * The Room is responsible for keeping track of all objects within the room.
 * 
 * @author Sam_
 * @author Luka
 *
 */
public class Room {
	// protected final int WIDTH, HEIGHT;
	protected final Cannon2 cannon;
	private static final int MAX_MISSES = 5;
	protected final Point LOADED_BUBBLE_POSITION;
	protected final Point NEXT_BUBBLE_POSITION;
	public final Point cannonPosition;
	protected final int BUBBLE_QUEUE_SPACING = 60;
	
	protected ColouredBubble nextBubble, loadedBubble;
	protected MovingBubble shotBubble;
	protected int misses = 0;
	protected BubbleMesh bubbleMesh;
	
	protected final Dimension screenSize;
	
	public Room(final Point cannonPosition, final Dimension dimension, final BubbleMesh bubbleMesh) {
		this.bubbleMesh = bubbleMesh;
		this.cannonPosition = cannonPosition;
		this.screenSize = dimension;
		
		fillBubbleSlots();
		
		this.cannon = new Cannon2(cannonPosition, dimension);
		
		LOADED_BUBBLE_POSITION = new Point(cannonPosition.x
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING), cannonPosition.y
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING));
		NEXT_BUBBLE_POSITION = new Point(cannonPosition.x + BUBBLE_QUEUE_SPACING
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING), cannonPosition.y
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING));
		
		Connector connector = new Client("127.0.0.1");
		// new CannonControllerMultiplayer(connector);
		connector.start();
	}
	
	public boolean canShoot() {
		return shotBubble == null;
	}
	
	protected void fillBubbleSlots() {
		nextBubble = new ColouredBubble(bubbleMesh.getRandomRemainingColor());
		loadedBubble = new ColouredBubble(bubbleMesh.getRandomRemainingColor());
		
		correctBubblePositions();
	}
	
	public void correctBubblePositions() {
		loadedBubble.setPosition(LOADED_BUBBLE_POSITION);
		nextBubble.setPosition(NEXT_BUBBLE_POSITION);
	}
	
	protected void drawQueue(final Graphics graphics) {
		loadedBubble.render(graphics);
		nextBubble.render(graphics);
	}
	
	public void gameStep() throws GameOver {
		if (shotBubble != null) {
			shotBubble.gameStep();
			
			bubbleMesh
					.stream()
					.filter(bubble -> bubble.intersect(shotBubble)
							&& (bubble.isHittable() || bubbleMesh.bubbleIsTop(bubble))).findAny()
					.ifPresent(bubble -> this.collide(bubble));
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
	
	public void shootBubble(final Vector2f direction) {
		Point bubbleStartPosition = new Point((cannonPosition.x - AbstractBubble.WIDTH / 2)
				+ (int) (screenSize.height * direction.x), cannonPosition.y - AbstractBubble.HEIGHT
				/ 2 + (int) (screenSize.width * direction.y));
		shotBubble = new MovingBubble(bubbleStartPosition, new Vector2f(direction), screenSize,
				loadedBubble.getColor());
		loadedBubble = nextBubble;
		nextBubble = new ColouredBubble(bubbleMesh.getRandomRemainingColor());
		correctBubblePositions();
	}
	
	public void addTask(Task task) {
		// TODO Auto-generated method stub
		
	}
}
