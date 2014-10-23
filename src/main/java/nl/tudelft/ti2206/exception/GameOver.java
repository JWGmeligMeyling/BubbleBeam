package nl.tudelft.ti2206.exception;

import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.mode.GameMode;

/**
 * The {@code GameOver} exception can be thrown in the {@link GameController}
 * and {@link GameMode} to end the game
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class GameOver extends RuntimeException {

	private static final long serialVersionUID = -1133998443308001411L;
	
	protected final boolean win;
	
	/**
	 * Construct a new {@code GameOver}
	 */
	public GameOver() {
		this(false);
	}
	
	/**
	 * Construct a new {@code GameOver}
	 * 
	 * @param win
	 *            iff the current game is win
	 */
	public GameOver(boolean win) {
		this.win = win;
	}
	
	/**
	 * Check if a player wins this game
	 * @return true iff the current game is win, or false for lose
	 */
	public boolean isWin() {
		return win;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameOver other = (GameOver) obj;
		if (win != other.win)
			return false;
		return true;
	}
	
}
