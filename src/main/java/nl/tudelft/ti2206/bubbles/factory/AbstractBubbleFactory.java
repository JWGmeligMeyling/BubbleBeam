package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;

public abstract class AbstractBubbleFactory implements BubbleFactory {
	
	protected final Random random = new Random();

	protected Bubble createColouredBubble(Set<Color> remainingColors) {
		return new ColouredBubble(getRandomRemainingColor(remainingColors));
	}
	
	protected Color getRandomRemainingColor(Set<Color> remainingColors) {
		int index = random.nextInt(remainingColors.size());
		Iterator<Color> iterator = remainingColors.iterator();
		for(int i = 0; i < index; i++) iterator.next();
		assert iterator.hasNext() : "The iterator should have one more item";
		return iterator.next();
	}

}
