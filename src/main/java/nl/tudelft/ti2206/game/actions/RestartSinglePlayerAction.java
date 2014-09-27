package nl.tudelft.ti2206.game.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.game.SinglePlayerFrame;

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
		
		
		try {
			SinglePlayerFrame frame = new SinglePlayerFrame();
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
