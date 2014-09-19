package nl.tudelft.ti2206.network.packets;

/**
 * The PacketHandlerCollection is responsible for storing all the different
 * PacketHandlers and guiding the different notification to the desired
 * PacketHandler.
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
	
	public PacketHandler.CannonRotate cannonRotateHandler;
	private PacketHandler.CannonShoot cannonShootHandler;
	private PacketHandler.BubbleMeshSync bubbleMeshSyncHandler;
	private PacketHandler.LoadNewBubble loadNewBubbleHandler;
	
	public void notify(Packet.CannonRotate packet) {
		cannonRotateHandler.notifyObservers(packet);
	}
	
	public void notify(Packet.CannonShoot packet) {
		cannonShootHandler.notifyObservers(packet);
	}
	
	public void notify(Packet.BubbleMeshSync packet) {
		bubbleMeshSyncHandler.notifyObservers(packet);
	}
	
	public void notify(Packet.LoadNewBubble packet) {
		loadNewBubbleHandler.notifyObservers(packet);
	}
	
}
