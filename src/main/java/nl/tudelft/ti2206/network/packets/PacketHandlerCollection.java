package nl.tudelft.ti2206.network.packets;

import nl.tudelft.ti2206.network.packets.Packet.AmmoPacket;
import nl.tudelft.ti2206.network.packets.Packet.BubbleMeshSync;
import nl.tudelft.ti2206.network.packets.Packet.CannonRotate;
import nl.tudelft.ti2206.network.packets.Packet.CannonShoot;
import nl.tudelft.ti2206.network.packets.Packet.GameModelPacket;
import nl.tudelft.ti2206.network.packets.Packet.PoppedPacket;


/**
 * The {@code PacketHandlerCollection} is responsible for storing all the
 * different {@link PacketHandler}s and guiding the different notification to
 * the desired {@link PacketHandler}.
 * 
 * @author Sam Smulders
 */
public class PacketHandlerCollection {
	
	private final PacketHandler<CannonRotate> cannonRotateHandler = new PacketHandler<CannonRotate>(false);
	private final PacketHandler<CannonShoot> cannonShootHandler = new PacketHandler<CannonShoot>(true);
	private final PacketHandler<BubbleMeshSync> bubbleMeshSyncHandler = new PacketHandler<BubbleMeshSync>(true);
	private final PacketHandler<AmmoPacket> loadNewBubbleHandler = new PacketHandler<AmmoPacket>(true);
	private final PacketHandler<PoppedPacket> popHandler = new PacketHandler<PoppedPacket>(true);
	private final PacketHandler<GameModelPacket> gameModelPacketHandler = new PacketHandler<GameModelPacket>(true);
	
	public void registerCannonShootHandler(final PacketListener<CannonShoot> packetListener) {
		cannonShootHandler.registerObserver(packetListener);
	}
	
	public void registerCannonRotateHandler(final PacketListener<CannonRotate> packetListener) {
		cannonRotateHandler.registerObserver(packetListener);
	}
	
	public void registerBubbleMeshSyncListener(final PacketListener<BubbleMeshSync> packetListener) {
		bubbleMeshSyncHandler.registerObserver(packetListener);
	}
	

	public void registerLoadNewBubbleListener(final PacketListener<AmmoPacket> packetListener) {
		loadNewBubbleHandler.registerObserver(packetListener);
	}
	
	public void registerPopHandler(final PacketListener<PoppedPacket> packetListener){
		popHandler.registerObserver(packetListener);
	}
	
	public void registerGameModelPacketHandler(final PacketListener<GameModelPacket> packetListener) {
		gameModelPacketHandler.registerObserver(packetListener);
	}
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(Packet.CannonRotate packet) {
		cannonRotateHandler.notifyObservers(packet);
	}
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(Packet.CannonShoot packet) {
		cannonShootHandler.notifyObservers(packet);
	}
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(Packet.BubbleMeshSync packet) {
		bubbleMeshSyncHandler.notifyObservers(packet);
	}
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(Packet.AmmoPacket packet) {
		loadNewBubbleHandler.notifyObservers(packet);
	}

	public void notify(PoppedPacket poppedPacket) {
		popHandler.notifyObservers(poppedPacket);
	}
	
	public void notify(GameModelPacket packet) {
		gameModelPacketHandler.notifyObservers(packet);
	}

}
