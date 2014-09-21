package nl.tudelft.ti2206.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.InputStream;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.cannon.Cannon;

public class NonReactiveGamePanel extends GamePanel {

	private final Cannon cannon;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3528849965271118208L;
	
	InputStream input;

	public NonReactiveGamePanel(final BubbleMesh bubbleMesh){
		super(bubbleMesh);
		this.cannon = new Cannon(bubbleMesh, new Point(WIDTH / 2, 400),
				this.getPreferredSize(), this.getLocation());
		this.cannon.bindMouseListenerTo(this);
	}
	
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		
		//add a grey overlay
		graphics.setColor(new Color(120,120,120,70)); // where a <1.0
		graphics.fillRect(0,0,this.getWidth(),this.getHeight());
	}	

}
