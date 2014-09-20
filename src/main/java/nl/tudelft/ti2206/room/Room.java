package nl.tudelft.ti2206.room;

import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.MovingBubble;
import nl.tudelft.ti2206.cannon.Cannon2;
import nl.tudelft.ti2206.cannon.CannonControllerMultiplayer;
import nl.tudelft.ti2206.network.Client;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.util.Vector2f;

/**
 * The Room is responsible for keeping track of all objects within the room.
 * 
 * @author Sam_
 *
 */
public class Room {
	protected final int WIDTH, HEIGHT;
	protected final Cannon2 cannon;
	
	protected ColouredBubble nextBubble, loadedBubble;
	protected MovingBubble shotBubble;
	
	protected static final int MAX_MISSES = 5;
	
	public Room(final int width, final int height, Cannon2 cannon) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.cannon = cannon;
		cannon.setRoom(this);
		Connector connector = new Client("127.0.0.1");
		new CannonControllerMultiplayer(connector);
		connector.start();
	}
	
	public boolean canShoot() {
		return shotBubble == null;
	}
	
	public void shootBubble(Vector2f direction) {
		
	}

	public void addTask(Task task) {
		// TODO Auto-generated method stub
		
	}
}
