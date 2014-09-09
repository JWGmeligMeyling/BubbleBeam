package nl.tudelft.ti2206.throwaway;

import javax.swing.JFrame;

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
		GuiThrowAwayPanel GUI = new GuiThrowAwayPanel();
		this.add(GUI);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.addMouseListener(MouseListener.INSTANCE);
		this.setLocationRelativeTo(null);
	}
	
}
