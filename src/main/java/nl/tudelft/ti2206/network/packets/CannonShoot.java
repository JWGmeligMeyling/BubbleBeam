package nl.tudelft.ti2206.network.packets;

import nl.tudelft.util.Vector2f;

/**
 * The {@code CannonShoot} {@link Packet} is used to send and receive
 * information about a cannon shooting.
 * 
 * @author Sam Smulders
 */
public class CannonShoot implements Packet {

	private static final long serialVersionUID = 1546268759069464515L;

	public final Vector2f direction;
	
	public CannonShoot(Vector2f direction) {
		this.direction = new Vector2f(direction);
	}
	
	@Override
	public void notify(PacketHandler packetHandlerCollection) {
		packetHandlerCollection.notify(this);
	}

	public Vector2f getDirection() {
		return direction;
	}
}