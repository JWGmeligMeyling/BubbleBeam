package nl.tudelft.ti2206.game.backend.mode;

import javax.inject.Inject;

import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.game.backend.GameController;

/**
 * In the {@code PowerupGameMode}, the {@link PowerUpBubbleFactory} is used to
 * create new {@code Bubbles}. It uses both {@link ColouredBubble
 * ColouredBubbles} and power-up bubbles, some with the Drunk Bubble effect.
 * After 5 misses, a new row is inserted on top.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@ModeName("Power-up Mode")
@ModeBubbleFactory(PowerUpBubbleFactory.class)
@ModeMusic("/power_music.wav")
@ModeMaps({"/smallboard.txt", "/spekboard.txt"})
public class PowerupGameMode extends ClassicGameMode {

	private static final long serialVersionUID = -2215681432174695925L;
	
	/**
	 * In the {@code PowerupGameMode}, the {@link PowerUpBubbleFactory} is used
	 * to create new {@code Bubbles}. It uses both {@link ColouredBubble
	 * ColouredBubbles} and power-up bubbles, some with the Drunk Bubble effect.
	 * After 5 misses, a new row is inserted on top.
	 * 
	 * @param gameController
	 *            {@link GameController} for this {@code GameMode}
	 */
	@Inject
	public PowerupGameMode(GameController gameController) {
		super(gameController);
	}

}
