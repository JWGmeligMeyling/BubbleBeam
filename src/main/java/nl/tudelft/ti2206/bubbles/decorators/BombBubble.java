package nl.tudelft.ti2206.bubbles.decorators;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;
import nl.tudelft.ti2206.bubbles.pop.RadialPopBehaviour;

/**
 * The {@code BombBubble} is a {@link Bubble} that explodes on impact, popping
 * bubbles in a radius of {@code POP_RADIUS}. {@code BombBubble} implements the
 * {@link Decorator} class and is considered a 'powerup' bubble.
 * 
 * @author leon
 */
public class BombBubble extends DecoratedBubble {

	private static final long serialVersionUID = -5406623504377849151L;
	private static final int POP_RADIUS = 2;

	protected final RadialPopBehaviour popBehaviour;
	protected static BufferedImage BOMB_IMAGE = _getBubbleImage();

	protected boolean collided = false;

	public BombBubble() {
		this(new AbstractBubble());
	}
	
	public BombBubble(Bubble bubble) {
		super(new SoundBubble("bomb.wav", bubble));
		popBehaviour = new RadialPopBehaviour(POP_RADIUS);
	}

	@Override
	public boolean popsWith(Bubble target) {
		// Returns true, it immidiately pops itself and it's closest neighbours
		// and isn't expected to remain in the game
		return true;
	}

	protected static BufferedImage _getBubbleImage() {
		try {
			BufferedImage scale = ImageIO.read(BombBubble.class
					.getResourceAsStream("/bomb.png"));
			scale.getScaledInstance(AbstractBubble.WIDTH,
					AbstractBubble.HEIGHT, Image.SCALE_SMOOTH);
			return scale;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(BOMB_IMAGE, (int) bubble.getX(),
				(int) bubble.getY(), bubble.getWidth(), bubble.getHeight(),
				null);
		bubble.render(graphics);
	}

	@Override
	public boolean isHittable() {
		return true;
	}

	@Override
	public RadialPopBehaviour getPopBehaviour() {
		return popBehaviour;
	}

}
