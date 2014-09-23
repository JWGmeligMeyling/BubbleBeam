package nl.tudelft.ti2206.game;

import java.awt.Color;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.room.SlaveRoom;

public class MultiPlayerPassiveGamePanel extends GamePanel {

	private static final long serialVersionUID = 3528849965271118208L;

	public MultiPlayerPassiveGamePanel(final BubbleMesh bubbleMesh, Connector connector){
		super(bubbleMesh);
		room = new SlaveRoom(cannonPosition, this.getPreferredSize(), connector);
		room.setup();
		gameTick.registerObserver(room);
		this.setBackground(new Color(225, 225, 225));
	}
	
}
