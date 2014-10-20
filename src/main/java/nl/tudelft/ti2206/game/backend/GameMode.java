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
	
	CLASSIC(new ClassicBubbleFactory(), new ClassicHighscore()),
	DRUNK(new DrunkBubbleFactory(), new DrunkHighscore()),
	POWERUP(new PowerUpBubbleFactory(), new PowerupHighscore());
	
	private final BubbleFactory bubbleFactory;
	private final Highscore highscore;
	
	private GameMode(BubbleFactory bubbleFactory, Highscore highscore) {
		this.bubbleFactory = bubbleFactory;
		this.highscore = highscore;
	}

	public Highscore getHighscore() {
		return highscore;
	}

	public BubbleFactory getBubbleFactory() {
		return bubbleFactory;
	}
	
}
