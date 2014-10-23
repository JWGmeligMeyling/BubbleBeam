package nl.tudelft.ti2206.highscore;

public class HighscoreItem extends ScoreItem {

	private static final long serialVersionUID = -4413305568318964035L;

	protected final String name;
	
	public HighscoreItem(String name, ScoreItem scoreItem) {
		super(scoreItem.score, scoreItem.start, scoreItem.end);
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public String getName() {
		return name;
	}

}
