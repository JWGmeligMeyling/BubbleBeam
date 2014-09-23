package nl.tudelft.ti2206.game.controller;

import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.game.Animation;
import nl.tudelft.util.Vector2f;

public interface GameController {
	
	Animation<ColouredBubble> shoot(final Vector2f direction);
	
	

}
