package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;
import java.util.Set;

public class BubblePlaceholder extends AbstractBubble {

	@Override
	public void render(Graphics graphics) {
		super.render(graphics);
	}
	
	@Override
	public boolean intersect(Bubble other){
		return false;
	}
	
	@Override
	public boolean connectedToTop() {
		return false;
	}
	
	@Override
	public boolean connectedToTop(final Set<Bubble> traversed) {
		return false;
	}

}
