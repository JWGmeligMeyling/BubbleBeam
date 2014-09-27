package nl.tudelft.ti2206.bubbles;

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
	public BubblePlaceholder getSnapPosition(Bubble bubble) {
		return that.getNeighboursOfType(BubblePlaceholder.class)
			.stream().min((BubblePlaceholder a, BubblePlaceholder b) ->
				a.getDistance(bubble) < b.getDistance(bubble) ? -1 : 1)
			.get();
	}

}
