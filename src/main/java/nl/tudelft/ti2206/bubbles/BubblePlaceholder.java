package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;

/**
 * The {@code BubblePlaceholder} fills a space in the {@link BubbleMesh} at
 * which a new Bubble can be snapped
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
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
