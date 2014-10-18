package nl.tudelft.ti2206.graphics.animations;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import nl.tudelft.ti2206.bubbles.Bubble;

public class FallAnimation extends FiniteAnimation {
	private static final int FALL_SPEED = 4;
	public Bubble bubble;
	private Point position;
	
	public FallAnimation(Bubble bubble) {
		super(bubble.getHeight());
		this.bubble = bubble;
		this.position = this.bubble.getPosition();
		this.position.translate(this.bubble.getRadius(), this.bubble.getRadius());
		this.bubble.setPosition(new Point(0, 0));
	}
	
	@Override
	public void render(final Graphics graphics) {
		final Graphics2D g2 = (Graphics2D) graphics;
		BufferedImage img = new BufferedImage(bubble.getWidth(), bubble.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		bubble.render(img.getGraphics());
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f / maxTime
				* (maxTime - time));
		g2.setComposite(ac);
		
		g2.drawImage(img, this.position.x - bubble.getWidth() / 2,
				this.position.y - bubble.getHeight() / 2 + time * FALL_SPEED, this.position.x
						+ bubble.getWidth() / 2, this.position.y + bubble.getHeight() / 2 + time
						* FALL_SPEED, 0, 0, bubble.getWidth(), bubble.getHeight(), null);
		this.time++;
	}
}
