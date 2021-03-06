package nl.tudelft.ti2206.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.google.common.collect.ImmutableSortedSet;

import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.backend.mode.GameMode;
import nl.tudelft.ti2206.game.event.GameListener.*;
import nl.tudelft.ti2206.highscore.HighscoreStore;
import nl.tudelft.ti2206.highscore.HighscoreRecord;
import nl.tudelft.ti2206.highscore.Score;

public class GameOverHighscore implements GameOverEventListener {
	
	private final SinglePlayerFrame frame;
	private final GameController gameController;

	public GameOverHighscore(final SinglePlayerFrame frame, final GameController gameController){
		this.frame = frame;
		this.gameController = gameController;
	}
	
	@Override
	public void gameOver(GameOverEvent event) {
		//don't add scores for multiplayer
		if(frame instanceof MultiplayerFrame) {
			return;
		}
		
		GameModel gameModel = gameController.getModel();
		Class<? extends GameMode> gameMode = gameModel.getGameMode();
		
		HighscoreStore highscore = HighscoreStore.getHighscores();
		ImmutableSortedSet<HighscoreRecord> scores = highscore.getScoresForGameMode(gameMode);
		Score scoreItem = Score.createScoreItem(gameModel);
		
		//choose which highscore to add to depending on the factory
		//either the highscore-list is not yet full or the last highscore on the list is less high than the one to be entered
		if(scores.size() < 10 || scores.last().compareTo(scoreItem) > 0){
			
			final JDialog dialog = new JDialog(frame, false);
			dialog.setTitle("Enter your name");
			JTextField nameField = new JTextField("",30);
			nameField.setDocument(new JTextFieldLimit(20));
			dialog.add(nameField);
			dialog.setVisible(true);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			//add that when on the textfield 'enter' is clicked or the dialogbox is closed to add to the highscore
			
			dialog.addWindowListener(new WindowAdapter() {
				
				@Override
				public void windowClosing(WindowEvent e) {
					String name = nameField.getText();
					HighscoreRecord highscoreItem = new HighscoreRecord(name, scoreItem);
					highscore.addScore(gameModel.getGameMode(), highscoreItem);
					dialog.dispose();
					showHighscorePopup(highscore, gameMode);
				}
			});
			
			nameField.addActionListener((e) -> {
				String name = nameField.getText();
				HighscoreRecord highscoreItem = new HighscoreRecord(name, scoreItem);
				highscore.addScore(gameModel.getGameMode(), highscoreItem);
				dialog.dispose();
				showHighscorePopup(highscore, gameMode);
			});
			
		} else {
			showHighscorePopup(highscore, gameMode);
		}

	}
	
	protected void showHighscorePopup(HighscoreStore highscore, Class<? extends GameMode> gameMode) {
		HighscorePopup popup = new HighscorePopup(highscore, gameMode);
		popup.setModal(true);
		popup.pack();
		popup.setLocationRelativeTo(frame);
		popup.setVisible(true);
	}
	
	class JTextFieldLimit extends PlainDocument {
		  private static final long serialVersionUID = 7134805879654022590L;
		private int limit;
		  JTextFieldLimit(int limit) {
		    super();
		    this.limit = limit;
		  }

		  JTextFieldLimit(int limit, boolean upper) {
		    super();
		    this.limit = limit;
		  }

		  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		    if (str == null)
		      return;

		    if ((getLength() + str.length()) <= limit) {
		      super.insertString(offset, str, attr);
		    }
		  }
		}


}
