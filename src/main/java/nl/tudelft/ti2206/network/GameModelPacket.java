package nl.tudelft.ti2206.network;

import nl.tudelft.ti2206.game.backend.GameModel;

/**
 * The {@code GameModelPacket} is used to send the {@link GameModel GameModels}
 * from the server to the connected client.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class GameModelPacket implements Packet {

	private static final long serialVersionUID = -3618436624787528399L;
	
	private final GameModel masterGameModel, slaveGameModel;
	
	/**
	 * Construct a new {@link GameModelPacket}
	 * @param masterGameModel The {@link GameModel} for the master panel
	 * @param slaveGameModel The {@link GameModel} for the slave panel
	 */
	public GameModelPacket(GameModel masterGameModel, GameModel slaveGameModel) {
		this.masterGameModel = masterGameModel;
		this.slaveGameModel = slaveGameModel;
	}
	
	/**
	 * @return The {@link GameModel} for the master panel
	 */
	public GameModel getMasterGameModel() {
		return masterGameModel;
	}

	/**
	 * @return The {@link GameModel} for the slave panel
	 */
	public GameModel getSlaveGameModel() {
		return slaveGameModel;
	}
	
}