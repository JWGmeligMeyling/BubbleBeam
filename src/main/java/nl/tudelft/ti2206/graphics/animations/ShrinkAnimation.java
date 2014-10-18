package nl.tudelft.ti2206.graphics.animations;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import nl.tudelft.ti2206.bubbles.Bubble;

public class ShrinkAnimation extends Animation {
	public Bubble bubble;
	private int time;
	private Point position;
	
	public ShrinkAnimation(Bubble bubble) {
		this.bubble = bubble;
		this.position = this.bubble.getPosition();
		this.position.translate(this.bubble.getRadius(), this.bubble.getRadius());
		// this.bubble.setCenter(new Point(0, 0));
		this.bubble.setPosition(new Point(0, 0));
	}
	
	@Override
	public void render(final Graphics graphics) {
		final Graphics2D g2 = (Graphics2D) graphics;
		BufferedImage img = new BufferedImage(bubble.getWidth(), bubble.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		bubble.render(img.getGraphics());
		g2.drawImage(img, this.position.x - bubble.getWidth() / 2 + this.time, this.position.y
				- bubble.getHeight() / 2 + this.time, this.position.x + bubble.getWidth() / 2
				- this.time, this.position.y + bubble.getHeight() / 2 - this.time, 0, 0,
				bubble.getWidth(), bubble.getHeight(), null);
		this.time++;
	}
	
	@Override
	public boolean isDone() {
		return this.time == this.bubble.getRadius();
	}
}
