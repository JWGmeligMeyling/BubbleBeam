package nl.tudelft.ti2206.game;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.room.SingleplayerRoom;

public class SinglePlayerGamePanel extends GamePanel {
	
	private static final long serialVersionUID = -5651117591089785288L;

	public SinglePlayerGamePanel(final BubbleMesh bubbleMesh) {
		super(bubbleMesh);
		
		room = new SingleplayerRoom(cannonPosition, this.getPreferredSize(), bubbleMesh, this);
		room.setup();
		gameTick.registerObserver(room);
	}
	
}
