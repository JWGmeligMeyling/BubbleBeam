package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleRight;

public class DrunkBubbleFactory extends BubbleFactory {

	protected final Random RANDOM_GENERATOR = new Random();
	
	protected final Class<?> DrunkBubbles[] = { DrunkBubbleRight.class,
			DrunkBubbleLeft.class };
	
	@Override
	public Bubble createBubble(Set<Color> remainingColors) {
		Bubble out = createColouredBubble(remainingColors);
		final int index = RANDOM_GENERATOR.nextInt(DrunkBubbles.length);
		
		try {
			Class<?> clasz = DrunkBubbles[index];
			return Bubble.class.cast(clasz.getConstructor(Bubble.class).newInstance(out));
		} catch (Throwable t) {
			return out;
		}
	}
	
	protected Bubble createColouredBubble(Set<Color> remainingColors) {
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
