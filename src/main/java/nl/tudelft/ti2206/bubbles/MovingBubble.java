package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.game.tick.Tickable;
import nl.tudelft.util.Vector2f;

/**
 * @author Sam Smulders
 * @author Luka Bavdaz
 *
 */
public class MovingBubble extends ColouredBubble implements Tickable {
	
	private static final long serialVersionUID = 6874942662206137844L;
	
	transient protected Vector2f velocity;
	transient protected Vector2f truePosition;
	transient protected Dimension screenSize;
	
	public MovingBubble(final Point position, final Vector2f velocity, final Dimension screenSize,
			final Color color) {
		super(color);
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
