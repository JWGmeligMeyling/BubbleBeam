package nl.tudelft.ti2206.game.backend.mode;

import javax.inject.Inject;

import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.highscore.PowerupHighscore;

@ModeName("Timed Mode")
@ModeHighscore(PowerupHighscore.class)
@ModeBubbleFactory(PowerUpBubbleFactory.class)
public class TimedGameMode implements GameMode {

	private static final long serialVersionUID = 2150478663941653803L;	
	
	protected final static int TICK_AMOUNT = 30;
	
	protected final GameController gameController;
	protected final GameModel gameModel;

	protected int ticks = 0;

	@Inject
	public TimedGameMode(GameController gameController) {
		this.gameController = gameController;
		this.gameModel = gameController.getModel();
	}

	@Override
	public void gameTick() {
		if((ticks = ++ticks % TICK_AMOUNT) == 0) {
			gameModel.getBubbleMesh().translate(0, 1);
		}
	}
	
}
