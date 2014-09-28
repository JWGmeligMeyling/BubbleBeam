package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BombBubble implements DecoratedBubble, PopRadius {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5406623504377849151L;
	private static final int POP_RADIUS = 2;
	protected final AbstractBubble bubble;
	protected static BufferedImage BOMB_IMAGE = _getBubbleImage();
	
	protected boolean collided = false;
		

	public BombBubble(){
		bubble = new AbstractBubble();
	}

	@Override
	public boolean popsWith(Bubble target) {
		return true;
	}

	protected static BufferedImage _getBubbleImage() {
		try {
			BufferedImage scale = ImageIO.read(BombBubble.class.getResourceAsStream("/bomb.png"));
			scale.getScaledInstance(AbstractBubble.WIDTH, AbstractBubble.HEIGHT, Image.SCALE_SMOOTH);
			return scale;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(BOMB_IMAGE, (int)bubble.getX(), (int)bubble.getY(), bubble.getWidth(), bubble.getHeight(), null);
	}
	
	@Override
	public Bubble getBubble() {
		return bubble;
	}
	
	@Override
	public int popRadius() {
		return POP_RADIUS;
	}

}
