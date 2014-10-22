package nl.tudelft.ti2206.network.packets;

import nl.tudelft.ti2206.game.backend.GameModel;

public class GameModelPacket implements Packet {

	private static final long serialVersionUID = -3618436624787528399L;
	
	private final GameModel masterGameModel, slaveGameModel;
	
	public GameModelPacket(GameModel masterGameModel, GameModel slaveGameModel) {
		this.masterGameModel = masterGameModel;
		this.slaveGameModel = slaveGameModel;
	}
	
	public GameModel getMasterGameModel() {
		return masterGameModel;
	}

	public GameModel getSlaveGameModel() {
		return slaveGameModel;
	}
	
}