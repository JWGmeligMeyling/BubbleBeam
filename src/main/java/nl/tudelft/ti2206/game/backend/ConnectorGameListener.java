package nl.tudelft.ti2206.game.backend;

import nl.tudelft.ti2206.game.event.GameListener;
import nl.tudelft.ti2206.logger.Logger;
import nl.tudelft.ti2206.logger.LoggerFactory;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.EventPacket;

/**
 * The {@code ConnectorGameListener} listens for {@link GameEvent GameEvents}
 * triggered by the {@link GameController} and sends them over the
 * {@link Connector}
 * 
 * @author Jan-Willem Gmelig Meyling
 * @see SlaveGamePacketListener
 *
 */
public class ConnectorGameListener implements GameListener {
	
	private static final Logger log = LoggerFactory.getLogger(ConnectorGameListener.class);
	
	protected final Connector connector;
	
	/**
	 * The {@code ConnectorGameListener} listens for {@link GameEvent
	 * GameEvents} triggered by the {@link GameController} and sends them over
	 * the {@link Connector}
	 * 
	 * @param connector {@code Connector} for this {@code ConnectorGameListener}
	 */
	public ConnectorGameListener(Connector connector) {
		this.connector = connector;
	}
	
	@Override
	public void rowInsert(RowInsertEvent event) {
		log.info("Sending insert row");
		connector.sendPacket(new EventPacket(event));
	}
	
	@Override
	public void shoot(CannonShootEvent event) {
		log.info("Sending shoot packet");
		connector.sendPacket(new EventPacket(event));
	}

	@Override
	public void gameOver(GameOverEvent event) {
		log.info("Sending game over event {}", event);
		connector.sendPacket(new EventPacket(event));
	}

	@Override
	public void shotMissed(ShotMissedEvent event) {
	}
	
	@Override
	public void score(ScoreEvent event) {}
	
	@Override
	public void pop(BubblePopEvent event) {
		log.info("{} bubbles popped", event.amountOfPoppedBubbles());
		connector.sendPacket(new EventPacket(event));
	}
	
	@Override
	public void rotate(CannonRotateEvent event) {
		connector.sendPacket(new EventPacket(event));
	}
	
	@Override
	public void ammo(AmmoLoadEvent event) {
		log.info("Sending ammo packet with [{}, {}]", event.getLoadedBubble(), event.getNextBubble());
		connector.sendPacket(new EventPacket(event));
	}
	
}