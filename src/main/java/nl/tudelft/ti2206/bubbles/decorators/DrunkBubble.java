package nl.tudelft.ti2206.bubbles.decorators;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;
import nl.tudelft.util.Vector2f;

/**
 * The {@code DrunkBubble} is a {@link Bubble} that veers off his normal course
 * while it moves and instead goes slightly to either left or right.
 * {@code DrunkBubble} is a {@link Decorator} class and is considered a
 * 'powerup' bubble.
 * 
 * @author Luka Bavdaz
 */
public abstract class DrunkBubble implements DecoratedBubble {

	private static final long serialVersionUID = -1107340833975779377L;
	protected float acceleration;
	protected static final int IMAGE_TRANSLATION = 20;
	protected static final int ARROW_WIDTH = 23;
	protected static final int ARROW_HEIGHT = 19;
	protected static final float ACCELERATION = 0.4f;
	protected static BufferedImage ARROW_IMAGE = _getArrowImage();
	protected Bubble bubble;

	public DrunkBubble(Bubble bubble) {
		this.bubble = bubble;
	}

	@Override
	public Vector2f velocityChange() {
		return bubble.velocityChange().add(new Vector2f(acceleration, 0f));
	}

	protected static BufferedImage _getArrowImage() {
		try {
			BufferedImage scale = ImageIO.read(BombBubble.class
					.getResourceAsStream("/arrow.png"));
			scale.getScaledInstance(ARROW_WIDTH, ARROW_HEIGHT,
					Image.SCALE_SMOOTH);
			return scale;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Bubble getBubble() {
		return bubble;
	}

	@Override
	public void setBubble(Bubble bubble) {
		this.bubble = bubble;
	}

	@Override
	public Bubble getSnappedBubble() {
		return this.getBubble().getSnappedBubble();
	}

}
