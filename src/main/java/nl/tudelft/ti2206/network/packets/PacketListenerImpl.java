package nl.tudelft.ti2206.network.packets;

import java.awt.Point;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.cannon.CannonModel;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.BubblePopEvent;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.RowInsertEvent;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.ScoreEvent;
import nl.tudelft.ti2206.game.event.CannonListener.CannonRotateEvent;
import nl.tudelft.ti2206.game.event.CannonListener.CannonShootEvent;
import nl.tudelft.ti2206.game.event.GameListener.AmmoLoadEvent;
import nl.tudelft.ti2206.game.event.GameListener.GameOverEvent;
import nl.tudelft.ti2206.game.event.GameListener.ShotMissedEvent;

public class PacketListenerImpl implements PacketListener {
	

	private static final Logger log = LoggerFactory.getLogger(PacketListenerImpl.class);
	
	protected final GameController gameController;
	protected final GameModel gameModel;
	protected final CannonController cannonController;
	protected final CannonModel cannonModel;
	
	public PacketListenerImpl(GameController slaveGameController, CannonController slaveCannonController) {
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
	public void handleScoreEvent(ScoreEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleGameOver(GameOverEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleShotMissed(ShotMissedEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void disconnect() {
		gameModel.setWon(false);
		gameModel.setGameOver(true);
	}

}
