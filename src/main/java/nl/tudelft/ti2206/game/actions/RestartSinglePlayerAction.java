package nl.tudelft.ti2206.game.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import nl.tudelft.ti2206.cannon.MouseCannonController;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestartSinglePlayerAction extends AbstractAction {

	private static final Logger log = LoggerFactory.getLogger(RestartSinglePlayerAction.class);
	
	private static final long serialVersionUID = -7718661789308082267L;
	
	private final SinglePlayerFrame singlePlayerFrame;
	
	public RestartSinglePlayerAction(SinglePlayerFrame singlePlayerFrame) {
		super();
		this.singlePlayerFrame = singlePlayerFrame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
			new ChooseGameMode(singlePlayerFrame, gameMode -> {
				
				try {
					GameModel gameModel = new GameModel(gameMode);
					MouseCannonController masterCannonController = new MouseCannonController();
					GameController gameController = new GameController(gameModel, masterCannonController);
					SinglePlayerFrame frame = new SinglePlayerFrame(masterCannonController, gameController);
					
					frame.pack();
					frame.setLocationRelativeTo(this.singlePlayerFrame);
					frame.setSound(singlePlayerFrame.hasSound());
					this.singlePlayerFrame.dispose();
					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.getFindMultiplayerAction().setEnabled(true);
				}
				catch (IOException e) {
					log.error(e.getMessage(), e);
				}
				
			});
	}
	
}
