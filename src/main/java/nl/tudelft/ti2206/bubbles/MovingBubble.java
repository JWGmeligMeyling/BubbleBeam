package nl.tudelft.ti2206.bubbles;

import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.game.backend.Tickable;
import nl.tudelft.util.Vector2f;

/**
 * The {@code MovingBubble} is a special type of {@link ColouredBubble} which
 * can be animated in order to perform a shoot action.
 * 
 * @author Sam Smulders
 * @author Luka Bavdaz
 * @author Jan-Willem Gmelig Meyling
 * @author Liam Clark
 */
public class MovingBubble implements Tickable, DecoratedBubble {

	private static final long serialVersionUID = 6874942662206137844L;

	transient protected Vector2f truePosition;
	transient protected Vector2f velocity;
	transient protected Bubble bubble;
	transient protected final Dimension screenSize;

	/**
	 * Construct a new {@code MovingBubble}
	 * 
	 * @param position
	 *            position to initialize the moving bubble (the cannon)
	 * @param velocity
	 *            angle for the bubble
	 * @param screenSize
	 *            screen size, which is required to perform bounces on the walls
	 * @param color
	 *            {@code Color} for this {@code Bubble}
	 */
	public MovingBubble(final Vector2f velocity, final Dimension screenSize,
			final Bubble bubble) {
		this.bubble = bubble;
		this.screenSize = screenSize;
		Point position = bubble.getPosition();
		this.truePosition = new Vector2f(position.x, position.y);
		this.velocity = velocity;
	}

	@Override
	public void gameTick() {
		truePosition = truePosition.add(velocity);
		bounceOnWallCollision();
		this.setPosition(truePosition.toPoint());
	}

	protected void bounceOnWallCollision() {
		int width = this.getWidth();
		if (truePosition.x + width > screenSize.width) {
			float xError = truePosition.x + width - (screenSize.width);
			truePosition = truePosition.add(velocity.multiply(-xError
					/ velocity.x));
			velocity.x = -velocity.x;
		} else if (truePosition.x < 0) {
			float xError = truePosition.x;
			truePosition = truePosition.add(velocity.multiply(-xError
					/ velocity.x));
			velocity.x = -velocity.x;
		}

	}

	/**
	 * Set the true position for this {@code MovingBubble}
	 * 
	 * @param truePosition
	 */
	public void setTruePosition(Vector2f truePosition) {
		this.truePosition = truePosition;
	}

	/**
	 * Get the true position for this {@code MovingBubble}
	 * 
	 * @return
	 */
	public Vector2f getTruePosition() {
		return truePosition;
	}

	/**
	 * Get the velocity for this {@code MovingBubble}
	 * 
	 * @return the velocity for this {@code MovingBubble}
	 */
	public Vector2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

	/**
	 * Get the screen size for this {@code MovingBubble}
	 * 
	 * @return the screen size for this {@code MovingBubble}
	 */
	public Dimension getScreenSize() {
		return screenSize;
	}

	@Override
	public Bubble getBubble() {
		return bubble;
	}

	@Override
	public Bubble getSnappedBubble() {
		return this.getBubble().getSnappedBubble();
	}

	@Override
	public void setBubble(Bubble bubble) {
		this.bubble = bubble;
	}
}
