package nl.tudelft.ti2206.game;

import java.awt.Graphics;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.network.Host;
import nl.tudelft.ti2206.room.MultiplayerRoom;

public class MultiPlayerActiveGamePanel extends GamePanel {
	
	
	Host connector;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 525456508008501827L;

	public MultiPlayerActiveGamePanel(final BubbleMesh bubbleMesh) {
		super(bubbleMesh);
		
		connector = new Host();
		room = new MultiplayerRoom(cannonPosition, this.getPreferredSize(), bubbleMesh, connector, this);
		room.setup();
		gameTick.registerObserver(room);
		//this is older code, keeping it here until the multiplayer overhaul works completely
		//this.cannon = new Cannon(bubbleMesh, new Point(WIDTH / 2, 400),
		//		this.getPreferredSize(), this.getLocation());
		//this.cannon.bindMouseListenerTo(this);
	}
	
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		//cannon.render(graphics);
	}

	@Override
	public void gameStep() throws GameOver {
		//cannon.gameStep();
		super.gameStep();
	}	
}
