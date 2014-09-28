package nl.tudelft.ti2206.bubbles;

import nl.tudelft.ti2206.bubbles.snap.SnapToSelf;

/**
 * The {@code BubblePlaceholder} fills a space in the {@link BubbleMesh} at
 * which a new Bubble can be snapped
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class BubblePlaceholder extends AbstractBubble {
	
	private static final long serialVersionUID = 4254751857830339489L;

	public BubblePlaceholder() {
		snapBehaviour = new SnapToSelf(this);
	}
	
}
