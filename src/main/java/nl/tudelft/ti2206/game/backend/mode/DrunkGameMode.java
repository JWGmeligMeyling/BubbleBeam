package nl.tudelft.ti2206.game.backend.mode;

import javax.inject.Inject;

import nl.tudelft.ti2206.bubbles.factory.DrunkBubbleFactory;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.highscore.DrunkHighscore;

@ModeName("Drunk Mode")
@ModeHighscore(DrunkHighscore.class)
@ModeBubbleFactory(DrunkBubbleFactory.class)
public class DrunkGameMode extends PowerupGameMode {

	private static final long serialVersionUID = -3111557227462971615L;
	
	@Inject
	public DrunkGameMode(GameController gameController) {
		super(gameController);
	}

}
