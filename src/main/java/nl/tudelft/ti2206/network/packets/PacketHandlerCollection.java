package nl.tudelft.ti2206.network.packets;

/**
 * The {@code PacketHandlerCollection} is responsible for storing all the
 * different {@link PacketHandler}s and guiding the different notification to
 * the desired {@link PacketHandler}.
 * 
 * @author Sam Smulders
 */
public class PacketHandlerCollection {
	
	public PacketHandlerCollection() {
		this.cannonRotateHandler = new PacketHandler.CannonRotate();
		this.cannonShootHandler = new PacketHandler.CannonShoot();
		this.bubbleMeshSyncHandler = new PacketHandler.BubbleMeshSync();
		this.loadNewBubbleHandler = new PacketHandler.LoadNewBubble();
	}
	
	public final PacketHandler.CannonRotate cannonRotateHandler;
	public final PacketHandler.CannonShoot cannonShootHandler;
	public final PacketHandler.BubbleMeshSync bubbleMeshSyncHandler;
	public final PacketHandler.LoadNewBubble loadNewBubbleHandler;
	
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
	public void notify(Packet.LoadNewBubble packet) {
		loadNewBubbleHandler.notifyObservers(packet);
	}
	
}
