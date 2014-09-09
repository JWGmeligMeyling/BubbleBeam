package nl.tudelft.ti2206.throwaway;

import javax.swing.JFrame;

public class GuiThrowAway  extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3473878919673841174L;
	
	public GuiThrowAway(){
		super("Bubble Shooter");
		GuiThrowAwayPanel GUI = new GuiThrowAwayPanel();
		this.add(GUI);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
}
