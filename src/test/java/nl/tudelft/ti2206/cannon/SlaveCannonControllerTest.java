package nl.tudelft.ti2206.cannon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.CannonRotate;
import nl.tudelft.ti2206.network.packets.CannonShoot;
import nl.tudelft.ti2206.network.packets.MockedPacketHandler;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.util.Vector2f;
import nl.tudelft.ti2206.game.event.CannonListener.*;

import org.junit.Before;
import org.junit.Test;

public class SlaveCannonControllerTest extends AbstractCannonControllerTest {

	protected SlaveCannonController slaveCannonController;
	
	protected PacketListener.CannonShootPacketListener cannonShootHandler;
	protected PacketListener.CannonRotatePacketListener cannonRotateHandler;
	
	@Before
	public void setUp() throws Exception {
		cannonModel = spy(new CannonModel());
		cannonController = slaveCannonController = spy(new SlaveCannonController(cannonModel));
		shootListener = mock(CannonShootListener.class);
		cannonModel.addEventListener(shootListener);
		assertEquals(cannonModel, cannonController.getModel());
		bindConnector();
		resetMocks();
	}
	
	protected void bindConnector() {
		Connector connector = mock(Connector.class);
		MockedPacketHandler collection = spy(new MockedPacketHandler());
		when(connector.getPacketHandlerCollection()).thenReturn(collection);
		
		slaveCannonController.bindConnectorAsSlave(connector);
		verify(collection, times(2)).addEventListener(any(), any());
		cannonShootHandler = collection.getListenerOfType(PacketListener.CannonShootPacketListener.class);
		cannonRotateHandler = collection.getListenerOfType(PacketListener.CannonRotatePacketListener.class);
	}

	@Test
	public void testCannonShootHandler() {
		CannonShoot packet = mock(CannonShoot.class);
		Vector2f direction = new Vector2f(1f,1f);
		
		when(packet.getDirection()).thenReturn(direction);
		cannonShootHandler.receivedCannonShoot(packet);
		
		verify(slaveCannonController, times(1)).setAngle(direction);
		verify(slaveCannonController, times(1)).shoot();
	}
	

	@Test
	public void testCannonRotateHandler() {
		CannonRotate packet = mock(CannonRotate.class);
		double rotation = 0.89d;
		
		when(packet.getRotation()).thenReturn(rotation);
		cannonRotateHandler.receivedCannonRotate(packet);
		
		verify(cannonModel, times(1)).setAngle(rotation);
		verify(cannonModel, times(1)).notifyObservers();
	}

}
