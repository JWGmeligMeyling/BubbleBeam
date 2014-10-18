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
	protected transient BufferedImage bombImage;

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

	protected BufferedImage getBubbleImage() {
		try {
			BufferedImage scaledImage = ImageIO.read(BombBubble.class.getResourceAsStream("/bomb.png"));
			scaledImage.getScaledInstance(bubble.getWidth(), bubble.getHeight(), Image.SCALE_SMOOTH);
			return scaledImage;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void render(Graphics graphics) {
		if(bombImage == null) bombImage = getBubbleImage();
		graphics.drawImage(bombImage, bubble.getX(), bubble.getY(), bubble.getWidth(), bubble.getHeight(), null);
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
