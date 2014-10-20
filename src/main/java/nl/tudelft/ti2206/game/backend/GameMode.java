package nl.tudelft.ti2206.game.backend;

import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.ClassicBubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.DrunkBubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.highscore.ClassicHighscore;
import nl.tudelft.ti2206.highscore.DrunkHighscore;
import nl.tudelft.ti2206.highscore.Highscore;
import nl.tudelft.ti2206.highscore.PowerupHighscore;

public enum GameMode {
	
	CLASSIC("Classic mode", new ClassicBubbleFactory(), new ClassicHighscore()),
	DRUNK("Drunk mode", new DrunkBubbleFactory(), new DrunkHighscore()),
	POWERUP("Power-up mode", new PowerUpBubbleFactory(), new PowerupHighscore()),
	TIMED("Timed mode", new PowerUpBubbleFactory(), new PowerupHighscore()) {

		private int ticks = 0;
		
		@Override
		public void gameTick(GameController gameController, GameModel gameModel) {
			if((ticks = ++ticks % 10) == 0) {
				gameModel.getBubbleMesh().translate(0, 1);
			}
		}
		
	};
	
	private final String name;
	private final BubbleFactory bubbleFactory;
	private final Highscore highscore;
	
	private GameMode(String name, BubbleFactory bubbleFactory, Highscore highscore) {
		this.name = name;
		this.bubbleFactory = bubbleFactory;
		this.highscore = highscore;
	}

	public Highscore getHighscore() {
		return highscore;
	}

	public BubbleFactory getBubbleFactory() {
		return bubbleFactory;
	}

	public void gameTick(GameController gameController, GameModel gameModel) {
		// Override this
	}

	public String getName() {
		return name;
	}
	
}
