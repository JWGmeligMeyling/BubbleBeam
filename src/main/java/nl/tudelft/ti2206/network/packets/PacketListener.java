package nl.tudelft.ti2206.network.packets;

/**
 * PacketListeners can be registered by the related PacketHandler to receive
 * their Packets.
 * 
 * @author Sam Smulders
 */
public interface PacketListener<T extends Packet> {
	public void update(T packet);
}
