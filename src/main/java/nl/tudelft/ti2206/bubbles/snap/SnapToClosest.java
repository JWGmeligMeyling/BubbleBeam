package nl.tudelft.ti2206.bubbles.snap;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;

/**
 * The {@code SnapToClosest} {@link SnapBehaviour} snaps to the closest
 * {@link BubblePlaceholder}
 * 
 * @author Luka Bavdaz
 * @author Sam Smulders
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class SnapToClosest implements SnapBehaviour {
	
	private static final long serialVersionUID = -4271971313720057746L;
	private final Bubble that;
	
	public SnapToClosest(Bubble that) {
		this.that = that;
	}

	@Override
	public Bubble getSnapPosition(Bubble bubble) {
		return that
			.getNeighbours()
			.stream()
			.filter(a -> !a.isHittable())
			.min((Bubble a, Bubble b) -> a.getDistance(bubble) < b
					.getDistance(bubble) ? -1 : 1).get();
	}

}
