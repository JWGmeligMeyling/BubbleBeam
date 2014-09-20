package nl.tudelft.ti2206.cannon;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.ti2206.room.Room;
import nl.tudelft.ti2206.room.Task;

public class SlaveCannonController extends CannonController {
	protected PacketHandlerCollection packetHandlerCollection;
	protected CannonShootListener cannonShootListener = new CannonShootListener();
	protected CannonRotateListener cannonRotateListener = new CannonRotateListener();
	protected Room room;
	
	public SlaveCannonController(Connector connector, Cannon2 cannon) {
		super(cannon);
		packetHandlerCollection = connector.getPacketHandlerCollection();
		packetHandlerCollection.cannonShootHandler.registerObserver(cannonShootListener);
		packetHandlerCollection.cannonRotateHandler.registerObserver(cannonRotateListener);
	}
	
	private class CannonShootListener implements PacketListener<Packet.CannonShoot> {
		@Override
		public void update(Packet.CannonShoot packet) {
			room.addTask(new Task(){
				@Override
				public void run(){
					room.shootBubble(packet.direction);
				}
			});
		}
	}
	
	private class CannonRotateListener implements PacketListener<Packet.CannonRotate> {
		@Override
		public void update(Packet.CannonRotate packet) {
			cannon.setAngle(packet.direction);
		}
	}
	
	public void deconstruct() {
		packetHandlerCollection.cannonRotateHandler.removeObserver(cannonRotateListener);
		packetHandlerCollection.cannonShootHandler.removeObserver(cannonShootListener);
		/*cannonShootListener = null;
		cannonRotateListener = null;
		room = null;
		cannon = null;
		packetHandlerCollection = null;*/
	}
}
