package nl.tudelft.ti2206.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JTextField;

import com.google.common.collect.ImmutableSortedSet;

import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.backend.mode.GameMode;
import nl.tudelft.ti2206.game.event.GameListener.*;
import nl.tudelft.ti2206.highscore.Highscore;
import nl.tudelft.ti2206.highscore.HighscoreItem;
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
	public void gameOver(GameOverEvent event) {
		GameModel gameModel = gameController.getModel();
		Class<? extends GameMode> gameMode = gameModel.getGameMode();
		gameModel.setGameOver(true);
		
		if(frame instanceof MultiplayerFrame){		//don't add scores for multiplayer
			return;
		}
		
		Highscore highscore = Highscore.getHighscores();
		ImmutableSortedSet<HighscoreItem> scores = highscore.getScoresForGameMode(gameModel.getGameMode());
		ScoreItem scoreItem = ScoreItem.createScoreItem(gameModel);
		
		//choose which highscore to add to depending on the factory
		//either the highscore-list is not yet full or the last highscore on the list is less high than the one to be entered
		if(scores.isEmpty() || scores.last().compareTo(scoreItem) > 0){
			
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
					HighscoreItem highscoreItem = new HighscoreItem(name, scoreItem);
					highscore.addScore(gameModel.getGameMode(), highscoreItem);
					dialog.dispose();
					showHighscorePopup(highscore, gameMode);
				}
			});
			
			nameField.addActionListener((e) -> {
				String name = nameField.getText();
				HighscoreItem highscoreItem = new HighscoreItem(name, scoreItem);
				highscore.addScore(gameModel.getGameMode(), highscoreItem);
				dialog.dispose();
				showHighscorePopup(highscore, gameMode);
			});
			
		} else {
			showHighscorePopup(highscore, gameMode);
		}

	}
	
	protected void showHighscorePopup(Highscore highscore, Class<? extends GameMode> gameMode) {
		HighscorePopup popup = new HighscorePopup(highscore, gameMode);
		popup.setModal(true);
		popup.pack();
		popup.setLocationRelativeTo(frame);
		popup.setVisible(true);
	}

}
