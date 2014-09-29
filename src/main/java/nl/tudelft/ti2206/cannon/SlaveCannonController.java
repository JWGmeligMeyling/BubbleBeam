package nl.tudelft.ti2206.cannon;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;

/**
 * The {@code SlaveCannonController} is a controller who's behaviour is
 * determined by the data received by a connector. By listening to received
 * {@link CannonShoot} and {@link CannonRotate} {@link Packets}, a
 * SlaveCannonController is able to copy the behaviour of an other
 * CannonController, like the opponents CannonController.
 * 
 * @author Sam Smulders
 */
public class SlaveCannonController extends AbstractCannonController {
	
	public SlaveCannonController() {
		super();
	}

	public SlaveCannonController(CannonModel cannonModel) {
		super(cannonModel);
	}

	public void bindConnector(final Connector connector) {
		final PacketHandlerCollection packetHandlerCollection = connector.getPacketHandlerCollection();
		
		packetHandlerCollection.registerCannonShootHandler((packet) -> {
			SlaveCannonController.this.setAngle(packet.getDirection());
			SlaveCannonController.this.shoot();
		});
		
		packetHandlerCollection.registerCannonRotateHandler((packet) -> {
			SlaveCannonController.this.getModel().setAngle(packet.getRotation());
			SlaveCannonController.this.getModel().notifyObservers();
		});
	}
	
}
