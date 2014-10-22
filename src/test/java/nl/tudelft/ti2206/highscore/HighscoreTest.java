package nl.tudelft.ti2206.highscore;

import static org.junit.Assert.*;

import nl.tudelft.ti2206.game.backend.mode.ClassicGameMode;

import org.junit.Test;

import com.google.common.collect.ImmutableSortedSet;

public class HighscoreTest {

	
	@Test
	public void testAdd(){
		Highscore highscore = new HighscoreImpl();
		highscore.clear();
		
		HighscoreItem item1 = new HighscoreItem("Michael", new ScoreItem(1200, 0, 0));
		HighscoreItem item2 = new HighscoreItem("Pam", new ScoreItem(1300, 20, 20));
		HighscoreItem item3 = new HighscoreItem("Dwight", new ScoreItem(1250, 20, 20));
		
		highscore.addScore(ClassicGameMode.class, item1);
		highscore.addScore(ClassicGameMode.class, item2);
		highscore.addScore(ClassicGameMode.class, item3);
		
		assertEquals(ImmutableSortedSet.of(item1, item3, item2),
				highscore.getScoresForGameMode(ClassicGameMode.class));
	}
	
	@Test
	public void writeAndRead(){
		
		Highscore highscore = new HighscoreImpl();
		highscore.clear();
		
		HighscoreItem item1 = new HighscoreItem("Michael", new ScoreItem(1200, 0, 0));
		HighscoreItem item2 = new HighscoreItem("Pam", new ScoreItem(1300, 20, 20));
		HighscoreItem item3 = new HighscoreItem("Dwight", new ScoreItem(1250, 20, 20));
		
		highscore.addScore(ClassicGameMode.class, item1);
		highscore.addScore(ClassicGameMode.class, item2);
		highscore.addScore(ClassicGameMode.class, item3);
		
		highscore.save();
		
		Highscore other = new HighscoreImpl();
		other.read();
		
		assertEquals(highscore, other);
	}
}
