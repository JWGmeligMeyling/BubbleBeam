package nl.tudelft.ti2206.highscore;

import java.io.File;
import java.io.Serializable;

import com.google.common.collect.ImmutableSortedSet;
import com.google.inject.ImplementedBy;

import nl.tudelft.ti2206.game.backend.mode.GameMode;

/**
 * Base interface for Highscore implementation
 * 
 * @author Leon Hoek
 *
 */
@ImplementedBy(HighscoreStoreImpl.class)
public interface HighscoreStore extends Serializable {
	
	/**
	 * @return Get the {@link File} in which the {@code Highscores} are saved
	 */
	File getFile();
	
	/**
	 * Get the highscores for a specific {@link GameMode}. An immutable copy of
	 * the set is returned, since new {@code ScoreItems} should be inserted through
	 * {@link #addScore(Class, HighscoreRecord)}.
	 * 
	 * @param gameMode
	 *            {@code GameMode} for which to seek {@code Highscores}
	 * @return an {@link ImmutableSortedSet} of {@link Score ScoreItems}
	 */
	ImmutableSortedSet<HighscoreRecord> getScoresForGameMode(Class<? extends GameMode> gameMode);
	
	/**
	 * Add a Highscore to the 
	 * @param gameMode {@link GameMode} for the {@code HighscoreItem}
	 * @param scoreItem {@link HighscoreRecord} to be stored
	 */
	void addScore(Class<? extends GameMode> gameMode, HighscoreRecord scoreItem);
	
	/**
	 * Clear the highscores
	 */
	void clear();
	
	/**
	 * Save the highscores to a file
	 */
	void save();
	
	/**
	 * Read the highscores from file
	 */
	void read();
	
	/**
	 * @return the highscores
	 */
	static HighscoreStore getHighscores() {
		HighscoreStore highscore = new HighscoreStoreImpl();
		highscore.read();
		return highscore;
	}
	
}
