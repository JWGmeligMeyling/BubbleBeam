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

public class Highscore {
	
	private static final Logger log = LoggerFactory.getLogger(Highscore.class);
	
	protected ArrayList<ScoreItem> scores;
	private final int LIST_LENGTH;
	
	protected String highscoreTitle = "Top 10 Single-Player";
	
	protected String scoreFile = "highscores";
	
	public Highscore(final int length) {
		LIST_LENGTH = length;
		scores = new ArrayList<ScoreItem>(LIST_LENGTH + 1);
		updateScores();
	}
	
	public String getTitle(){
		return highscoreTitle;
	}
	
	public void deleteHighscores(){
		scores.removeAll(scores);
		File folder = new File(System.getProperty("user.home"), "BubbleBeam");
		File highscores = new File(folder, "scoreFile");
		highscores.delete();
	}
	
	public void addScore(final ScoreItem item){
		if(!scores.contains(item)){
			scores.add(item);
		}
		sort();
		if(scores.size() == LIST_LENGTH + 1){
			scores.remove(LIST_LENGTH);	//remove the eleventh position so a new score can be added and sorted into it
		}
		
	}
	
	/**
	 * 
	 * @param index is from 1 to the size of the leaderboard (probably 10)
	 * @return the score at this position, and null if there is no score at this position
	 */
	public ScoreItem getPlace(final int index){
		try{
			return scores.get(index-1);
		} catch(IndexOutOfBoundsException e){
			return null;
		}finally{}
	}
	public void writeScoreFile(){
		try{
			File folder = new File(System.getProperty("user.home"), "BubbleBeam");
			folder.mkdirs();
			File highscores = new File(folder, "scoreFile");
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(highscores));
			output.writeObject(scores);
			
			output.close();
		} catch (FileNotFoundException e) {
			log.error("Highscore-file could not be made.");
		} catch (IOException e) {
			log.error("IOexception while writing high-scores.");
			e.printStackTrace();
		} finally{
			//do nothing
		}
	}
	
	protected void updateScores(){
		readScoreFile();
	}
	
	protected void sort(){
		ScoreCompare comparator = new ScoreCompare();
		Collections.sort(scores,comparator);
	}
	
	public void readScoreFile() {
		try{
			File folder = new File(System.getProperty("user.home"), "BubbleBeam");
			File highscores = new File(folder, "scoreFile");
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(highscores));
			@SuppressWarnings("unchecked")
			ArrayList<ScoreItem> temp = (ArrayList<ScoreItem>) input.readObject();
			for(ScoreItem item: temp){
				addScore(item);
			}
			input.close();
		} catch(FileNotFoundException e){
			log.info("No highscore-file found.");
		} catch (IOException e) {
			log.error("IOexception while reading high-scores. Continuing without previous highscores.");
		} catch (ClassNotFoundException e) {
			log.error("Class ScoreItem is not found. Continuing without previous highscores");
		} finally
		{
			//Do nothing i guess
		}
	}
}
