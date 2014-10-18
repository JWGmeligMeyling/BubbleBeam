package nl.tudelft.ti2206.highscore;

public class DrunkHighscore extends Highscore {

	protected static final String highscoreTitle = "Top 10 Drunkmode";
	
	protected static final String drunkScoreFile = "drunkHighscores";
	
	@Override
	public String getTitle(){
		return highscoreTitle;
	}
	@Override
	protected String getScoreFile(){
		return drunkScoreFile;
	}
	
}
