package nl.tudelft.ti2206.cannon;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.Packet.LoadNewBubble;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.util.Vector2f;

public class RoomControllerSlave implements RoomController {
	protected BubbleMeshSyncListener bubbleMeshSyncListener = new BubbleMeshSyncListener();
	protected LoadNewBubbleListener loadNewBubbleListener = new LoadNewBubbleListener();
	private Room room;
	private PacketHandlerCollection packetHandlerCollection;
	private Connector connector;
	
	public RoomControllerSlave(Connector connector) {
		this.connector = connector;
		this.packetHandlerCollection = connector.getPacketHandlerCollection();
		this.packetHandlerCollection.bubbleMeshSyncHandler.registerObserver(bubbleMeshSyncListener);
		this.packetHandlerCollection.loadNewBubbleHandler.registerObserver(loadNewBubbleListener);
	}
	
	private class BubbleMeshSyncListener implements PacketListener<Packet.BubbleMeshSync> {
		@Override
		public void update(Packet.BubbleMeshSync packet) {
			room.addTask(new Task() {
				@Override
				public void run() {
				}
			});
		}
	}
	
	private class LoadNewBubbleListener implements PacketListener<Packet.LoadNewBubble> {
		@Override
		public void update(LoadNewBubble packet) {
		}
	}
	
	@Override
	public void shootBubble(Vector2f direction) {
		connector.sendPacket(new Packet.CannonShoot(direction));
	}
}
