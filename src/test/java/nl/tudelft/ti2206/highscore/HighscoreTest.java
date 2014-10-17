package nl.tudelft.ti2206.highscore;

import java.util.Date;

import org.junit.Test;

public class HighscoreTest {

	
	@Test
	public void testAdd(){
		Highscore highscore = new Highscore(3);
		highscore.deleteHighscores();
		
		ScoreItem item1 = new ScoreItem(1200,"Michael");
		ScoreItem item2 = new ScoreItem(1300,"Pam",new Date(20));
		
		highscore.addScore(item1);
		highscore.addScore(item2);
		ScoreItem test = new ScoreItem(1250,"Dwight");
		highscore.addScore(test);
		assert(highscore.getPlace(1).equals(item2));
		ScoreItem holly = new ScoreItem(1270,"Holly",new Date(12));
		highscore.addScore(holly);
		assert(highscore.getPlace(3).equals(test));
		assert(highscore.getPlace(4) == null);
		
		ScoreItem test2 = new ScoreItem(1300,"Pam",new Date(40));
		highscore.addScore(test2);
		assert(highscore.getPlace(1).equals(item2));
		assert(highscore.getPlace(2).equals(test2));
		assert(highscore.getPlace(3).equals(holly));
		
		highscore.deleteHighscores();
	}
	
	@Test
	public void writeAndRead(){
		Highscore highscore = new Highscore(4);
		highscore.deleteHighscores();
		
		ScoreItem item1 = new ScoreItem(1200,"Michael",new Date(30));
		ScoreItem item2 = new ScoreItem(1200,"Angela",new Date(140));
		ScoreItem item3 = new ScoreItem(4300,"Tuna",new Date(240));
		ScoreItem item4 = new ScoreItem(100,"Kevin",new Date(300));
		
		highscore.addScore(item1);
		highscore.addScore(item2);
		highscore.addScore(item3);
		highscore.addScore(item4);
		
		highscore.writeScoreFile();
		
		Highscore newHighscore = new Highscore(5);
		assert(newHighscore.getPlace(1).equals(item3));
		assert(newHighscore.getPlace(2).equals(item1));
		assert(newHighscore.getPlace(3).equals(item2));
		assert(newHighscore.getPlace(4).equals(item4));
		assert(newHighscore.getPlace(5) == null);
		
		highscore.deleteHighscores();
	}
	
	
	
	public void testSort(){
		
	}
}
