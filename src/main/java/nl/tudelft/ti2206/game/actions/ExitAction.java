package nl.tudelft.ti2206.game.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.logger.Logger;
import nl.tudelft.ti2206.logger.LoggerFactory;

public class ExitAction extends AbstractAction {

	private static final long serialVersionUID = 6941454112469903520L;
	private static final Logger log = LoggerFactory.getLogger(ExitAction.class);
	
	private final SinglePlayerFrame singlePlayerFrame;

	public ExitAction(SinglePlayerFrame singlePlayerFrame) {
		super();
		this.singlePlayerFrame = singlePlayerFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("Closing the application");
		singlePlayerFrame.dispose();
	}

}
