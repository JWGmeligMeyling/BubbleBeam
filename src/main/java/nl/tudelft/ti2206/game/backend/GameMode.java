package nl.tudelft.ti2206.game.backend;

import java.applet.Applet;
import java.applet.AudioClip;

import nl.tudelft.ti2206.bubbles.decorators.SoundBubble;
import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.ClassicBubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.DrunkBubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.highscore.ClassicHighscore;
import nl.tudelft.ti2206.highscore.DrunkHighscore;
import nl.tudelft.ti2206.highscore.Highscore;
import nl.tudelft.ti2206.highscore.PowerupHighscore;

public enum GameMode {
	
	
	
	CLASSIC("Classic mode", new ClassicBubbleFactory(), new ClassicHighscore(),"/classic_music.wav"),
	DRUNK("Drunk mode", new DrunkBubbleFactory(), new DrunkHighscore(),"/drunk_music.wav"),
	POWERUP("Power-up mode", new PowerUpBubbleFactory(), new PowerupHighscore(),"/power_music.wav"),
	TIMED("Timed mode", new PowerUpBubbleFactory(), new PowerupHighscore(),"/time_music.wav") {

		private int ticks = 0;
		
		
		@Override
		public void missed(GameController gameController, GameModel gameModel) {
			// do nothing, it's hard enough already
		}
		
		private final static  int TICK_AMOUNT = 30;
		
		@Override
		public void gameTick(GameController gameController, GameModel gameModel) {
			if((ticks = ++ticks % TICK_AMOUNT) == 0) {
				gameModel.getBubbleMesh().translate(0, 1);
			}
		}
		
	};
	
	private final static int MAX_MISSES = 5;

	private final String name;
	private final BubbleFactory bubbleFactory;
	private final Highscore highscore;
	private final AudioClip music;
	
	private GameMode(String name, BubbleFactory bubbleFactory, Highscore highscore,String music) {
		this.name = name;
		this.bubbleFactory = bubbleFactory;
		this.highscore = highscore;
		this.music = Applet.newAudioClip(GameMode.class.getResource(music));
	}

	public AudioClip getMusic(){
		return music;
	}
	
	public Highscore getHighscore() {
		return highscore;
	}

	public BubbleFactory getBubbleFactory() {
		return bubbleFactory;
	}
	
	public void missed(GameController gameController, GameModel gameModel) {
		int misses = gameModel.getMisses();
		if (++misses == MAX_MISSES) {
			misses = 0;
			gameController.insertRow();
		}
		gameModel.setMisses(misses);
	}

	public void gameTick(GameController gameController, GameModel gameModel) {
		// Override this
	}

	public String getName() {
		return name;
	}
	
}
