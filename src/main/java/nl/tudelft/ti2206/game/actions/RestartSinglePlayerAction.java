package nl.tudelft.ti2206.game.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.game.backend.GameMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestartSinglePlayerAction extends AbstractAction {

	private static final Logger log = LoggerFactory
			.getLogger(RestartSinglePlayerAction.class);
	private static final long serialVersionUID = -7718661789308082267L;
	
	private final SinglePlayerFrame singlePlayerFrame;
	
	public RestartSinglePlayerAction(SinglePlayerFrame singlePlayerFrame) {
		super("Restart Single-Player");
		this.singlePlayerFrame = singlePlayerFrame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
			final JDialog dialog = new JDialog(singlePlayerFrame, false);	//this makes the dialog always stay on top of the singleplayerframe
			dialog.setTitle("Choose singleplayer gamemode");
			
			//adding buttons for all gamemodes to the dialog
			
			GameMode[] gameModes = GameMode.values();
			dialog.setLayout(new GridLayout(1, gameModes.length));
			
			for(GameMode gameMode : gameModes) {
				String label = gameMode.getName();
				JButton button = new JButton(label);
				
				button.addActionListener((a) -> {
					start(gameMode);
				});
				
				dialog.add(button);
			}
			
			//make it so that the dialog is displayed as it should be
			dialog.setVisible(true);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			//add that when on the textfield 'enter' is clicked or the dialogbox is closed to add to the highscore
			
	}
	
	public void start(GameMode gameMode){
		try {
			SinglePlayerFrame frame = new SinglePlayerFrame(gameMode);
			frame.pack();
			frame.setLocationRelativeTo(this.singlePlayerFrame);
			this.singlePlayerFrame.dispose();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.getFindMultiplayerAction().setEnabled(true);
		}
		catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
