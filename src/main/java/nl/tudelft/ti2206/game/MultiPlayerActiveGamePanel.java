package nl.tudelft.ti2206.game;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.room.MultiplayerRoom;

public class MultiPlayerActiveGamePanel extends GamePanel {
	
	private static final long serialVersionUID = 525456508008501827L;
	
	public MultiPlayerActiveGamePanel(final BubbleMesh bubbleMesh, Connector connector) {
		super(bubbleMesh);
		room = new MultiplayerRoom(cannonPosition, this.getPreferredSize(), bubbleMesh, connector,
				this);
		room.setup();
		gameTick.registerObserver(room);
	}
	
}
