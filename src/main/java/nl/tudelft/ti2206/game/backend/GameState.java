package nl.tudelft.ti2206.game.backend;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;

/**
 * The {@link GameState} tells the state of a {@link GameModel}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public enum GameState {
	/**
	 * The player is still playing the game
	 */
	PLAYING,
	
	/**
	 * The player has won the game, the {@link BubbleMesh} is empty
	 */
	WIN,
	
	/**
	 * The player has lost the game, either the {@link Bubble Bubbles} have
	 * reached the bottom, or another player in the multiplayer has won the game
	 */
	LOSE;
}
