package nl.tudelft.ti2206.game.backend;

import java.awt.Point;

import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.cannon.CannonModel;
import nl.tudelft.ti2206.game.event.GameListener;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.BubblePopEvent;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.RowInsertEvent;
import nl.tudelft.ti2206.game.event.CannonListener.CannonRotateEvent;
import nl.tudelft.ti2206.game.event.CannonListener.CannonShootEvent;
import nl.tudelft.ti2206.game.event.GameListener.GameEvent;
import nl.tudelft.ti2206.game.event.GameListener.AmmoLoadEvent;
import nl.tudelft.ti2206.game.event.GameListener.GameOverEvent;
import nl.tudelft.ti2206.game.event.GameListener.ShotMissedEvent;
import nl.tudelft.ti2206.logger.Logger;
import nl.tudelft.ti2206.logger.LoggerFactory;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.GameModelPacket;
import nl.tudelft.ti2206.network.Packet;
import nl.tudelft.ti2206.network.PacketListener;

/**
 * The {@code SlaveGamePacketListener} listens for {@link Packet Packets} on the
 * {@link Connector}, and triggers the corresponding {@link GameEvent GameEvents} on
 * the corresponding {@link GameController}.
 *  
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class SlaveGamePacketListener implements PacketListener {
	
	private static final Logger log = LoggerFactory.getLogger(SlaveGamePacketListener.class);
	
	protected final GameController gameController;
	protected final GameModel gameModel;
	protected final CannonController cannonController;
	protected final CannonModel cannonModel;

	/**
	 * The {@code SlaveGamePacketListener} listens for {@link Packet Packets} on
	 * the {@link Connector}, and triggers the corresponding {@link GameEvent
	 * GameEvents} on the corresponding {@link GameController}.
	 * 
	 * @param slaveGameController
	 *            {@link GameController} for the slave panel
	 * @param slaveCannonController
	 *            {@link CannonController} for the slave panel
	 */
	public SlaveGamePacketListener(GameController slaveGameController, CannonController slaveCannonController) {
		this.gameController = slaveGameController;
		this.gameModel = slaveGameController.getModel();
		this.cannonController = slaveCannonController;
		this.cannonModel = slaveCannonController.getModel();
	}

	@Override
	public void handleAmmoLoad(AmmoLoadEvent event) {
		log.info("Processing event {}", event);
		gameModel.setLoadedBubble(event.getLoadedBubble());
		gameModel.setNextBubble(event.getNextBubble());
		gameModel.notifyObservers();
	}

	@Override
	public void handleGameModelPacket(GameModelPacket packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCannonRotate(CannonRotateEvent event) {
		cannonController.getModel().setAngle(event.getAngle());
		cannonController.getModel().notifyObservers();
	}

	@Override
	public void handleCannonShoot(CannonShootEvent event) {
		log.info("Processing event {}", event);
		cannonController.setAngle(event.getDirection());
		cannonController.shoot();
	}

	@Override
	public void handleRowInsert(RowInsertEvent event) {
		log.info("Processing event {}", event);
		
		// Hack this packet a bit..
		event.getInsertedBubbles().forEach(bubble -> {
			bubble.setPosition(new Point(2,2));
			bubble.getConnections().clear();
		});
		
		gameModel.getBubbleMesh().insertRow(event.getInsertedBubbles().iterator());
	}

	@Override
	public void handleBubblePop(BubblePopEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleScoreEvent(GameListener.ScoreEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleGameOver(GameOverEvent event) {
		gameController.gameOver(event.getGameOver());
	}

	@Override
	public void handleShotMissed(ShotMissedEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void disconnect() {
		// On disconnect, the other player wins
		gameController.gameOver(new GameOver(false));
	}

}
