package nl.tudelft.ti2206.highscore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class Highscore {
	
	private static final Logger log = LoggerFactory.getLogger(Highscore.class);
	
	protected ArrayList<ScoreItem> scores;
	protected final int LIST_LENGTH = 10;
	
	protected static final String highscoreTitle = "Top 10 Single-Player";
	
	protected static final String scoreFile = "highscores";
	
	public Highscore() {
		scores = new ArrayList<ScoreItem>(LIST_LENGTH + 1);
		updateScores();
	}
	
	public int getSize(){
		return this.LIST_LENGTH;
	}
	
	abstract protected String getScoreFile();
	
	abstract public String getTitle();
	
	public void deleteHighscores(){
		scores.removeAll(scores);
		deleteHighscoresOnDisk();
	}
	
	private void deleteHighscoresOnDisk(){
		File folder = new File(System.getProperty("user.home"), "BubbleBeam");
		File highscores = new File(folder, getScoreFile());
		highscores.delete();
	}
	
	private void addScore(final ScoreItem item){
		if(!scores.contains(item)){
			scores.add(item);
		}
		sort();
		if(scores.size() == LIST_LENGTH + 1){
			scores.remove(LIST_LENGTH);	//remove the eleventh position so a new score can be added and sorted into it
		}
		
	}
	
	public void addNewScore(final ScoreItem item){
		this.addScore(item);
		this.deleteHighscoresOnDisk();
		this.writeScoreFile();
	}
	
	/**
	 * usage: getPlace(1) to return the highest score
	 * 
	 * @param index
	 *            is from 1 to the size of the leaderboard (probably 10)
	 * @return the score at this position, and null if there is no score at this
	 *         position
	 */
	public ScoreItem getPlace(final int index){
		try{
			return scores.get(index-1);
		} catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public void writeScoreFile(){
		File folder = new File(System.getProperty("user.home"), "BubbleBeam");
		folder.mkdirs();
		File highscores = new File(folder, getScoreFile());

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(highscores))) {
			output.writeObject(scores);
		}
		catch (FileNotFoundException e) {
			log.error("Highscore-file could not be made.", e);
		} catch (IOException e) {
			log.error("IOexception while writing high-scores.", e);
		}
	}
	
	protected void updateScores(){
		readScoreFile();
	}
	
	protected void sort(){
		Collections.sort(scores);
	}
	
	public void readScoreFile() {
		File folder = new File(System.getProperty("user.home"), "BubbleBeam");
		File highscores = new File(folder, getScoreFile());
		log.info("The file that is now loaded with the highscores is called: {}", getScoreFile());
		
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(highscores))) {
			
			@SuppressWarnings("unchecked")
			ArrayList<ScoreItem> temp = (ArrayList<ScoreItem>) input.readObject();
			
			for(ScoreItem item: temp){
				addScore(item);
			}
			
		}
		catch(FileNotFoundException e) {
			log.info("No highscore-file found.");
		}
		catch (IOException e) {
			log.error("IOexception while reading high-scores. Continuing without previous highscores.", e);
		}
		catch (ClassNotFoundException e) {
			log.error("Class ScoreItem is not found. Continuing without previous highscores", e);
		}
	}
}
