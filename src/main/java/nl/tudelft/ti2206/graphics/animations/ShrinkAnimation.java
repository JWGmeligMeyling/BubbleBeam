package nl.tudelft.ti2206.graphics.animations;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import nl.tudelft.ti2206.bubbles.Bubble;

/**
 * ShrinkAnimation animates a bubble shrinking to nothing.
 * 
 * @author Sam Smulders
 */
public class ShrinkAnimation extends FiniteAnimation {
	
	protected final Bubble bubble;
	protected final Point position;
	protected final int width, height;
	
	public ShrinkAnimation(Bubble bubble) {
		super(bubble.getRadius());
		Point pos = bubble.getPosition();
		int radius = bubble.getRadius();
		
		this.bubble = bubble;
		this.position = new Point(pos.x + radius, pos.y + radius);
		this.width = bubble.getWidth();
		this.height = bubble.getHeight();
		this.bubble.setPosition(new Point(0, 0));
	}
	
	@Override
	public void render(final Graphics graphics) {
		final Graphics2D g2 = (Graphics2D) graphics;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		int shrink = this.time;
		
		int xBegin = this.position.x - width / 2;
		int xEnd = xBegin + width - shrink;
		xBegin += shrink;
		
		int yBegin = this.position.y - height / 2;
		int yEnd = yBegin + height - shrink;
		yBegin += shrink;
		
		bubble.render(img.getGraphics());
		g2.drawImage(img, xBegin, yBegin, xEnd, yEnd, 0, 0, width, height, null);
	}
}
