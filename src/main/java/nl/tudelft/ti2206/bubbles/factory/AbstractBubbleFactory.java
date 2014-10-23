package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.ColouredBubble;

/**
 * Base class for the {@link BubbleFactory BubbleFactories}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public abstract class AbstractBubbleFactory implements BubbleFactory {
	
	protected final Random random;
	
	protected AbstractBubbleFactory() {
		this(new Random());
	}
	
	protected AbstractBubbleFactory(Random random) {
		this.random = random;
	}

	protected ColouredBubble createColouredBubble(Set<Color> remainingColors) {
		return new ColouredBubble(getRandomRemainingColor(remainingColors));
	}
	
	protected Color getRandomRemainingColor(Set<Color> remainingColors) {
		int index = random.nextInt(remainingColors.size());
		Iterator<Color> iterator = remainingColors.iterator();
		for(int i = 0; i < index; i++) iterator.next();
		return iterator.next();
	}

}
