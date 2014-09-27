package nl.tudelft.ti2206.bubbles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link SnapBehaviour} snaps {@link MovingBubbles} to this {@link Bubble}
 * 
 * @author Luka Bavdaz
 * @author Sam Smulders
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class SnapToSelf implements SnapBehaviour {
	
	private static final Logger log = LoggerFactory.getLogger(SnapBehaviour.class);

	private final BubblePlaceholder that;
	
	public SnapToSelf(BubblePlaceholder that) {
		this.that = that;
	}

	@Override
	public BubblePlaceholder getSnapPosition(Bubble bubble) {
		log.info("Snapping to top: {}", bubble);
		return that;
	}

}
