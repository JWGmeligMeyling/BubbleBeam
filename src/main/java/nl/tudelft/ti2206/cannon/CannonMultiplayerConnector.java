package nl.tudelft.ti2206.cannon;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.util.Vector2f;

/**
 * The {@code CannonMultiplayerConnector} can listen to a
 * {@link CannonController} to send it's behaviour to an other game instance
 * over a {@link Connector}.
 * 
 * @author Sam Smulders
 */
public class CannonMultiplayerConnector implements CannonControllerObserver {
	
	protected final Connector controller;
	
	public CannonMultiplayerConnector(Connector controller) {
		this.controller = controller;
	}
	
	@Override
	public void cannonRotate(double direction) {
		controller.sendPacket(new Packet.CannonRotate(direction));
	}
	
	@Override
	public void cannonShoot(Vector2f direction) {
		controller.sendPacket(new Packet.CannonShoot(direction));
	}
	
}
