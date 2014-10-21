package nl.tudelft.ti2206.game.backend.mode;

import javax.inject.Inject;

import nl.tudelft.ti2206.bubbles.factory.ClassicBubbleFactory;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.highscore.ClassicHighscore;

@ModeName("Classic Mode")
@ModeHighscore(ClassicHighscore.class)
@ModeBubbleFactory(ClassicBubbleFactory.class)
public class ClassicGameMode implements GameMode {

	private static final long serialVersionUID = -5322766009361290612L;

	private final static int MAX_MISSES = 5;
	
	private int misses = 0;
	
	protected final GameController gameController;
	protected final GameModel gameModel;

	@Inject
	public ClassicGameMode(GameController gameController) {
		this.gameController = gameController;
		this.gameModel = gameController.getModel();
	}

	@Override
	public void missed() {
		if (++misses == MAX_MISSES) {
			misses = 0;
			gameController.insertRow();
		}
	}

}
