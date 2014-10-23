package nl.tudelft.ti2206.bubbles.snap;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link SnapBehaviour} snaps {@link MovingBubble MovingBubbles} to this {@link Bubble}
 * 
 * @author Luka Bavdaz
 * @author Sam Smulders
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class SnapToSelf implements SnapBehaviour {
	
	private static final long serialVersionUID = 532307379653361657L;

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
