package nl.tudelft.ti2206.game.backend;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.factory.DefaultBubbleFactory;
import nl.tudelft.ti2206.cannon.SlaveCannonController;
import nl.tudelft.ti2206.game.GamePanel;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;

public class SlaveGameController extends GameController {

	protected final SlaveCannonController cannonController;
	
	public SlaveGameController(final BubbleMesh bubbleMesh,
			final Connector connector, final GameTick gameTick) {
		this(new GameModel(bubbleMesh), connector, new SlaveCannonController(), gameTick);
	}
	
	public SlaveGameController(final GameModel model,
			final Connector connector, final GameTick gameTick) {
		this(model, connector, new SlaveCannonController(), gameTick);
	}

	private SlaveGameController(final GameModel model,
			final Connector connector,
			final SlaveCannonController cannonController,
			final GameTick gameTick) {
		
		this(model, connector, cannonController, gameTick, new DefaultBubbleFactory());
	}
	
	private SlaveGameController(final GameModel model,
			final Connector connector,
			final SlaveCannonController cannonController,
			final GameTick gameTick,
			final DefaultBubbleFactory factory) {
		
		super(model, cannonController, gameTick, factory);
		this.cannonController = cannonController;
		
		bindListeners(connector.getPacketHandlerCollection());
		cannonController.bindConnector(connector);
	}
	
	protected void bindListeners(PacketHandlerCollection packetHandlerCollection) {
		packetHandlerCollection.registerBubbleMeshSyncListener((packet) -> {
			model.setBubbleMesh(packet.bubbleMesh);
		});

		packetHandlerCollection.registerLoadNewBubbleListener((packet) -> {
			
			packet.loadedBubble.setCenter(GamePanel.AMMO_POSITION);
			packet.nextBubble.setCenter(GamePanel.AMMO_NEXT_POSITION);
			
			model.setLoadedBubble(packet.loadedBubble);
			model.setNextBubble(packet.nextBubble);
		});
	}

	@Override
	public SlaveCannonController getCannonController() {
		return cannonController;
	}
	
	@Override
	protected void updateBubbles() {
		// Do nothing, fetch them from network
	}
	
	@Override
	protected void insertRow() {
		// Don't insert rows by logic, replace mesh instead
	}

}
