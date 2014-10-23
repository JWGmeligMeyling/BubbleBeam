package nl.tudelft.ti2206.game.backend;

import static org.junit.Assert.*;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.game.event.GameListener;
import nl.tudelft.ti2206.game.event.GameListener.GameOverEvent;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.EventPacket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConnectorGameListenerTest {
	
	protected GameController gameController = Mockito.mock(GameController.class);
	protected ConnectorGameListener listener;
	protected Connector connector;

	@Before
	public void setUp() throws Exception {
		connector = Mockito.mock(Connector.class);
		listener = new ConnectorGameListener(connector);
	}

	@Test
	public void testGameOver() {
		GameListener.GameOverEvent event = new GameListener.GameOverEvent(gameController, new GameOver());
		listener.gameOver(event);
		Mockito.verify(connector).sendPacket(new EventPacket(event));
	}
	
}
