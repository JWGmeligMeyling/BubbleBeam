package nl.tudelft.ti2206.cannon;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;
import nl.tudelft.ti2206.network.packets.PacketListener;

public class CannonControllerMultiplayer implements CannonController {
	
	protected CannonShootListener cannonShootListener = new CannonShootListener();
	protected CannonRotateListener cannonRotateListener = new CannonRotateListener();
	private Room room;
	private Cannon2 cannon;
	
	public CannonControllerMultiplayer(Connector connector) {
		PacketHandlerCollection col = connector.getPacketHandlerCollection();
		col.cannonShootHandler.registerObserver(cannonShootListener);
		col.cannonRotateHandler.registerObserver(cannonRotateListener);
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
}
