package nl.tudelft.ti2206.bubbles.decorators;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;

/**
 * Stone bubble implementation
 * 
 * @author Liam Clark
 *
 */

public class StoneBubble extends DecoratedBubble {

	private static final long serialVersionUID = -8390605724378971542L;
	private static BufferedImage STONE_IMAGE = _getBubbleImage();
	
	public StoneBubble() {
		this(new AbstractBubble());
	}
	
	public StoneBubble(Bubble bubble) {
		super(bubble);
	}
	
	protected static BufferedImage _getBubbleImage() {
		try {
			BufferedImage scale = ImageIO.read(StoneBubble.class.getResourceAsStream("/stone.png"));
			scale.getScaledInstance(AbstractBubble.WIDTH, AbstractBubble.HEIGHT, Image.SCALE_SMOOTH);
			return scale;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean popsWith(Bubble target) {
		return false;
	}
	
	@Override
	public boolean isHittable() {
		return true;
	}
	
	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(STONE_IMAGE, (int) bubble.getX(), (int) bubble.getY(),
				bubble.getWidth(), bubble.getHeight(), null);
		bubble.render(graphics);
	}
	
}
