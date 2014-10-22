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
	protected Point position;
	protected final static BufferedImage EXPLOSION_STRIP = _getExplosionImage();
	
	public ExplosionAnimation(Bubble bubble) {
		super(15);
		Point pos = bubble.getPosition();
		this.position = new Point(pos.x, pos.y);
		this.position.translate(bubble.getRadius(), bubble.getRadius());
	}
	
	private static BufferedImage _getExplosionImage() {
		try {
			return ImageIO.read(ConfettiAnimation.class.getResourceAsStream("/explosionStrip.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void render(final Graphics graphics) {
		graphics.drawImage(EXPLOSION_STRIP, this.position.x - EXPLOSION_STRIP.getHeight() / 2,
				this.position.y - EXPLOSION_STRIP.getHeight() / 2, this.position.x
						+ EXPLOSION_STRIP.getHeight() / 2,
				this.position.y + EXPLOSION_STRIP.getHeight() / 2, EXPLOSION_STRIP.getHeight()
						* time, 0,
				EXPLOSION_STRIP.getHeight() * time + EXPLOSION_STRIP.getHeight(),
				EXPLOSION_STRIP.getHeight(), null);
	}
}
