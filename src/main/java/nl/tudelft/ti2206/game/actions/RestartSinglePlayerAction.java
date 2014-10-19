package nl.tudelft.ti2206.game.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import nl.tudelft.ti2206.game.SinglePlayerFrame;

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
			dialog.setLayout(new GridLayout(1,4));
			JButton classicButton = new JButton("Classic");
			dialog.add(classicButton);
			
			JButton drunkButton = new JButton("Drunk mode");
			dialog.add(drunkButton);
			
			JButton timedButton = new JButton("Timed mode");
			dialog.add(timedButton);
			
			JButton powerupButton = new JButton("Power-up mode");
			dialog.add(powerupButton);
			
			//create the universal actionlistener
			ActionListener al = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
					if(e.getSource().equals(timedButton)){
						startTimed();
					} else if(e.getSource().equals(drunkButton)){
						startDrunk();
					} else if(e.getSource().equals(classicButton)){
						startClassic();
					} else if(e.getSource().equals(powerupButton)){
						startPowerup();
					}
				}
			};
			
			//making the buttons functional
			classicButton.addActionListener(al);
			drunkButton.addActionListener(al);
			powerupButton.addActionListener(al);
			timedButton.addActionListener(al);
			
			//make it so that the dialog is displayed as it should be
			dialog.setVisible(true);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			//add that when on the textfield 'enter' is clicked or the dialogbox is closed to add to the highscore
			
	}
	
	public void startClassic(){
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
	
	public void startPowerup(){
		//do nothing for now
	}
	
	public void startDrunk(){
		//do nothing for now
	}
	
	public void startTimed(){
		//do nothing for now
	}

}
