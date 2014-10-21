package nl.tudelft.ti2206.game.backend.mode;

import java.io.Serializable;

import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.game.backend.Tickable;
import nl.tudelft.ti2206.highscore.Highscore;

/**
 * Base interface for the various {@code GameModes} in the game
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface GameMode extends Tickable, Serializable {
	
	/**
	 * Triggered when the player misses a shot
	 */
	default void missed() {
		// TODO Probably we want GameMode's to listen for GameEvents instead of hard coded hooking
	};
	
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
