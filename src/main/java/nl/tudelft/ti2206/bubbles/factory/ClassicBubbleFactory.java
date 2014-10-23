package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Preconditions;

import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.game.backend.mode.ClassicGameMode;

/**
 * The {@link ClassicBubbleFactory} is a {@link BubbleFactory} that creates
 * {@link ColouredBubble ColouredBubbles} and is used by the
 * {@link ClassicGameMode}
 * 
 * @author Leon Hoek
 *
 */
public class ClassicBubbleFactory extends AbstractBubbleFactory {
	
	protected final Random RANDOM_GENERATOR = new Random();
	
	public ClassicBubbleFactory() {
		super();
	}

	public ClassicBubbleFactory(Random random) {
		super(random);
	}

	@Override
	public ColouredBubble createBubble(Set<Color> remainingColors) {
		Preconditions.checkArgument(!remainingColors.isEmpty(), "There should be a remaining color to create a new bubble");
		return createColouredBubble(remainingColors);
	}
}
