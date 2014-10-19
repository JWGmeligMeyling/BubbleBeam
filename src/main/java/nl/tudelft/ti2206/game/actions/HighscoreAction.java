package nl.tudelft.ti2206.game.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.ClassicBubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.DrunkBubbleFactory;
import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.highscore.ClassicHighscore;
import nl.tudelft.ti2206.highscore.DrunkHighscore;
import nl.tudelft.ti2206.highscore.Highscore;
import nl.tudelft.ti2206.highscore.HighscorePopup;
import nl.tudelft.ti2206.highscore.PowerupHighscore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighscoreAction extends AbstractAction{

/**
	 * 
	 */
	private static final long serialVersionUID = 3592886509735315068L;

private static final Logger log = LoggerFactory.getLogger(ExitAction.class);
	
	private final SinglePlayerFrame singlePlayerFrame;

	public HighscoreAction(SinglePlayerFrame singlePlayerFrame) {
		super("Highscore");
		this.singlePlayerFrame = singlePlayerFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("Opening Highscore-popup");
		//ScheduledExecutorService executor = singlePlayerFrame.getScheduledExecutorService();
		//TODO open different type for different game-mode
		BubbleFactory fac = singlePlayerFrame.getController().getFactory();
		if(fac instanceof ClassicBubbleFactory){
			new HighscorePopup(new ClassicHighscore());
		} else if(fac instanceof DrunkBubbleFactory){
			new HighscorePopup(new DrunkHighscore());
		} else if(fac instanceof PowerUpBubbleFactory){
			new HighscorePopup(new PowerupHighscore());
		}
		
		
		//executor.submit(() -> {	
		//});
	}
}
