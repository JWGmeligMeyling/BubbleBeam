package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;

public class ClassicBubbleFactory extends BubbleFactory {
	
	protected final Random RANDOM_GENERATOR = new Random();
	
	@Override
	public Bubble createBubble(Set<Color> remainingColors) {
		// create new colouredBubble
		final int index = RANDOM_GENERATOR.nextInt(remainingColors.size());
		final Iterator<Color> iterator = remainingColors.iterator();
		for (int i = 0; i <= index; i++)
		{
			if (i == index) {
				return new ColouredBubble(iterator.next());
			} else {
				iterator.next();
			}
		}
		throw new IndexOutOfBoundsException();
	}
}
