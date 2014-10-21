package nl.tudelft.ti2206.network.packets;

import java.util.EventListener;

/**
 * PacketListeners can be registered by the related PacketHandler to receive
 * their Packets.
 * 
 * @author Sam Smulders
 */
public interface PacketListener extends EventListener {
	
	/**
	 * Received Ammo packet
	 * @param packet
	 */
	void receivedAmmoPacket(AmmoPacket packet);
	
	/**
	 * Recieved BubbleMeshSync packet
	 * @param packet
	 */
	void receivedBubbleMeshSync(BubbleMeshSync packet);
	
	/**
	 * 
	 * @param packet
	 */
	void receivedCannonRotate(CannonRotate packet);
	
	/**
	 * 
	 * @param packet
	 */
	void receivedCannonShoot(CannonShoot packet);
	
	/**
	 * 
	 * @param packet
	 */
	void receivedGameModelPacket(GameModelPacket packet);
	
	/**
	 * 
	 * @param poppedPacket
	 */
	void receivedPoppedPacket(PoppedPacket poppedPacket);
	
	interface AmmoPacketListener extends PacketListener {
		@Override default void receivedBubbleMeshSync(BubbleMeshSync packet) {};
		@Override default void receivedCannonRotate(CannonRotate packet) {};
		@Override default void receivedCannonShoot(CannonShoot packet) {};
		@Override default void receivedGameModelPacket(GameModelPacket packet) {};
		@Override default void receivedPoppedPacket(PoppedPacket poppedPacket) {};
	}
	
	interface BubbleMeshSyncListener extends PacketListener {
		@Override default void receivedAmmoPacket(AmmoPacket packet) {};
		@Override default void receivedCannonRotate(CannonRotate packet) {};
		@Override default void receivedCannonShoot(CannonShoot packet) {};
		@Override default void receivedGameModelPacket(GameModelPacket packet) {};
		@Override default void receivedPoppedPacket(PoppedPacket poppedPacket) {};
	}
	
	interface CannonRotatePacketListener extends PacketListener {
		@Override default void receivedAmmoPacket(AmmoPacket packet) {};
		@Override default void receivedBubbleMeshSync(BubbleMeshSync packet) {};
		@Override default void receivedCannonShoot(CannonShoot packet) {};
		@Override default void receivedGameModelPacket(GameModelPacket packet) {};
		@Override default void receivedPoppedPacket(PoppedPacket poppedPacket) {};
	}
	
	interface CannonShootPacketListener extends PacketListener {
		@Override default void receivedAmmoPacket(AmmoPacket packet) {};
		@Override default void receivedBubbleMeshSync(BubbleMeshSync packet) {};
		@Override default void receivedCannonRotate(CannonRotate packet) {};
		@Override default void receivedGameModelPacket(GameModelPacket packet) {};
		@Override default void receivedPoppedPacket(PoppedPacket poppedPacket) {};
	}
	
	interface GameModelPacketListener extends PacketListener {
		@Override default void receivedAmmoPacket(AmmoPacket packet) {};
		@Override default void receivedBubbleMeshSync(BubbleMeshSync packet) {};
		@Override default void receivedCannonRotate(CannonRotate packet) {};
		@Override default void receivedCannonShoot(CannonShoot packet) {};
		@Override default void receivedPoppedPacket(PoppedPacket poppedPacket) {};
	}
	
	interface PoppedPacketListener extends PacketListener {
		@Override default void receivedAmmoPacket(AmmoPacket packet) {};
		@Override default void receivedBubbleMeshSync(BubbleMeshSync packet) {};
		@Override default void receivedCannonRotate(CannonRotate packet) {};
		@Override default void receivedCannonShoot(CannonShoot packet) {};
		@Override default void receivedGameModelPacket(GameModelPacket packet) {};
	}
}
