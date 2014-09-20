/*package nl.tudelft.ti2206.cannon.temp;

import nl.tudelft.ti2206.cannon.Cannon2;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.ti2206.room.Room;

public class CannonControllerMultiplayer implements CannonController {
	
	private Room room;
	private Cannon2 cannon;
	private PacketHandlerCollection packetHandlerCollection;
	protected CannonShootListener cannonShootListener = new CannonShootListener();
	protected CannonRotateListener cannonRotateListener = new CannonRotateListener();
	
	public CannonControllerMultiplayer(Connector connector) {
		packetHandlerCollection = connector.getPacketHandlerCollection();
		packetHandlerCollection.cannonShootHandler.registerObserver(cannonShootListener);
		packetHandlerCollection.cannonRotateHandler.registerObserver(cannonRotateListener);
	}
	
	private class CannonShootListener implements PacketListener<Packet.CannonShoot> {
		@Override
		public void update(Packet.CannonShoot packet) {
			room.shootBubble(packet.direction);
		}
	}
	
	private class CannonRotateListener implements PacketListener<Packet.CannonRotate> {
		@Override
		public void update(Packet.CannonRotate packet) {
			cannon.angle = packet.direction;
		}
	}
	
	@Override
	public void setCannon(Cannon2 cannon) {
		this.cannon = cannon;
		this.room = cannon.room;
	}
	
	public void deconstruct() {
		packetHandlerCollection.cannonRotateHandler.removeObserver(cannonRotateListener);
		packetHandlerCollection.cannonShootHandler.removeObserver(cannonShootListener);
		cannonShootListener = null;
		cannonRotateListener = null;
		room = null;
		cannon = null;
		packetHandlerCollection = null;
	}
}
*/