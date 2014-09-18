package nl.tudelft.ti2206.game;

import java.awt.Graphics;

import nl.tudelft.ti2206.bubbles.BubbleMesh;

public class NonReactiveGamePanel extends GamePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3528849965271118208L;

	public NonReactiveGamePanel(final BubbleMesh bubbleMesh) {
		super(bubbleMesh);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		
		//add a grey overlay
		
	}

}
