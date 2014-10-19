package nl.tudelft.ti2206.highscore;

public class ClassicHighscore extends Highscore {

	protected static final String highscoreTitle = "Top 10 classic mode";
	
	protected static final String drunkScoreFile = "classicHighscores";
	
	@Override
	public String getTitle(){
		return highscoreTitle;
	}
	@Override
	protected String getScoreFile(){
		return drunkScoreFile;
	}
	
}
