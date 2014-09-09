package nl.tudelft.ti2206.throwaway;

import java.awt.Point;

import javax.swing.JFrame;

public class GuiThrowAway  extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3473878919673841174L;
	
	public GuiThrowAway(){
		super("Bubble Shooter");
		GuiThrowAwayPanel GUI = new GuiThrowAwayPanel();
		GUI.addBubble(new Bubble());
		Bubble b= new Bubble();
		b.setPosition(new Point(250,300));
		GUI.addBubble(b);
		this.add(GUI);
		this.pack();
		setVisible(true);
	}
	
}
