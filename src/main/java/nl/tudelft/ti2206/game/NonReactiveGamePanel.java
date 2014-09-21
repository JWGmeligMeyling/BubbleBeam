package nl.tudelft.ti2206.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.cannon.Cannon;

public class NonReactiveGamePanel extends GamePanel {

	private final Cannon cannon;
	
	private static final long serialVersionUID = 3528849965271118208L;
	
	public NonReactiveGamePanel(final BubbleMesh bubbleMesh){
		super(bubbleMesh);
		this.cannon = new Cannon(bubbleMesh, new Point(WIDTH / 2, 400),
				this.getPreferredSize(), this.getLocation());
		this.setBackground(new Color(225, 225, 225));
	}
	
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		cannon.render(graphics);
	}

	public void setCannonAngle(double angle) {
		cannon.setAngle(angle);
	}	

}
