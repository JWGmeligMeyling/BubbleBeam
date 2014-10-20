package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;

public class ClassicBubbleFactory extends AbstractBubbleFactory {
	
	protected final Random RANDOM_GENERATOR = new Random();
	
	@Override
	public Bubble createBubble(Set<Color> remainingColors) {
		
		if(remainingColors.isEmpty()) {
			throw new IllegalArgumentException("There should be a remaining color to create a new bubble");
		}
		
		return createColouredBubble(remainingColors);
		
	}
}
