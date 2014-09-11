package nl.tudelft.ti2206.throwaway;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import nl.tudelft.ti2206.bubbles.BubbleMesh;

public class GuiThrowAway extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3473878919673841174L;
	public static final int FPS = 60;
	protected static final int FRAME_PERIOD = 1000/FPS;
	
	private final GuiThrowAwayPanel GUI;
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	
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
	
	public long time = System.currentTimeMillis();
	
	private void run() {
		executorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				time = System.currentTimeMillis();
				GUI.gameStep();
			}
			
		}, 0, FRAME_PERIOD, TimeUnit.MILLISECONDS);
	}
	
}
