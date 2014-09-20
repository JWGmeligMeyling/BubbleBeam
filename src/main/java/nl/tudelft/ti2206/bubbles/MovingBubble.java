package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.util.Vector2f;

/**
 * @author Sam Smulders
 * @author Luka Bavdaz
 *
 */
public class MovingBubble extends ColouredBubble {
	
	public static final long TIME_DENOM = 5;
	
	protected Vector2f velocity;
	protected Vector2f truePosition;
	protected Dimension screenSize;
	protected long previousTime;
	
	public MovingBubble(final Point position, final Vector2f velocity,
			final Dimension screenSize, final Color color) {
		super(color);
		this.screenSize = screenSize;
		this.truePosition = new Vector2f(position.x, position.y);
		this.velocity = velocity;
		this.previousTime = System.currentTimeMillis();
	}
	
	public void gameStep() {
		long timeDifference = getTimeDifference();
		truePosition = truePosition.add(velocity.multiply(timeDifference / TIME_DENOM));
		bounceOnWallCollision();
		this.setPosition(truePosition.toPoint());
	}
	
	protected long getTimeDifference() {
		long now = System.currentTimeMillis();
		long diff = now - previousTime;
		previousTime = now;
		return diff;
	}
	
	protected boolean hitsTopBorder(){
		return (truePosition.y <= 0);
	}

	/**
	 * 
	 */
	protected void bounceOnWallCollision() {
		if (truePosition.x + ColouredBubble.WIDTH > screenSize.width) {
			float xError = truePosition.x + ColouredBubble.WIDTH
					- (screenSize.width);
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		}
		else if (truePosition.x < 0) {
			float xError = truePosition.x;
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		}
		
		
	}
}
