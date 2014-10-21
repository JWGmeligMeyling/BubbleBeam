package nl.tudelft.ti2206.game.backend.mode;

import javax.inject.Inject;

import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.highscore.PowerupHighscore;

@ModeName("Power-up Mode")
@ModeHighscore(PowerupHighscore.class)
@ModeBubbleFactory(PowerUpBubbleFactory.class)
public class PowerupGameMode extends ClassicGameMode {

	private static final long serialVersionUID = -2215681432174695925L;
	
	@Inject
	public PowerupGameMode(GameController gameController) {
		super(gameController);
	}

}
