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
 *
 */
public class MovingBubble implements Tickable, DecoratedBubble {
	
	private static final long serialVersionUID = 6874942662206137844L;
	
	transient protected Vector2f truePosition;
	transient protected final Bubble bubble;
	transient protected final Vector2f velocity;
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
	public MovingBubble(final Point position, final Vector2f velocity, final Dimension screenSize,
			final Bubble bubble) {
		this.bubble = bubble;
		this.screenSize = screenSize;
		this.truePosition = new Vector2f(position.x, position.y);
		setPosition(position);
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
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		} else if (truePosition.x < 0) {
			float xError = truePosition.x;
			truePosition = truePosition.add(velocity.multiply(-xError / velocity.x));
			velocity.x = -velocity.x;
		}
		
	}
	
	public void setTruePosition(Vector2f truePosition) {
		this.truePosition = truePosition;
	}
	
	public Vector2f getTruePosition() {
		return truePosition;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public Dimension getScreenSize() {
		return screenSize;
	}
	
	public Bubble getBubble() {
		return bubble;
	}
	
}
