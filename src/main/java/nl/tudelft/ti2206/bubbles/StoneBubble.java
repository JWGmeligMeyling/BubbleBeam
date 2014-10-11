package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Stone bubble implementation
 * 
 * @author LC
 *
 */

public class StoneBubble implements DecoratedBubble {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8390605724378971542L;
	private Bubble bubble;
	private static BufferedImage STONE_IMAGE = _getBubbleImage();
	
	public StoneBubble() {
		this(null);
	}

	public StoneBubble(DecoratedBubble bubble2) {
		if(bubble2 != null){
			bubble = bubble2;
		} else{
			bubble = new AbstractBubble();
		}
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
	public Bubble getBubble() {
		return bubble;
	}
	
	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(STONE_IMAGE, (int) bubble.getX(), (int) bubble.getY(),
				bubble.getWidth(), bubble.getHeight(), null);
		bubble.render(graphics);
	}
	
	@Override
	public void setBubble(Bubble bubble) {
		this.bubble = bubble;
	}
}
