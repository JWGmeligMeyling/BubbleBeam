package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;

public abstract class BubbleFactory {

	public abstract Bubble createBubble(Set<Color> remainingColors);
}
