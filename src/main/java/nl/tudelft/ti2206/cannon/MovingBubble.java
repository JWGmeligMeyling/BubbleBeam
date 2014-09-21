package nl.tudelft.ti2206.cannon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.util.Vector2f;

/**
 * @author Sam Smulders
 * @author Luka Bavdaz
 *
 */
public class MovingBubble extends ColouredBubble {
	
	private static final long serialVersionUID = -5720287124346974296L;

	public static final long TIME_DENOM = 5;
	
	transient protected Vector2f velocity;
	transient protected Vector2f truePosition;
	transient protected Dimension screenSize;
	transient protected Point screenLocation;
	transient protected long previousTime;
	
	public MovingBubble(final Point position, final Vector2f velocity,
			final Dimension screenSize, final Point screenLocation,
			final Color color) {
		
		super(color);
		this.screenSize = screenSize;
		this.position = position;
		this.truePosition = new Vector2f(position.x, position.y);
		this.screenLocation = screenLocation;
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
		return (truePosition.y <= screenLocation.y);
	}

	/**
	 * 
	 */
	protected void bounceOnWallCollision() {
		if (truePosition.x + ColouredBubble.WIDTH > screenSize.width + screenLocation.x) {
			float xError = truePosition.x + ColouredBubble.WIDTH
					- (screenSize.width + screenLocation.x);
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		}
		else if (truePosition.x < screenLocation.x) {
			float xError = truePosition.x - screenLocation.x;
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		}
		
		
	}
}
