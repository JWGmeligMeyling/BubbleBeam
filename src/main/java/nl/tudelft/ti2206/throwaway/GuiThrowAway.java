package nl.tudelft.ti2206.throwaway;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiThrowAway extends JFrame {
	
	private static final Logger log = LoggerFactory.getLogger(GuiThrowAway.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3473878919673841174L;
	
	private final GuiThrowAwayPanel GUI;
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	
	public GuiThrowAway() {
		super("Bubble Shooter");
		GUI = new GuiThrowAwayPanel();
		
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
				// log.info("Game step at {}", time);
				GUI.gameStep();
			}
			
		}, 0, 33, TimeUnit.MILLISECONDS);
	}
	
}
