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
	
	protected Vector2f velocity;
	protected Vector2f truePosition;
	protected Dimension screenSize;
	
	public MovingBubble(final Point position, final Vector2f velocity, final Dimension screenSize,
			final Color color) {
		super(color);
		this.screenSize = screenSize;
		this.truePosition = new Vector2f(position.x, position.y);
		this.position = position;
		this.velocity = velocity;
	}
	
	public void gameStep() {
		truePosition = truePosition.add(velocity);
		bounceOnWallCollision();
		this.setPosition(truePosition.toPoint());
	}
	
	protected boolean hitsTopBorder() {
		return (truePosition.y <= 0);
	}
	
	protected void bounceOnWallCollision() {
		if (truePosition.x + ColouredBubble.WIDTH > screenSize.width) {
			float xError = truePosition.x + ColouredBubble.WIDTH - (screenSize.width);
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		} else if (truePosition.x < 0) {
			float xError = truePosition.x;
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		}
		
	}
}
