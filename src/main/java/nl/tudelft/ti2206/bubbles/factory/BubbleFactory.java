package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;

/**
 * Extend this class to create bubbles for your custom GameMode
 * 
 * @author Leon Hoek
 *
 */
public interface BubbleFactory {
	
	/**
	 * Create a new {@link Bubble} according to the set of remaining colors
	 * 
	 * @param remainingColors
	 * @return a new {@link Bubble}
	 */
	Bubble createBubble(Set<Color> remainingColors);
}
