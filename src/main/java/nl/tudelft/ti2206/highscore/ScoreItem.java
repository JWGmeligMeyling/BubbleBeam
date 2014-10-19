package nl.tudelft.ti2206.highscore;

import java.io.Serializable;
import java.util.Date;

public class ScoreItem implements Serializable {

	/**
	 * make it serializable for easy file storage
	 */
	private static final long serialVersionUID = 7652090502941376855L;
	private long score;
	private String name;
	private Date date;

	public ScoreItem(final long score, final String name) {
		this(score,name,new Date());
	}
	public ScoreItem(final long score, final String name, final Date date){
		setScore(score);
		setName(name);
		setDate(date);
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	private void setDate(Date date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ScoreItem) {
			ScoreItem that = (ScoreItem) other;
			if (this.getScore() == that.getScore()
					&& this.getDate().equals(that.getDate())
					&& this.getName().equals(that.getName())) {
				return true;
			}
		}
		return false;
	}
}
