package nl.tudelft.ti2206.game.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.game.HighscorePopup;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.highscore.HighscoreStore;

public class HighscoreAction extends AbstractAction {

	private static final long serialVersionUID = 3592886509735315068L;

	private static final Logger log = LoggerFactory.getLogger(ExitAction.class);
	
	private final SinglePlayerFrame singlePlayerFrame;

	public HighscoreAction(SinglePlayerFrame singlePlayerFrame) {
		super();
		this.singlePlayerFrame = singlePlayerFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("Opening Highscore-popup");

		HighscorePopup popup = new HighscorePopup(HighscoreStore.getHighscores(),
				singlePlayerFrame.getModel().getGameMode());
		
		popup.setModal(true);
		popup.pack();
		popup.setLocationRelativeTo(singlePlayerFrame);
		popup.setVisible(true);
		
	}
}
