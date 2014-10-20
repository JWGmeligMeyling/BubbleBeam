package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleRight;

public class DrunkBubbleFactory extends AbstractBubbleFactory {

	private static final Logger log = LoggerFactory.getLogger(DrunkBubbleFactory.class);
	
	protected final Class<?> DrunkBubbles[] = { DrunkBubbleRight.class,
			DrunkBubbleLeft.class };
	
	@Override
	public Bubble createBubble(Set<Color> remainingColors) {
		Bubble out = createColouredBubble(remainingColors);
		final int index = random.nextInt(DrunkBubbles.length);
		
		try {
			Class<?> clasz = DrunkBubbles[index];
			return Bubble.class.cast(clasz.getConstructor(Bubble.class).newInstance(out));
		}
		catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			log.warn("Failed to instantate extra effect, falling back to no effect", e);
			return out;
		}
	}
	
}
