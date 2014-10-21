package nl.tudelft.ti2206.network.packets;

/**
 * The {@code CannonRotate} {@link Packet} is used to send and receive
 * information about a cannon rotating.
 * 
 * @author Sam Smulders
 */
public class CannonRotate implements Packet {
	
	private static final long serialVersionUID = -8205066675151884103L;
	
	public final double rotation;
	
	public CannonRotate(double direction) {
		this.rotation = direction;
	}
	
	@Override
	public void notify(PacketHandler packetHandlerCollection) {
		packetHandlerCollection.notify(this);
	}
	
	public double getRotation() {
		return rotation;
	}
	
}