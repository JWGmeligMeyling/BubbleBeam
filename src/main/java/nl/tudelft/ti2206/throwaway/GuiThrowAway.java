package nl.tudelft.ti2206.throwaway;

import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import nl.tudelft.ti2206.game.Cannon;
import nl.tudelft.ti2206.game.MouseListener;

public class GuiThrowAway extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3473878919673841174L;
	public static GuiThrowAway instance;
	public boolean gameEnded = false;
	public GuiThrowAwayPanel GUI;
	
	public GuiThrowAway() {
		super("Bubble Shooter");
		instance = this;
		GUI = new GuiThrowAwayPanel();
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		addComponents();
		
		this.add(GUI);
		this.pack();
		setVisible(true);
		this.addMouseListener(MouseListener.INSTANCE);
		
		run();
	}
	
	private void addComponents() {
		GUI.addBubble(new Bubble());
		Bubble b = new Bubble();
		b.setPosition(new Point(250, 300));
		GUI.addBubble(b);
		
		Cannon cannon = new Cannon(200, 400);
		
		GUI.addCannon(cannon);
	}

	public void run() {
		int fps = 0;
		long time = System.currentTimeMillis();
		while (!gameEnded ) {
			GUI.gameStep();
			long curTime = System.currentTimeMillis();
			fps = (int) (1000/(curTime-time));
			time = curTime;
			try {
				Thread.sleep(33); // sleep 33 = 30 fps
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
