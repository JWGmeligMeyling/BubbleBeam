package nl.tudelft.ti2206.game.backend.mode;

import javax.inject.Inject;

import nl.tudelft.ti2206.bubbles.factory.DrunkBubbleFactory;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.highscore.DrunkHighscore;

/**
 * In the {@code DrunkGameMode}, the {@link DrunkBubbleFactory} is used to
 * create new {@code Bubbles}. It uses both {@link ColouredBubbles} and power-up
 * bubbles, all with the Drunk Bubble effect. After 5 misses, a new row is
 * inserted on top.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@ModeName("Drunk Mode")
@ModeHighscore(DrunkHighscore.class)
@ModeBubbleFactory(DrunkBubbleFactory.class)
public class DrunkGameMode extends PowerupGameMode {

	private static final long serialVersionUID = -3111557227462971615L;
	
	/**
	 * In the {@code DrunkGameMode}, the {@link DrunkBubbleFactory} is used to
	 * create new {@code Bubbles}. It uses both {@link ColouredBubbles} and
	 * power-up bubbles, all with the Drunk Bubble effect. After 5 misses, a new
	 * row is inserted on top.
	 * 
	 * @param gameController
	 *            {@link GameController} for this {@code GameMode}
	 */
	@Inject
	public DrunkGameMode(GameController gameController) {
		super(gameController);
	}

}
