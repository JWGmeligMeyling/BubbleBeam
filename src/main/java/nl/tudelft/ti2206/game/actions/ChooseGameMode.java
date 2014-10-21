package nl.tudelft.ti2206.game.actions;

import java.awt.Frame;
import java.awt.GridLayout;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;

import nl.tudelft.ti2206.game.backend.GameMode;

public class ChooseGameMode {

	public ChooseGameMode(final Frame owner, final Consumer<GameMode> callback) {
		final JDialog dialog = new JDialog(owner, false);	//this makes the dialog always stay on top of the singleplayerframe
		dialog.setTitle("Choose Game Mode");
		
		//adding buttons for all gamemodes to the dialog
		
		GameMode[] gameModes = GameMode.values();
		dialog.setLayout(new GridLayout(2, gameModes.length));
		
		for(GameMode gameMode : gameModes) {
			String label = gameMode.getName();
			JButton button = new JButton(label);
			
			button.addActionListener((a) -> {
				callback.accept(gameMode);
			});
			
			dialog.add(button);
		}
		
		//make it so that the dialog is displayed as it should be
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

}
