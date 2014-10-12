package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.BombBubble;
import nl.tudelft.ti2206.bubbles.decorators.DecoratedBubble;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleRight;
import nl.tudelft.ti2206.bubbles.decorators.JokerBubble;
import nl.tudelft.ti2206.bubbles.decorators.SoundBubble;
import nl.tudelft.ti2206.bubbles.decorators.StoneBubble;

public class DefaultBubbleFactory extends BubbleFactory{
	
	protected final Random RANDOM_GENERATOR = new Random();
	
	private int chanceOfPowerup = 1;	// 1-10. 1 is one in ten chance, 10 is all bubbles are powerups
	
	protected final String effects[] = { "joker", "bomb", "reversebomb", "stone", "drunkR",
			"drunkL" };
	
	// protected final String secondaryEffect[] = {};
	
	@Override
	public Bubble createBubble(Set<Color> remainingColors) {
		//determine if the next bubble is going to be a powerup
		int number = RANDOM_GENERATOR.nextInt(10);
		if(number < chanceOfPowerup){
			return createSpecialBubble();
		}
		// create new colouredBubble
		final int index = RANDOM_GENERATOR.nextInt(remainingColors.size());
		final Iterator<Color> iterator = remainingColors.iterator();
		for (int i = 0; i <= index; i++)
			if (i == index) {
				return new ColouredBubble(iterator.next());
			} else {
				iterator.next();
			}
		throw new IndexOutOfBoundsException();
	}
	
	protected Bubble createSpecialBubble() {
		int number = RANDOM_GENERATOR.nextInt(effects.length);
		DecoratedBubble bubble = addInnerEffect(number);		//adds the first effect to a bubble
		
		// add a second different effect
		number = (number + RANDOM_GENERATOR.nextInt(effects.length - 1)) % effects.length;
		bubble = addOuterEffect(number, bubble);
		
		return bubble;
	}
	
	protected DecoratedBubble addOuterEffect(int index, DecoratedBubble bubble) {
		String toAdd = effects[index];
		DecoratedBubble wrapperBubble;
		switch (toAdd) {
		case "joker": // Do nothing, don't add the joker
			wrapperBubble = bubble;
		break;
		case "bomb":
			wrapperBubble = new SoundBubble("bomb.wav", new BombBubble(bubble));
		break;
		// case "reversebomb": //wrapperBubble = new ReverseBombBubble(bubble);
		// break;
		case "stone":
			wrapperBubble = new StoneBubble(bubble);
		break;
		case "drunkL":
			wrapperBubble = new DrunkBubbleLeft(bubble);
		break;
		case "drunkR":
			wrapperBubble = new DrunkBubbleRight(bubble);
		break;
		// case "sound": wrapperBubble = new SoundBubble(bubble);
		// break;
		default:
			wrapperBubble = new SoundBubble("horn.wav", new JokerBubble());
		}
		return wrapperBubble;
	}
	
	protected DecoratedBubble addInnerEffect(int index) {
		String toAdd = effects[index];
		DecoratedBubble wrapperBubble;
		switch (toAdd) {
		case "joker":
			wrapperBubble = new JokerBubble();
		break;
		case "bomb":
			wrapperBubble = new SoundBubble("bomb.wav", new BombBubble());
		break;
		// case "reversebomb": wrapperBubble = new ReverseBombBubble();
		// break;
		case "stone":
			wrapperBubble = new StoneBubble();
		break;
		case "drunkL":
			wrapperBubble = new DrunkBubbleLeft();
		break;
		case "drunkR":
			wrapperBubble = new DrunkBubbleRight();
		break;
		// case "sound": wrapperBubble = new SoundBubble();
		// break;
		default:
			wrapperBubble = new SoundBubble("horn.wav", new JokerBubble());
		}
		return wrapperBubble;
	}
}
