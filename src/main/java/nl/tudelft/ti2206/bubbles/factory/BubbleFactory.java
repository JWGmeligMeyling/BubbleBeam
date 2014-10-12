package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;

/**
 * Extend this class to create bubbles for your custom GameMode
 * @author leon
 *
 */
public abstract class BubbleFactory {

	public abstract Bubble createBubble(Set<Color> remainingColors);
}
