package nl.tudelft.ti2206.game.backend.mode;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.io.Serializable;

import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
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
	 * 
	 * @return an array of {@link BubbleMesh} files for this {@link GameMode}
	 */
	default String[] getMaps() {
		return getClass().getAnnotation(ModeMaps.class).value();
	}

	/**
	 * @return the next {@link BubbleMesh} for this {@link GameMode}
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	BubbleMesh nextMap() throws IOException;
	
	/**
	 * @return true if the {@link GameMode} has another map
	 */
	boolean hasNextMap();
	
}
