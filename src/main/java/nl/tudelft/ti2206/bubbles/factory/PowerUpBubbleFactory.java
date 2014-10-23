package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.BombBubble;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleRight;
import nl.tudelft.ti2206.bubbles.decorators.JokerBubble;
import nl.tudelft.ti2206.bubbles.decorators.StoneBubble;
import nl.tudelft.ti2206.game.backend.mode.PowerupGameMode;

/**
 * The {@link PowerUpBubbleFactory} is a {@link BubbleFactory} that creates
 * {@link ColouredBubble} and Power-up bubbles. It's used in the
 * {@link PowerupGameMode}.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class PowerUpBubbleFactory extends AbstractBubbleFactory {
	
	private static final Logger log = LoggerFactory.getLogger(PowerUpBubbleFactory.class);
	
	protected int chanceOfPowerup = 3;	// 1-10. 1 is one in ten chance, 10 is all bubbles are powerups
	protected int chanceOfExtraEffect = 2;	// 1-10. 1 is one in ten chance, 10 is all bubbles have extra effects
	
	protected final Class<?> primaryEffects[] = {
			JokerBubble.class, BombBubble.class, StoneBubble.class };
	protected final Class<?> extraEffects[] = { DrunkBubbleRight.class,
			DrunkBubbleLeft.class };
	
	@Override
	public Bubble createBubble(Set<Color> remainingColors) {
		Bubble out;
		//determine if the next bubble is going to be a Decorated or Coloured Bubble
		int number = random.nextInt(10);
		int effectIndex = random.nextInt(primaryEffects.length);
		
		if(remainingColors.isEmpty() || number < chanceOfPowerup){
			out = addPrimaryEffect(effectIndex);
		}
		else {
			out = createColouredBubble(remainingColors);
		}
		
		//add a secondary extra effect
		int extraIndex = random.nextInt(extraEffects.length);  
		number = random.nextInt(10);
		
		if(number < chanceOfExtraEffect){
			out = addExtraEffect(extraIndex,out);
		}
		
		return out;
	}
	
	protected Bubble addExtraEffect(int index, Bubble bubble) {
		try {
			Class<?> clasz = extraEffects[index];
			return Bubble.class.cast(clasz.getConstructor(Bubble.class).newInstance(bubble));
		}
		catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			log.warn("Failed to instantate extra effect, falling back to no effect", e);
			return bubble;
		}
	}
	
	protected Bubble addPrimaryEffect(int index) {
		Class<?> clasz = primaryEffects[index];
		try {
			return Bubble.class.cast(clasz.newInstance());
		}
		catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | SecurityException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
