package nl.tudelft.ti2206.game.backend;

import java.util.Random;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.decorators.BombBubble;
import nl.tudelft.ti2206.bubbles.decorators.DecoratedBubble;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleRight;
import nl.tudelft.ti2206.bubbles.decorators.JokerBubble;
import nl.tudelft.ti2206.bubbles.decorators.SoundBubble;
import nl.tudelft.ti2206.bubbles.decorators.StoneBubble;

public class BubbleFactory {
	
	protected final Random RANDOM_GENERATOR = new Random();
	
	protected final String effects[] = { "joker", "bomb", "reversebomb", "stone", "drunkR",
			"drunkL" };
	
	// protected final String secondaryEffect[] = {};
	
	protected Bubble createSpecialBubble() {
		int number = RANDOM_GENERATOR.nextInt(effects.length);
		DecoratedBubble bubble = addInnerEffect(number);
		
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
