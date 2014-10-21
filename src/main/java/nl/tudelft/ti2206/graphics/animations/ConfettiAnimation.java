package nl.tudelft.ti2206.graphics.animations;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.cannon.Cannon;

public class ConfettiAnimation extends FiniteAnimation {
	protected static Random random = new Random();
	protected final static BufferedImage CONFETTI_STRIP = _getCannonImage();
	protected final static int STRIP_SIZE = 9;
	protected static final float MIN_CONFETTI_SPEED = 0.5f;
	protected static final float MAX_CONFETTI_SPEED = 5f;
	protected static final int CONFETTI_RADIUS = 9;
	protected static final int CONFETTI_HALF_WIDTH = 6;
	protected static final int CONFETTI_WIDTH = 11;
	
	protected float[] direction = new float[STRIP_SIZE];
	protected float[] angle = new float[STRIP_SIZE];
	protected float[] speed = new float[STRIP_SIZE];
	
	public Bubble bubble;
	private Point position;
	
	public ConfettiAnimation(int maxTime, Bubble bubble) {
		super(maxTime);
		this.bubble = bubble;
		Point pos = bubble.getPosition();
		this.position = new Point(pos.x, pos.y);
		this.position.translate(this.bubble.getRadius(), this.bubble.getRadius());
		this.bubble.setPosition(new Point(0, 0));
		for (int i = 0; i < STRIP_SIZE; i++) {
			direction[i] = (float) (random.nextFloat() * Math.PI);
			angle[i] = (float) (random.nextFloat() * Math.PI);
			speed[i] = random.nextFloat() * (MAX_CONFETTI_SPEED - MIN_CONFETTI_SPEED)
					+ MIN_CONFETTI_SPEED;
		}
	}
	
	// confettiStrip.png
	
	@Override
	public void render(Graphics g) {
		
		int size = (int) (time * MAX_CONFETTI_SPEED) + CONFETTI_RADIUS;
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics imgGraphics = img.getGraphics();
		imgGraphics.translate(size / 2, size / 2);
		for (int i = 0; i < STRIP_SIZE; i++) {
			int x = (int) (Math.cos(direction[i]) * speed[i] * time);
			int y = (int) (Math.sin(direction[i]) * speed[i] * time);
			imgGraphics.drawImage(CONFETTI_STRIP, -CONFETTI_HALF_WIDTH + x, -CONFETTI_HALF_WIDTH
					+ y, CONFETTI_HALF_WIDTH + x, CONFETTI_HALF_WIDTH + y, CONFETTI_WIDTH * i, 0,
					CONFETTI_WIDTH * i + CONFETTI_WIDTH, CONFETTI_WIDTH, null);
		}
		
		Graphics2D g2 = (Graphics2D) g;
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f / maxTime
				* (maxTime - time));
		g2.setComposite(ac);
		g2.drawImage(img, this.position.x - size / 2, this.position.y - size / 2, this.position.x
				+ size / 2, this.position.y + size / 2, 0, 0, size, size, null);
		this.time++;
		
		// graphics.rotate(-angle + Math.PI / 2, position.x, position.y);
		// graphics.drawImage(CONFETTI_STRIP, position.x - WIDTH / 2, position.y
		// - HEIGHT / 2
		// - ROTATE_TRANSLATION, position.x + WIDTH / 2, position.y + HEIGHT / 2
		// - ROTATE_TRANSLATION, 0, 0, CONFETTI_STRIP.getWidth(),
		// CONFETTI_STRIP.getHeight(),
		// null);
		// graphics.rotate(bAngle, position.x, position.y);
	}
	
	private static BufferedImage _getCannonImage() {
		try {
			return ImageIO.read(ConfettiAnimation.class.getResourceAsStream("/confettiStrip.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
