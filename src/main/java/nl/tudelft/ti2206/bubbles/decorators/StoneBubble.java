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
	private transient BufferedImage stoneImage;
	
	public StoneBubble() {
		this(new AbstractBubble());
	}
	
	public StoneBubble(Bubble bubble) {
		super(bubble);
	}
	
	protected BufferedImage getBubbleImage() {
		try {
			BufferedImage scaledImage = ImageIO.read(StoneBubble.class.getResourceAsStream("/stone.png"));
			scaledImage.getScaledInstance(bubble.getWidth(), bubble.getHeight(), Image.SCALE_SMOOTH);
			return scaledImage;
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
		if(stoneImage == null) stoneImage = getBubbleImage();
		graphics.drawImage(stoneImage, bubble.getX(), bubble.getY(), bubble.getWidth(), bubble.getHeight(), null);
		bubble.render(graphics);
	}
	
}
