package nl.tudelft.ti2206.cannon;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.ti2206.room.Room;

/**
 * The SlaveCannonController is used to copy what an other player is doing by
 * accepting packets from a given connector.
 * 
 * @author Sam Smulders
 */
public class SlaveCannonController extends CannonController {
	protected PacketHandlerCollection packetHandlerCollection;
	protected CannonShootListener cannonShootListener = new CannonShootListener();
	protected CannonRotateListener cannonRotateListener = new CannonRotateListener();
	protected Room room;
	
	public SlaveCannonController(Connector connector) {
		packetHandlerCollection = connector.getPacketHandlerCollection();
		packetHandlerCollection.cannonShootHandler.registerObserver(cannonShootListener);
		packetHandlerCollection.cannonRotateHandler.registerObserver(cannonRotateListener);
	}
	
	private class CannonShootListener implements PacketListener<Packet.CannonShoot> {
		@Override
		public void update(Packet.CannonShoot packet) {
			notifyObserversShoot(packet.direction);
		}
	}
	
	private class CannonRotateListener implements PacketListener<Packet.CannonRotate> {
		@Override
		public void update(Packet.CannonRotate packet) {
			notifyObserversRotate(packet.rotation);
		}
	}
	
	public void deconstruct() {
		packetHandlerCollection.cannonRotateHandler.removeObserver(cannonRotateListener);
		packetHandlerCollection.cannonShootHandler.removeObserver(cannonShootListener);
		/*
		 * cannonShootListener = null; cannonRotateListener = null; room = null;
		 * cannon = null; packetHandlerCollection = null;
		 */
	}
}
