package nl.tudelft.ti2206.exception;

public class GameOver extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1133998443308001411L;
	
	protected final boolean win;
	
	public GameOver() {
		this(false);
	}
	
	public GameOver(boolean win) {
		this.win = win;
	}
	
	public boolean isWin() {
		return win;
	}

}
