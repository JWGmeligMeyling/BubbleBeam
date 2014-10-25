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

/**
 * ConfettiAnimation animates confetti thrown around.
 * 
 * @author Sam_
 *
 */
public class ConfettiAnimation extends FiniteAnimation {
	protected static Random random = new Random();
	
	protected final static BufferedImage CONFETTI_STRIP = _getConfettiImage();
	protected final static int HEIGHT = CONFETTI_STRIP.getHeight();
	protected final static int CONFETTI_ON_STRIP_WIDTH = HEIGHT;
	protected static final int CONFETTI_RADIUS = (int) Math.sqrt(HEIGHT * HEIGHT * 2);
	
	protected final static int STRIP_SIZE = 9;
	protected static final double MIN_CONFETTI_SPEED = 2f;
	protected static final double MAX_CONFETTI_SPEED = 5f;
	protected static final int CONFETTI_HALF_WIDTH = 6;
	
	protected final double[] direction = new double[STRIP_SIZE];
	protected final double[] angle = new double[STRIP_SIZE];
	protected final double[] speed = new double[STRIP_SIZE];
	
	private final Point position;
	
	public ConfettiAnimation(int maxTime, Bubble bubble) {
		super(maxTime);
		int radius = bubble.getRadius();
		Point pos = bubble.getPosition();
		this.position = new Point(pos.x + radius, pos.y + radius);
		for (int i = 0; i < STRIP_SIZE; i++) {
			direction[i] = random.nextFloat() * Math.PI * 2;
			angle[i] = random.nextFloat() * Math.PI * 2;
			speed[i] = random.nextFloat() * (MAX_CONFETTI_SPEED - MIN_CONFETTI_SPEED)
					+ MIN_CONFETTI_SPEED;
		}
	}
	
	@Override
	public void render(Graphics g) {
		BufferedImage img = createConfettiImage();
		
		Graphics2D g2 = (Graphics2D) g;
		setAlpha(g2);
		
		/*
		 * The size is equal to the maximum possible travelled time by the
		 * confetti, multiplied by 2 because the confetti speed can be negative.
		 */
		int size = (int) (time * MAX_CONFETTI_SPEED * 2) + CONFETTI_RADIUS;
		
		int xBegin = this.position.x - size / 2;
		int xEnd = xBegin + size;
		
		int yBegin = this.position.y - size / 2;
		int yEnd = yBegin + size;
		
		g2.drawImage(img, xBegin, yBegin, xEnd, yEnd, 0, 0, size, size, null);
	}
	
	protected void setAlpha(Graphics2D g2) {
		float alpha = 1f / maxTime * this.getTimeLeft();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2.setComposite(ac);
	}
	
	protected BufferedImage createConfettiImage() {
		int size = (int) (time * MAX_CONFETTI_SPEED) * 2 + CONFETTI_RADIUS;
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		renderConfetti((Graphics2D) img.getGraphics(), size);
		return img;
	}
	
	protected void renderConfetti(Graphics2D imgGraphics, int size) {
		int halfSize = size / 2;
		imgGraphics.translate(halfSize, halfSize);
		for (int i = 0; i < STRIP_SIZE; i++) {
			renderTranslatedConfettiParticle(imgGraphics, i);
		}
	}
	
	protected void renderTranslatedConfettiParticle(Graphics2D imgGraphics, int nr) {
		int x = (int) (Math.cos(direction[nr]) * speed[nr] * time);
		int y = (int) (Math.sin(direction[nr]) * speed[nr] * time);
		imgGraphics.translate(x, y);
		imgGraphics.rotate(angle[nr]);
		
		renderConfettiParticle(imgGraphics, nr);
		
		imgGraphics.rotate(-angle[nr]);
		imgGraphics.translate(-x, -y);
	}
	
	protected void renderConfettiParticle(Graphics2D imgGraphics, int nr) {
		int beginXOfImageOnStrip = CONFETTI_ON_STRIP_WIDTH * nr;
		int endXOfImageOnStrip = beginXOfImageOnStrip + CONFETTI_ON_STRIP_WIDTH;
		
		int xBegin = -CONFETTI_ON_STRIP_WIDTH / 2;
		int xEnd = xBegin + CONFETTI_ON_STRIP_WIDTH;
		
		int yBegin = -HEIGHT / 2;
		int yEnd = yBegin + HEIGHT;
		
		imgGraphics.drawImage(CONFETTI_STRIP, xBegin, yBegin, xEnd, yEnd, beginXOfImageOnStrip, 0,
				endXOfImageOnStrip, CONFETTI_ON_STRIP_WIDTH, null);
	}
	
	private static BufferedImage _getConfettiImage() {
		try {
			return ImageIO.read(ConfettiAnimation.class.getResourceAsStream("/confettiStrip.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
