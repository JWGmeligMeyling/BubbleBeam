package nl.tudelft.ti2206.bubbles.factory;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.BombBubble;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleRight;
import nl.tudelft.ti2206.bubbles.decorators.JokerBubble;
import nl.tudelft.ti2206.bubbles.decorators.StoneBubble;

public class PowerUpBubbleFactory extends BubbleFactory{
	
	protected final Random RANDOM_GENERATOR = new Random();
	
	private int chanceOfPowerup = 3;	// 1-10. 1 is one in ten chance, 10 is all bubbles are powerups
	private int chanceOfExtraEffect = 2;	// 1-10. 1 is one in ten chance, 10 is all bubbles have extra effects
	
	protected final Class<?> primaryEffects[] = {
			JokerBubble.class, BombBubble.class, StoneBubble.class };
	protected final Class<?> extraEffects[] = { DrunkBubbleRight.class,
			DrunkBubbleLeft.class };
	
	
	@Override
	public Bubble createBubble(Set<Color> remainingColors) {
		Bubble out;
		//determine if the next bubble is going to be a Decorated or Coloured Bubble
		int number = RANDOM_GENERATOR.nextInt(10);
		int effectIndex = RANDOM_GENERATOR.nextInt(primaryEffects.length);
		if(number < chanceOfPowerup){
			out = addPrimaryEffect(effectIndex);
		} else {
			out = createColouredBubble(remainingColors);
		}
		
		//add a secondary extra effect
		int extraIndex = RANDOM_GENERATOR.nextInt(extraEffects.length);  
		number = RANDOM_GENERATOR.nextInt(10);
		if(number < chanceOfExtraEffect){
			out = addExtraEffect(extraIndex,out);
		}
		return out;
		
		
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
	
	protected Bubble addExtraEffect(int index, Bubble bubble) {
		try {
			Class<?> clasz = extraEffects[index];
			return Bubble.class.cast(clasz.getConstructor(Bubble.class).newInstance(bubble));
		} catch (Throwable t) {
			return bubble;
		}
	}
	
	protected Bubble addPrimaryEffect(int index) {
		Class<?> clasz = primaryEffects[index];
		try {
			return Bubble.class.cast(clasz.newInstance());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
