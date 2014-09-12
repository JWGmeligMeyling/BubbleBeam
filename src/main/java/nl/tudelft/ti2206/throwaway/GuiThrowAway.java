package nl.tudelft.ti2206.throwaway;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.Timer;

import nl.tudelft.ti2206.bubbles.BubbleMesh;

public class GuiThrowAway extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3473878919673841174L;
	public static final int FPS = 30;
	protected static final int FRAME_PERIOD = 1000/FPS;
	
	private final GuiThrowAwayPanel GUI;
	
	public GuiThrowAway() {
		super("Bubble Shooter");
		
		try {
			BubbleMesh bubbleMesh = BubbleMesh.parse(new File("src/main/resources/board.txt"));
			GUI = new GuiThrowAwayPanel(bubbleMesh);
		} catch (Exception e) {
			throw new RuntimeException("User too stupid, {put a username here}", e);
		}
		
		this.add(GUI);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		this.setLocationRelativeTo(null);
		
		run();
	}
	
	private void run() {
		new Timer(FRAME_PERIOD, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.gameStep();
			}
			
		}).start();
	}
	
}
