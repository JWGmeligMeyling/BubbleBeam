package nl.tudelft.ti2206.highscore;

import java.io.Serializable;

import nl.tudelft.ti2206.game.backend.GameModel;

/**
 * The {@code ScoreItem} contains a score and start and end timestamps
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class Score implements Comparable<Score>, Serializable {

	/**
	 * make it serializable for easy file storage
	 */
	private static final long serialVersionUID = 7652090502941376855L;
	
	protected final long score;
	protected final long start;
	protected final long end;
	
	/**
	 * Construct a new {@link Score}
	 * 
	 * @param score
	 *            Amount of points
	 * @param start
	 *            Current time millis when the game started
	 * @param end
	 *            Current time millis when the game ended
	 */
	public Score(final long score, final long start, final long end){
		this.score = score;
		this.start = start;
		this.end = end;
	}

	public long getScore() {
		return score;
	}

	public long getStart() {
		return start;
	}
	
	public long getEnd() {
		return end;
	}

	@Override
	public int compareTo(Score o) {
		int value = Long.signum(o.score - this.score);
		if(value == 0)
			value = Long.signum((o.end - o.start) - (this.end - this.start));
		if(value == 0)
			value = Long.signum(o.end - this.end);
		return value;
	}
	
	/**
	 * Create a new {@code ScoreItem} based on a {@link GameModel}
	 * 
	 * @param gameModel
	 *            {@code GameModel} for this {@code ScoreItem}
	 * @return new {@code ScoreItem}
	 */
	public static Score createScoreItem(GameModel gameModel) {
		return new Score(gameModel.getScore(), System.currentTimeMillis(), System.currentTimeMillis());
	}
	
}
