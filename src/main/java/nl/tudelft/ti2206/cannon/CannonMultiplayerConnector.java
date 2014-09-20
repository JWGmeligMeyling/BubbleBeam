package nl.tudelft.ti2206.cannon;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.util.Vector2f;

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
