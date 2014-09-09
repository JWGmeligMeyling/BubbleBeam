package nl.tudelft.ti2206.throwaway;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import nl.tudelft.ti2206.game.MouseListener;

public class GuiThrowAway extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3473878919673841174L;
	
	public static GuiThrowAway instance;
	
	private final GuiThrowAwayPanel GUI;
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1); 
	
	public GuiThrowAway() {
		super("Bubble Shooter");
		GUI = new GuiThrowAwayPanel();
		
		this.add(GUI);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		this.addMouseListener(MouseListener.INSTANCE);
		this.setLocationRelativeTo(null);
		
		run();
	}
	
	public long time = System.currentTimeMillis();

	private void run() {
		executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				time = System.currentTimeMillis();
			}
			
		}, 0, 33, TimeUnit.MILLISECONDS);
	}
	
}
