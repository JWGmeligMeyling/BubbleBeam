package nl.tudelft.ti2206.graphics.animations;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.bubbles.Bubble;

/**
 * 
 * @author Sam Smulders
 */
public class ExplosionAnimation extends FiniteAnimation {
	
	protected final static BufferedImage EXPLOSION_STRIP = _getExplosionImage();
	protected final static int HEIGHT = EXPLOSION_STRIP.getHeight();
	protected final static int IMAGE_ON_STRIP_WIDTH = HEIGHT;
	
	protected final Point position;
	
	public ExplosionAnimation(Bubble bubble) {
		super(15);
		Point pos = bubble.getPosition();
		int radius = bubble.getRadius();
		this.position = new Point(pos.x + radius, pos.y + radius);
		this.position.translate(-IMAGE_ON_STRIP_WIDTH / 2, -HEIGHT / 2);
	}
	
	private static BufferedImage _getExplosionImage() {
		try {
			return ImageIO.read(ConfettiAnimation.class.getResourceAsStream("/explosionStrip.png"));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void render(final Graphics graphics) {
		int beginXOfImageOnStrip = time * IMAGE_ON_STRIP_WIDTH;
		int endXOfImageOnStrip = beginXOfImageOnStrip + IMAGE_ON_STRIP_WIDTH;
		
		int xBegin = this.position.x;
		int xEnd = xBegin + IMAGE_ON_STRIP_WIDTH;
		
		int yBegin = this.position.y;
		int yEnd = yBegin + HEIGHT;
		
		graphics.drawImage(EXPLOSION_STRIP, xBegin, yBegin, xEnd, yEnd, beginXOfImageOnStrip, 0,
				endXOfImageOnStrip, HEIGHT, null);
	}
}
