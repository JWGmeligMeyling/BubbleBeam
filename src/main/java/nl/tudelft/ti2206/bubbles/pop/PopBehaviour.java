package nl.tudelft.ti2206.bubbles.pop;

import java.io.Serializable;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;

/**
 * The {@code PopBehaviour} defines a way to search for {@link Bubble Bubbles}
 * that should pop along with this {@code Bubble}, if any.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface PopBehaviour extends Serializable {

	/**
	 * Get the {@link Bubble Bubbles} that should pop along with this
	 * {@code Bubble}, if any.
	 * 
	 * @param target
	 *            The {@code Bubble} at which to start popping
	 * @return a {@link Set} containing the {@code Bubbles} that should pop,
	 *         including the target {@code Bubble}
	 */
	Set<Bubble> getBubblesToPop(Bubble target);
	
	/**
	 * Determine if a {@code Set} of {@link Bubble Bubbles} to be popped is valid
	 * @param targets
	 * 		{@code Bubbles} to be popped
	 * @return
	 * 		true if the {@code Bubbles} should pop
	 */
	boolean isValidPop(Set<Bubble> targets);
	
}
