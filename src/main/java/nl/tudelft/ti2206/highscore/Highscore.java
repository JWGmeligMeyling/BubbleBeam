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
@ImplementedBy(HighscoreImpl.class)
public interface Highscore extends Serializable {
	
	/**
	 * @return Get the {@link File} in which the {@code Highscores} are saved
	 */
	File getFile();
	
	/**
	 * Get the highscores for a specific {@link GameMode}. An immutable copy of
	 * the set is returned, since new {@code ScoreItems} should be inserted through
	 * {@link Highscore#addScore(Class, ScoreItem)}.
	 * 
	 * @param gameMode
	 *            {@code GameMode} for which to seek {@code Highscores}
	 * @return an {@link ImmutableSortedSet} of {@link ScoreItem ScoreItems}
	 */
	ImmutableSortedSet<HighscoreItem> getScoresForGameMode(Class<? extends GameMode> gameMode);
	
	/**
	 * Add a Highscore to the 
	 * @param gameMode
	 * @param scoreItem
	 */
	void addScore(Class<? extends GameMode> gameMode, HighscoreItem scoreItem);
	
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
	 * Get the {@link Highscores}
	 * @return the highscores
	 */
	static Highscore getHighscores() {
		Highscore highscore = new HighscoreImpl();
		highscore.read();
		return highscore;
	}
	
}
