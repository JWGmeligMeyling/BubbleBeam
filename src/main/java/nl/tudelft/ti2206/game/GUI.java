package nl.tudelft.ti2206.game;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.throwaway.GuiThrowAwayPanel;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GUI {

	JFrame frame;
	GuiThrowAwayPanel gamePanel;

	// multiplayer on same machine
	GuiThrowAwayPanel player2Panel;
	boolean multiplayer = false;
	

	public static final int FPS = 60;
	protected static final int FRAME_PERIOD = 1000/FPS;

	// Score-labels
	JLabel playerScore;
	JLabel player2Score;

	// Buttons

	// game-variables
	boolean game_is_running = true;

	// gridbag constants
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

	protected void fillGameFrame(Container pane) throws FileNotFoundException, IOException {
		if (RIGHT_TO_LEFT) {
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		Insets extPadding = new Insets(10, 10, 10, 10);
		Insets noPadding = new Insets(0,0,0,0);
		
		// everything the frame must be filled with
		gamePanel = new GuiThrowAwayPanel(
				BubbleMesh.parse(new File("src/main/resources/board.txt")));

		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.gridheight = 4;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.insets = extPadding;
		pane.add(gamePanel,c);
		
		//score-label
		playerScore = new JLabel("Score: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = 4;
		c.ipadx = 30;
		c.ipady = 30;
		c.insets = extPadding;
		pane.add(playerScore, c);
		
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
		c.ipady = 30;
		c.insets = noPadding;
		pane.add(spaceForLogo, c);
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				GUI.this.exit();
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 2;
		c.gridy = 1;
		c.ipadx = 30;
		c.ipady = 30;
		c.insets = extPadding;
		pane.add(exit, c); 
		
		JButton singlePlayerRestart = new JButton("Restart Single-Player");
		singlePlayerRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				multiplayer = false;
				GUI.this.restart();
			}
			
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 2;
		c.gridy = 2;
		c.ipadx = 30;
		c.insets = extPadding;
		pane.add(singlePlayerRestart, c);
		
		JButton multiPlayerRestart = new JButton("Restart Multi-Player");
		multiPlayerRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				multiplayer = true;
				GUI.this.restart();
			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 2;
		c.gridy = 3;
		c.ipadx = 30;
		c.insets = extPadding;
		pane.add(multiPlayerRestart, c);

		// everything the frame must be filled with
		if (multiplayer) {
			player2Panel = new GuiThrowAwayPanel(
					BubbleMesh.parse(new File("src/main/resources/anotherboard")));

			c.fill = GridBagConstraints.NONE;
			c.weightx = 0;
			c.weighty = 0;
			c.gridheight = 4;
			c.gridwidth = 1;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 3;
			c.gridy = 0;
			c.ipadx = 0;
			c.ipady = 0;
			c.insets = extPadding;
			pane.add(player2Panel,c);
			
			//score-label
			player2Score = new JLabel("Score: ");
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0;
			c.weighty = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 3;
			c.gridy = 4;
			c.ipadx = 30;
			c.ipady = 30;
			c.insets = extPadding;
			pane.add(player2Score, c);
			
		}

	}

	public GUI() throws FileNotFoundException, IOException {
		frame = new JFrame("Bubble Shooter");

		fillGameFrame(frame.getContentPane());

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		run();
	}

	protected void exit(){
		//todo
	}

	protected void restart() {
		JPanel contentPane = (JPanel) frame.getContentPane();
		
		contentPane.removeAll();
		
		try {
			fillGameFrame(contentPane);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		contentPane.revalidate();
		contentPane.repaint();
		frame.pack();
		
		updateDisplayedScore();
		
	}
	
	/**
	 * Call this function when the score-attribute in GuiThrowAwayPanel has changed.
	 * This function will then display the new score on the screen.
	 */
	public void updateDisplayedScore(){
		playerScore.setText("Score: "+ gamePanel.getScore());
		if(multiplayer){
			
			player2Score.setText("Score: "+ player2Panel.getScore());
		}
		
	}
	protected void update(){
		gamePanel.gameStep();
		if(multiplayer){
			 player2Panel.gameStep();
		}
	}

	private void run() {
		new Timer(FRAME_PERIOD, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.gameStep();
				if (multiplayer) {
					player2Panel.gameStep();
				}
			}
			
		}).start();
	}
	
}
