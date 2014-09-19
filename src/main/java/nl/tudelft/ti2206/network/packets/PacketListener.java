package nl.tudelft.ti2206.network.packets;

/**
 * PacketListeners can be registered by the related PacketHandler to receive
 * their Packets.
 * 
 * @author Sam Smulders
 */
public interface PacketListener {
	
	public interface CannonRotate extends PacketListener {
		void update(Packet.CannonRotate packet);
	}
	
	public interface CannonShoot extends PacketListener {
		void update(Packet.CannonShoot packet);
	}
	
	public interface BubbleMeshSync extends PacketListener {
		void update(Packet.BubbleMeshSync packet);
	}
	
	public interface LoadNewBubble extends PacketListener {
		void update(Packet.LoadNewBubble packet);
	}
}
