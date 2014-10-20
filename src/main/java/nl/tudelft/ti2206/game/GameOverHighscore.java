package nl.tudelft.ti2206.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JTextField;

import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.backend.GameOverEventListener;
import nl.tudelft.ti2206.highscore.Highscore;
import nl.tudelft.ti2206.highscore.HighscorePopup;
import nl.tudelft.ti2206.highscore.ScoreItem;

public class GameOverHighscore implements GameOverEventListener {
	
	private final SinglePlayerFrame frame;
	private final GameController gameController;

	public GameOverHighscore(final SinglePlayerFrame frame, final GameController gameController){
		this.frame = frame;
		this.gameController = gameController;
	}
	
	@Override
	public void gameOver() {
		GameModel gameModel = gameController.getModel();
		gameModel.setGameOver(true);
		long score = gameModel.getScore();
		
		if(frame instanceof MultiplayerFrame){		//don't add scores for multiplayer
			return;
		}
		
		ScoreItem scoreEntry = new ScoreItem(score,"");
		
		//choose which highscore to add to depending on the factory
		Highscore hs = gameModel.getGameMode().getHighscore();
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
			
			nameField.addActionListener((event) -> {
				String name = nameField.getText();
				scoreEntry.setName(name);
				hs.addNewScore(scoreEntry);
				dialog.dispose();
				new HighscorePopup(hs);
			});
			
		} else {
			new HighscorePopup(hs);
		}

	}

}
