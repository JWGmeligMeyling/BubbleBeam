package nl.tudelft.ti2206.game.backend;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.cannon.MouseCannonController;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.util.Vector2f;

public class MasterGameController extends GameController {
	
	private static final Logger log = LoggerFactory.getLogger(MasterGameController.class);

	protected final MouseCannonController cannonController;
	
	protected Connector connector;
	
	public MasterGameController(final BubbleMesh bubbleMesh, final GameTick gameTick) {
		this(new GameModel(bubbleMesh), new MouseCannonController(), gameTick);
	}
	
	public MasterGameController(final BubbleMesh bubbleMesh, final Connector connector,
			final GameTick gameTick) {
		
		this(new GameModel(bubbleMesh), connector, new MouseCannonController(), gameTick);
	}
	
	public MasterGameController(final GameModel model, final Connector connector,
			final MouseCannonController cannonController, final GameTick gameTick) {
	
		this(model, cannonController, gameTick);
		bindConnector(connector);
	}

	public MasterGameController(final GameModel model,
			final MouseCannonController cannonController, final GameTick gameTick) {
		
		super(model, cannonController, gameTick);
		this.cannonController = cannonController;
	}
	
	
	@Override
	public MouseCannonController getCannonController() {
		return cannonController;
	}
	
	/**
	 * Bind a connector for multiplayer
	 * @param connector
	 */
	public void bindConnector(final Connector connector) {
		this.connector = connector;
		cannonController.bindController(connector);
		sendInitialData(connector);
	}
	
	protected void sendInitialData(final Connector connector) {
		connector.sendPacket(new Packet.BubbleMeshSync(model.getBubbleMesh()));
		connector.sendPacket(new Packet.AmmoPacket(
				model.getLoadedBubble().getColor(),
				model.getNextBubble().getColor()));
	}
	
	@Override
	protected void shoot(final Vector2f direction) {
		super.shoot(direction);
		
		if(connector == null) return;
		log.info("Sending shoot packet");
		connector.sendPacket(new Packet.CannonShoot(direction));
		
		Color loadedBubble = model.getLoadedBubble().getColor();
		Color nextBubble = model.getNextBubble().getColor();
		log.info("Sending ammo packet with [{}, {}]", loadedBubble, nextBubble);
		connector.sendPacket(new Packet.AmmoPacket(loadedBubble, nextBubble));
	}

	@Override
	protected void insertRow() {
		super.insertRow();
		if(connector != null) {
			log.info("Sending insert row");
			connector.sendPacket(new Packet.BubbleMeshSync(model
					.getBubbleMesh()));
		}
	}
	
}
