package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;

public class BubblePlaceholder extends AbstractBubble {

	@Override
	public void render(Graphics graphics) {
		super.render(graphics);
	}
	
	@Override
	public BubblePlaceholder getSnapPosition(final Bubble bubble) {
		return this;
	}	
}
