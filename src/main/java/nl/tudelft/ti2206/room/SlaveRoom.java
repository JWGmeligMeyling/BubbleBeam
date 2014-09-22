package nl.tudelft.ti2206.room;

import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.cannon.SlaveCannonController;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.Packet.BubbleMeshSync;
import nl.tudelft.ti2206.network.packets.Packet.LoadNewBubble;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;
import nl.tudelft.ti2206.network.packets.PacketListener;

/**
 * The SlaveRoom is used to copy the behaviour of the room in the opponent
 * program instance.
 * 
 * @author Sam Smulders
 */
public class SlaveRoom extends Room {
	
	private PacketHandlerCollection packetHandlerCollection;
	private PacketListener<BubbleMeshSync> bubbleMeshSyncListener;
	private PacketListener<LoadNewBubble> loadNewBubbleListener;
	
	public SlaveRoom(Point cannonPosition, Dimension dimension,
			Connector connector) {
		super(cannonPosition, dimension);
		this.packetHandlerCollection = connector.getPacketHandlerCollection();
		
		cannonController = new SlaveCannonController(connector);
		cannonController.registerObserver(cannon);
		
		bubbleMeshSyncListener = new PacketListener<Packet.BubbleMeshSync>() {
			@Override
			public void update(Packet.BubbleMeshSync packet) {
				bubbleMesh = BubbleMesh.parse(packet.parseString);
				bubbleMesh.calculatePositions();
			}
		};
		loadNewBubbleListener = new PacketListener<Packet.LoadNewBubble>() {
			@Override
			public void update(Packet.LoadNewBubble packet) {
				System.out.println("Bubble packet received in SlaveRoom");
				bubbleQueue.add(new ColouredBubble(packet.color));
				correctBubblePositions();
			}
		};
		packetHandlerCollection.bubbleMeshSyncHandler.registerObserver(bubbleMeshSyncListener);
		packetHandlerCollection.loadNewBubbleHandler.registerObserver(loadNewBubbleListener);
	}
	
	@Override
	public void deconstruct() {
		packetHandlerCollection.bubbleMeshSyncHandler.removeObserver(bubbleMeshSyncListener);
		packetHandlerCollection.loadNewBubbleHandler.removeObserver(loadNewBubbleListener);
	}
	
	/*
	 * TODO Implement a way to stack tasks when they can't be executed
	 * instantly. Like shooting & adding new rows.
	 */
	// public class Task { public void run() { } }
	// public void addTask(Task task) {}
	// addTask(new Task() {@Override public void run() { } });
	
	protected void addBubble() {
		bubbleQueue.add(new ColouredBubble(bubbleMesh.getRandomRemainingColor()));
	}
	
}
