package nl.tudelft.ti2206.game;

import java.awt.Color;
import java.awt.Graphics;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.room.SlaveRoom;

public class MultiPlayerPassiveGamePanel extends GamePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3528849965271118208L;

	public MultiPlayerPassiveGamePanel(final BubbleMesh bubbleMesh, Connector connector){
		super(bubbleMesh);
		room = new SlaveRoom(cannonPosition, this.getPreferredSize(), connector);
		room.setup();
		gameTick.registerObserver(room);
	}
	
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		
		//add a grey overlay
		graphics.setColor(new Color(120,120,120,70)); // where a <1.0
		graphics.fillRect(0,0,this.getWidth(),this.getHeight());
	}	

}
