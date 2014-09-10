package nl.tudelft.ti2206.cannon;

import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.util.Vector2f;

/**
 * @author Sam Smulders
 * @author Luka Bavdaz
 *
 */
public class MovingBubble {
	
	private static final float SPEED_MULTIPLIER = 10f;
	
	protected Vector2f velocity;
	protected Vector2f truePosition;
	protected Bubble bubble;
	protected Dimension screenSize;
	protected Point screenLocation;
	
	public MovingBubble(Point position, Bubble bubble, Vector2f velocity, Dimension screenSize,
			Point screenLocation) {
		this.bubble = bubble;
		this.screenSize = screenSize;
		this.truePosition = new Vector2f(position.x, position.y);
		this.screenLocation = screenLocation;
		this.velocity = velocity;
	}
	
	public void gameStep() {
		truePosition = truePosition.add(velocity.multiply(SPEED_MULTIPLIER));
		
		bounceOnWallCollision();
		
		bubble.setPosition(new Point((int) Math.round(truePosition.x), (int) Math
				.round(truePosition.y)));
	}
	/**
	 * 
	 */
	private void bounceOnWallCollision() {
		if (truePosition.x + ColouredBubble.WIDTH > screenSize.width + screenLocation.x) {
			float xError = truePosition.x + ColouredBubble.WIDTH
					- (screenSize.width + screenLocation.x);
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		} else if (truePosition.x < screenLocation.x) {
			float xError = truePosition.x - screenLocation.x;
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		}
	}

	public Bubble getBubble() {
		return bubble;
	}
}
