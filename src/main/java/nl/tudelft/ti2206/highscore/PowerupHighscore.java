package nl.tudelft.ti2206.highscore;

public class PowerupHighscore extends Highscore {

	protected static final String highscoreTitle = "Top 10 Power-up mode";
	
	protected static final String drunkScoreFile = "powerupHighscores";
	
	@Override
	public String getTitle(){
		return highscoreTitle;
	}
	@Override
	protected String getScoreFile(){
		return drunkScoreFile;
	}
	
}
