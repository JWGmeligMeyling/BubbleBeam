package nl.tudelft.ti2206.highscore;

import java.util.Date;

import org.junit.Test;

public class HighscoreTest {
	
	Highscore highscore;

	
	@Test
	public void testAdd(){
		highscore = new Highscore(3);
		ScoreItem item1 = new ScoreItem(1200,"Michael");
		ScoreItem item2 = new ScoreItem(1300,"Pam");
		
		highscore.addScore(item1);
		highscore.addScore(item2);
		ScoreItem test = new ScoreItem(1250,"Dwight");
		highscore.addScore(test);
		assert(highscore.getPlace(1).equals(item2));
		ScoreItem holly = new ScoreItem(1270,"Holly",new Date(12));
		highscore.addScore(holly);
		assert(highscore.getPlace(3).equals(test));
		assert(highscore.getPlace(4) == null);
		
		ScoreItem test2 = new ScoreItem(1300,"Pam");
		highscore.addScore(test2);
		assert(highscore.getPlace(1).equals(item2));
		assert(highscore.getPlace(2).equals(test2));
		assert(highscore.getPlace(3).equals(holly));
	}
	
	public void testSort(){
		
	}
}
