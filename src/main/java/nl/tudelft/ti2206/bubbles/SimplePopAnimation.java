package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;
import java.awt.Graphics2D;

import nl.tudelft.ti2206.graphics.Animation;

public class SimplePopAnimation extends Animation {
	public Bubble bubble;
	private double time;

	@Override
	public void render(final Graphics graphics) {
		final Graphics2D g2 = (Graphics2D) graphics;
		g2.scale(time, time);
		bubble.render(graphics);
		g2.scale(1/time, 1/time);
	}

	@Override
	public boolean isDone() {
		return time == 10;
	}
}
