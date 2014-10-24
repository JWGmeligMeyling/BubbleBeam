package nl.tudelft.ti2206.game.backend.mode;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.Serializable;

import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.game.backend.Tickable;
import nl.tudelft.ti2206.game.event.GameListener;

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
	
	@Override default void ammo(AmmoLoadEvent event) {
		// No-op on default, but subtypes may rely on ammo
	}
	
	@Override default void score(ScoreEvent event) {
		// No-op on default, but subtypes may rely on score
	}
	
	/**
	 * Get the name for this {@code GameMode}
	 * @return the name
	 */
	default String getModeName() {
		return this.getClass().getAnnotation(ModeName.class).value();
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
	
	/**
	 * Get the music for this {@code GameMode}
	 * @return {@link AudioClip} music
	 */
	default AudioClip getMusic() {
		String path = this.getClass().getAnnotation(ModeMusic.class).value();
		return Applet.newAudioClip(GameMode.class.getResource(path));
	}
	
	/**
	 * @return the map paths for this {@link GameMode}
	 */
	static String[] getMapsFor(Class<? extends GameMode> klasz) {
		return klasz.getAnnotation(ModeMaps.class).value();
	}
	
	/**
	 * @param currentMap
	 *            the current map for the game
	 * @return the next map path
	 */
	static String getNextMap(Class<? extends GameMode> klasz, String currentMap) {
		String[] maps = getMapsFor(klasz);
		int index = 0;
		while(!maps[index++].equals(currentMap) && index < maps.length);
		index %= maps.length;
		return maps[index];
	}
	
}
