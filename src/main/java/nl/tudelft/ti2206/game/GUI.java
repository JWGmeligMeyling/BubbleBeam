package nl.tudelft.ti2206.game;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import nl.tudelft.ti2206.throwaway.GuiThrowAwayPanel;

public class GUI {
	
	JFrame frame;
	GuiThrowAwayPanel gamePanel;
	
	// multiplayer on same machine
	GuiThrowAwayPanel player2Panel;
	final boolean multiplayer = true;
	
	// Score-labels
	JLabel playerScore;
	JLabel player2Score;
	
<<<<<<< HEAD
	//Buttons
	
	
	//game-variables
	boolean game_is_running = true;
	public long time = System.currentTimeMillis();
	private static final Logger log = LoggerFactory.getLogger(GUI.class);
=======
	// game-variables
	boolean game_is_running = true;
	public long time = System.currentTimeMillis();
	// private static final Logger log = LoggerFactory.getLogger(GUI.class);
>>>>>>> aef0e8868e59a75c47b08a877ce0121afbd0f8b6
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	
	// gridbag constants
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;
	
	protected void fillGameFrame(Container pane) {
		if (RIGHT_TO_LEFT) {
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// everything the frame must be filled with
		gamePanel = new GuiThrowAwayPanel();
		gamePanel.setMinimumSize(gamePanel.getPreferredSize());
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.gridheight = 4;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = 0;
<<<<<<< HEAD
		c.ipadx = 0;
		c.ipady = 0;
		pane.add(gamePanel,c);
		
=======
		pane.add(gamePanel, c);
>>>>>>> aef0e8868e59a75c47b08a877ce0121afbd0f8b6
		
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
		
<<<<<<< HEAD
		
		playerScore = new JLabel("Score: ");
=======
		JButton score = new JButton("Score: ");
>>>>>>> aef0e8868e59a75c47b08a877ce0121afbd0f8b6
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 1;
		c.ipadx = 30;
<<<<<<< HEAD
		c.ipady = 30;
		pane.add(playerScore, c);
		//JLabel 
=======
		pane.add(score, c);
		// JLabel
>>>>>>> aef0e8868e59a75c47b08a877ce0121afbd0f8b6
		
		JButton restart = new JButton("Restart");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
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
<<<<<<< HEAD
		pane.add(restart, c);
		//JLabel 
=======
		pane.add(menu, c);
		// JLabel
>>>>>>> aef0e8868e59a75c47b08a877ce0121afbd0f8b6
		
		// multiplayer
		// everything the frame must be filled with
		if (multiplayer) {
			player2Panel = new GuiThrowAwayPanel();
			player2Panel.setMinimumSize(player2Panel.getPreferredSize());
			player2Panel.setMaximumSize(player2Panel.getPreferredSize());
			c.fill = GridBagConstraints.NONE;
			c.weightx = 0;
			c.weighty = 0;
			c.gridheight = 4;
			c.gridwidth = 1;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 3;
			c.gridy = 0;
<<<<<<< HEAD
			c.ipadx = 0;
			c.ipady = 0;
			pane.add(player2Panel,c);
=======
			pane.add(player2Panel, c);
>>>>>>> aef0e8868e59a75c47b08a877ce0121afbd0f8b6
			
		}
		
	}
	
	public GUI() {
		frame = new JFrame("Bubble Shooter");
		// JPanel gamePanel = new GuiThrowAwayPanel();
		
		fillGameFrame(frame.getContentPane());
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		run();
	}
	
<<<<<<< HEAD
	protected void exit(){
		
	}
	protected void restart(){
		//send message to both gamePanel and player2Panel for reset
		//update scoreboards
	}
	
	protected void update(){
=======
	protected void update() {
>>>>>>> aef0e8868e59a75c47b08a877ce0121afbd0f8b6
		gamePanel.gameStep();
		if (multiplayer) {
			player2Panel.gameStep();
		}
		
	}
	
	private void run() {
		executorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				time = System.currentTimeMillis();
				// log.info("Game step at {}", time);
				update();
			}
			
		}, 0, 33, TimeUnit.MILLISECONDS);
	}
}
