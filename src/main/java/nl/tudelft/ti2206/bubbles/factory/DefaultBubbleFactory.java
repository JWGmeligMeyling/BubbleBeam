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
import nl.tudelft.ti2206.bubbles.decorators.SoundBubble;
import nl.tudelft.ti2206.bubbles.decorators.StoneBubble;

public class DefaultBubbleFactory extends BubbleFactory{
	
	protected final Random RANDOM_GENERATOR = new Random();
	
	private int chanceOfPowerup = 1;	// 1-10. 1 is one in ten chance, 10 is all bubbles are powerups
	private int chanceOfExtraEffect = 2;	// 1-10. 1 is one in ten chance, 10 is all bubbles have extra effects
	
	
	protected final String primaryEffects[] = {"joker", "bomb", "stone"};
	protected final String extraEffects[] = {"drunkR","drunkL"};
	
	
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
		String toAdd = extraEffects[index];
		Bubble wrapperBubble;
		switch (toAdd) {
		case "drunkL":
			wrapperBubble = new DrunkBubbleLeft(bubble);
		break;
		case "drunkR":
			wrapperBubble = new DrunkBubbleRight(bubble);
		break;
		default:
			wrapperBubble = bubble;	//Add no extra effect
		}
		return wrapperBubble;
	}
	
	protected Bubble addPrimaryEffect(int index) {
		String toAdd = primaryEffects[index];
		Bubble wrapperBubble;
		switch (toAdd) {
		case "joker":
			wrapperBubble = new JokerBubble();
		break;
		case "bomb":
			wrapperBubble = new SoundBubble("bomb.wav", new BombBubble());
		break;
		case "stone":
			wrapperBubble = new StoneBubble();
		break;
		default:
			wrapperBubble = new SoundBubble("horn.wav", new JokerBubble());
		}
		return wrapperBubble;
	}
}
