package nl.tudelft.ti2206.game.backend.mode;

import java.io.Serializable;

import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.game.backend.Tickable;
import nl.tudelft.ti2206.game.event.GameListener;
import nl.tudelft.ti2206.highscore.Highscore;

/**
 * Base interface for the various {@code GameModes} in the game
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface GameMode extends GameListener, Tickable, Serializable {
	
	@Override default void gameOver(GameOverEvent event) {
		// No-op on default, but subtypes may rely on gameOver
	}
	
	@Override default void shotMissed(ShotMissedEvent event) {
		// No-op on default, but subtypes may rely on shotMissed, for example classic mode
	}
	
	@Override default void gameTick() {
		// No-op on default, but subtypes may rely on gametick, for example Timed mode
	}
	
	/**
	 * Get the name for this {@code GameMode}
	 * @return the name
	 */
	default String getModeName() {
		return this.getClass().getAnnotation(ModeName.class).value();
	}
	
	/**
	 * Get the {@link Highscore} instance for this {@code GameMode}
	 * @return the {@code Highscore}
	 */
	default Highscore getHighscore() {
		try {
			return this.getClass().getAnnotation(ModeHighscore.class).value()
					.newInstance();
		}
		catch (InstantiationException  | IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * Get the {@link BubbleFactory} instance for this {@code GameMode}
	 * @return a {@code BubbleFactory}
	 */
	default BubbleFactory getBubbleFactory() {
		try {
			return this.getClass().getAnnotation(ModeBubbleFactory.class)
					.value().newInstance();
		}
		catch (InstantiationException  | IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}
