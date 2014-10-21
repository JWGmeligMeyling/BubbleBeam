package nl.tudelft.ti2206.game.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.game.backend.GameModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestartSinglePlayerAction extends AbstractAction {

	private static final Logger log = LoggerFactory
			.getLogger(RestartSinglePlayerAction.class);
	private static final long serialVersionUID = -7718661789308082267L;
	protected static final String DEFAULT_BOARD_PATH = "/board.txt";
	
	private final SinglePlayerFrame singlePlayerFrame;
	
	public RestartSinglePlayerAction(SinglePlayerFrame singlePlayerFrame) {
		super("Restart Single-Player");
		this.singlePlayerFrame = singlePlayerFrame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
			new ChooseGameMode(singlePlayerFrame, gameMode -> {
				
				try {
					BubbleMesh bubbleMesh = BubbleMesh.parse(SinglePlayerFrame.class.getResourceAsStream(DEFAULT_BOARD_PATH));
					GameModel gameModel = new GameModel(gameMode, bubbleMesh);
					
					SinglePlayerFrame frame = new SinglePlayerFrame(gameModel);
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
				
			});
	}
	
}
