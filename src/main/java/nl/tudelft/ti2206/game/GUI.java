package nl.tudelft.ti2206.game;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.throwaway.GuiThrowAwayPanel;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GUI {

	JFrame frame;
	GuiThrowAwayPanel gamePanel;

	// multiplayer on same machine
	GuiThrowAwayPanel player2Panel;
	final boolean multiplayer = true;
	

	public static final int FPS = 30;
	protected static final int FRAME_PERIOD = 1000/FPS;

	// Score-labels
	JLabel playerScore;
	JLabel player2Score;

	// Buttons

	// game-variables
	boolean game_is_running = true;
	public long time = System.currentTimeMillis();

	private final ScheduledExecutorService executorService = Executors
			.newScheduledThreadPool(1);

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
		pane.add(gamePanel, c);

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
		pane.add(spaceForLogo, c);

		playerScore = new JLabel("Score: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 1;
		c.ipadx = 30;
		c.ipady = 30;
		pane.add(playerScore, c);
		// JLabel

		JButton restart = new JButton("Restart");
		
		restart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				restart();
			}
			
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 2;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 2;
		c.gridy = 2;
		c.ipadx = 30;
		pane.add(restart, c);
		// JLabel

		// multiplayer
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
			pane.add(player2Panel, c);

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

	protected void exit() {

	}

	protected void restart() {
		// send message to both gamePanel and player2Panel for reset
		// update scoreboards
	}

	private void run() {
		executorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				time = System.currentTimeMillis();
				gamePanel.gameStep();
				if (multiplayer) {
					player2Panel.gameStep();
				}
			}
			
		}, 0, FRAME_PERIOD, TimeUnit.MILLISECONDS);
	}
	
}
