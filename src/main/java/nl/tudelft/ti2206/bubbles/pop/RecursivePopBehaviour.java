package nl.tudelft.ti2206.bubbles.pop;

import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;

import com.google.common.collect.Sets;

/**
 * The {@code RecursivePopBehaviour} is the default {@link PopBehaviour} that
 * seeks {@link Bubble#isHittable() hittable} bubbles that pop with each others,
 * defined by their {@link Bubble#popsWith(Bubble)} implementation.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class RecursivePopBehaviour implements PopBehaviour {

	private static final long serialVersionUID = 279908381407272175L;
	
	public final static int MINIMAL_POP_SIZE = 3;

	@Override
	public Set<Bubble> getBubblesToPop(final Bubble target) {
		return getBubblesToPop(target, Sets.newHashSet(target));
	}

	protected Set<Bubble> getBubblesToPop(Bubble target, Set<Bubble> bubblesToPop) {
		target.getNeighbours().stream()
			.filter(bubble -> bubble.isHittable() &&
					(bubble.popsWith(target) || target.popsWith(bubble)) &&
					bubblesToPop.add(bubble))
			.forEach(bubble -> getBubblesToPop(bubble, bubblesToPop));
		return bubblesToPop;
	}

	@Override
	public boolean isValidPop(final Set<Bubble> targets) {
		return targets.size() >= MINIMAL_POP_SIZE;
	}
	
}
