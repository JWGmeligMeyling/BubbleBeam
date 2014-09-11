package nl.tudelft.ti2206.game;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.throwaway.GuiThrowAwayPanel;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

public class GUI {

	private JFrame frame;
	
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;
	
	
	public void fillGameFrame(Container pane){
		if(RIGHT_TO_LEFT){
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// everything the frame must be filled with
		JPanel gamePanel;
		try {
			BubbleMesh bubbleMesh = BubbleMesh.parse(new File("src/main/resources/board.txt"));
			gamePanel = new GuiThrowAwayPanel(bubbleMesh);
		} catch (Exception e) {
			throw new RuntimeException("User too stupid, {put a username here}", e);
		}
		
		gamePanel.setMinimumSize(gamePanel.getPreferredSize());
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.gridheight = 4;
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(gamePanel,c);
		
		
		JButton spaceForLogo = new JButton("Space for logo");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 0;
		c.ipadx = 30;
		pane.add(spaceForLogo, c);
		
		
		JButton score = new JButton("Score: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 1;
		c.ipadx = 30;
		pane.add(score, c);
		//JLabel 
		
		JButton menu = new JButton("Menu: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 2;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 2;
		c.gridy = 2;
		c.ipadx = 30;
		pane.add(menu, c);
		//JLabel 
		
	}

	public GUI(){
		frame = new JFrame("Bubble Shooter");
		//JPanel gamePanel = new GuiThrowAwayPanel();
		
		fillGameFrame(frame.getContentPane());
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		
		
	}
}
