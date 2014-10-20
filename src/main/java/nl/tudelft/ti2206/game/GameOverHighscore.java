package nl.tudelft.ti2206.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.ClassicBubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.DrunkBubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.highscore.ClassicHighscore;
import nl.tudelft.ti2206.highscore.DrunkHighscore;
import nl.tudelft.ti2206.highscore.Highscore;
import nl.tudelft.ti2206.highscore.HighscorePopup;
import nl.tudelft.ti2206.highscore.PowerupHighscore;
import nl.tudelft.ti2206.highscore.ScoreItem;

public class GameOverHighscore implements GameOverEventListener {
	
	private static final Logger log = LoggerFactory.getLogger(GameOverHighscore.class);
	
	
	private final SinglePlayerFrame frame;

	public GameOverHighscore(final SinglePlayerFrame frame){
		this.frame = frame;
		
	}
	@Override
	public void gameOver() {
		frame.getController().getModel().setGameOver(true);
		
		if(frame instanceof MultiplayerFrame){		//don't add scores for multiplayer
			return;
		}
		
		long score = frame.getController().getModel().getScore();
		ScoreItem scoreEntry = new ScoreItem(score,"");
		
		//choose which highscore to add to depending on the factory
		Highscore hs;
		BubbleFactory bf = frame.getController().getFactory();
		if(bf instanceof ClassicBubbleFactory){
			hs = new ClassicHighscore();
		} else if(bf instanceof DrunkBubbleFactory){
			hs = new DrunkHighscore();
		} else if(bf instanceof PowerUpBubbleFactory){
			hs = new PowerupHighscore();
		} else{
			log.error("Unknown BubbleFactory, so no highscores available. Showing classic highscores");
			hs = new ClassicHighscore();
		}
		ScoreItem lastPlace = hs.getPlace(hs.getSize());
	
		//either the highscore-list is not yet full or the last highscore on the list is less high than the one to be entered
		if(lastPlace == null || (lastPlace != null && lastPlace.compareTo(scoreEntry) > 0)){
			final JDialog dialog = new JDialog(frame, false);
			dialog.setTitle("Enter your name");
			JTextField nameField = new JTextField("",30);
			dialog.add(nameField);
			dialog.setVisible(true);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			//add that when on the textfield 'enter' is clicked or the dialogbox is closed to add to the highscore
			dialog.addWindowListener(new WindowAdapter() {
				
				@Override
				public void windowClosing(WindowEvent e) {
					String name = nameField.getText();
					scoreEntry.setName(name);
					hs.addNewScore(scoreEntry);
					dialog.dispose();
					new HighscorePopup(hs);
				}
			});
			nameField.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String name = nameField.getText();
					scoreEntry.setName(name);
					hs.addNewScore(scoreEntry);
					dialog.dispose();
					new HighscorePopup(hs);
				}});
		} else{
			new HighscorePopup(hs);
		}

	}

}
