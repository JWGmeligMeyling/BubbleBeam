package nl.tudelft.ti2206.highscore;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import nl.tudelft.ti2206.game.backend.mode.ClassicGameMode;

import org.junit.Test;

import com.google.common.collect.ImmutableSortedSet;

public class HighscoreTest {

	
	@Test
	public void testAdd() throws IOException{
		File file = File.createTempFile("doesnt", "matter");
		HighscoreStore highscore = new HighscoreStoreImpl(file);
		highscore.clear();
		
		HighscoreRecord item1 = new HighscoreRecord("Michael", new Score(1200, 0, 0));
		HighscoreRecord item2 = new HighscoreRecord("Pam", new Score(1300, 20, 20));
		HighscoreRecord item3 = new HighscoreRecord("Dwight", new Score(1250, 20, 20));
		
		highscore.addScore(ClassicGameMode.class, item1);
		highscore.addScore(ClassicGameMode.class, item2);
		highscore.addScore(ClassicGameMode.class, item3);
		
		assertEquals(ImmutableSortedSet.of(item1, item3, item2),
				highscore.getScoresForGameMode(ClassicGameMode.class));
	}
	
	@Test
	public void writeAndRead() throws IOException{
		File file = File.createTempFile("doesnt", "matter");
		HighscoreStore highscore = new HighscoreStoreImpl(file);
		highscore.clear();
		
		HighscoreRecord item1 = new HighscoreRecord("Michael", new Score(1200, 0, 0));
		HighscoreRecord item2 = new HighscoreRecord("Pam", new Score(1300, 20, 20));
		HighscoreRecord item3 = new HighscoreRecord("Dwight", new Score(1250, 20, 20));
		
		highscore.addScore(ClassicGameMode.class, item1);
		highscore.addScore(ClassicGameMode.class, item2);
		highscore.addScore(ClassicGameMode.class, item3);
		
		highscore.save();
		
		HighscoreStore other = new HighscoreStoreImpl(file);
		other.read();
		
		assertEquals(highscore, other);
	}
}
