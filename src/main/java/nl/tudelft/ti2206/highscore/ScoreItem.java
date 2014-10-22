package nl.tudelft.ti2206.highscore;

import java.io.Serializable;

import nl.tudelft.ti2206.game.backend.GameModel;

public class ScoreItem implements Comparable<ScoreItem>, Serializable {

	/**
	 * make it serializable for easy file storage
	 */
	private static final long serialVersionUID = 7652090502941376855L;
	
	protected final long score;
	protected final long start;
	protected final long end;

	public ScoreItem(final long score, final long start, final long end){
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
	public boolean equals(Object other) {
		if(other == this) return true;
		if (other instanceof ScoreItem) {
			ScoreItem that = (ScoreItem) other;
			if (this.score == that.score
					&& this.start == that.start
					&& this.end == that.end) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int compareTo(ScoreItem o) {
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
	 * @param name
	 *            Player name
	 * @return new {@code ScoreItem}
	 */
	public static ScoreItem createScoreItem(GameModel gameModel) {
		return new ScoreItem(gameModel.getScore(), gameModel.getStart(), System.currentTimeMillis());
	}
	
}
