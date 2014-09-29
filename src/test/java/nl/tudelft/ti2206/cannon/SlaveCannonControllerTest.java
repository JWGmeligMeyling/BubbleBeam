package nl.tudelft.ti2206.cannon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.Packet.CannonRotate;
import nl.tudelft.ti2206.network.packets.Packet.CannonShoot;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.util.Vector2f;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SlaveCannonControllerTest extends AbstractCannonControllerTest {

	protected SlaveCannonController slaveCannonController;
	
	@Captor
	private ArgumentCaptor<PacketListener<CannonShoot>> cannonShootCaptor;
	
	@Captor
	private ArgumentCaptor<PacketListener<CannonRotate>> cannonRotateCaptor;
	
	protected PacketListener<CannonShoot> cannonShootHandler;
	protected PacketListener<CannonRotate> cannonRotateHandler;
	
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
		PacketHandlerCollection collection = spy(new PacketHandlerCollection());
		when(connector.getPacketHandlerCollection()).thenReturn(collection);
		
		slaveCannonController.bindConnector(connector);
		verify(collection, times(1)).registerCannonShootHandler(
				cannonShootCaptor.capture());
		verify(collection, times(1)).registerCannonRotateHandler(
				cannonRotateCaptor.capture());
		
		cannonShootHandler = cannonShootCaptor.getValue();
		cannonRotateHandler = cannonRotateCaptor.getValue();
	}

	@Test
	public void testCannonShootHandler() {
		Packet.CannonShoot packet = mock(Packet.CannonShoot.class);
		Vector2f direction = new Vector2f(1f,1f);
		
		when(packet.getDirection()).thenReturn(direction);
		cannonShootHandler.update(packet);
		
		verify(slaveCannonController, times(1)).setAngle(direction);
		verify(slaveCannonController, times(1)).shoot();
	}
	

	@Test
	public void testCannonRotateHandler() {
		Packet.CannonRotate packet = mock(Packet.CannonRotate.class);
		double rotation = 0.89d;
		
		when(packet.getRotation()).thenReturn(rotation);
		cannonRotateHandler.update(packet);
		
		verify(cannonModel, times(1)).setAngle(rotation);
		verify(cannonModel, times(1)).notifyObservers();
	}

}
