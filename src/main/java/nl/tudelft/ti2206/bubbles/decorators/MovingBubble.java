package nl.tudelft.ti2206.bubbles.decorators;

import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;
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
public class MovingBubble extends DecoratedBubble implements Tickable {

	private static final long serialVersionUID = 6874942662206137844L;

	transient protected Vector2f truePosition;
	transient protected Vector2f velocity;
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
		super(bubble);
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
	 * Add velocity to this {@code MovingBubble}
	 */
	public void addVelocity() {
		this.velocity = this.velocity.add(bubble.velocityChange());
	}

}
