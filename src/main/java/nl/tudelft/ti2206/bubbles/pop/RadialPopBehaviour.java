package nl.tudelft.ti2206.bubbles.pop;

import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;

import com.google.common.collect.Sets;

/**
 * The RadialPopBehaviour is a {@link PopBehaviour} that causes {@link Bubbles} in a
 * certain radius from the target {@code Bubble} to pop.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class RadialPopBehaviour implements PopBehaviour {
	
	private static final long serialVersionUID = -4431821356745981916L;
	
	private final int popRadius;
	
	/**
	 * Construct a new {@code RadialPopBehaviour}
	 * @param popRadius
	 * 		Radius for the behaviour
	 */
	public RadialPopBehaviour(int popRadius) {
		this.popRadius = popRadius;
	}

	@Override
	public Set<Bubble> getBubblesToPop(final Bubble target) {
		return getBubblesToPop(target, Sets.newHashSet(), 0);
	}

	protected Set<Bubble> getBubblesToPop(final Bubble target,
			final Set<Bubble> targets, final int depth) {
		
		if(depth <= popRadius && targets.add(target)) {
			target.getNeighbours().stream()
				.filter(Bubble::isHittable)
				.forEach(bubble -> getBubblesToPop(bubble, targets, depth + 1));
		}
		
		return targets;
	}

	@Override
	public boolean isValidPop(Set<Bubble> targets) {
		return true;
	}

}
