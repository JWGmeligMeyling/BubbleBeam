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
	public Set<Bubble> getBubblesToPop(Bubble target) {
		Set<Bubble> targets = Sets.newHashSet(target);
		Set<Bubble> neighboursInRadius = Sets.newHashSet(target);
		
		for(int i = 0; i < popRadius; i++) {
			targets.stream()
				.filter(Bubble::isHittable)
				.forEach(t -> neighboursInRadius.addAll(t.getNeighbours()));
			Set<Bubble> newTargets = Sets.newHashSet(neighboursInRadius);
			newTargets.removeAll(targets);
			targets = newTargets;
		}
		
		return targets;
	}

}
