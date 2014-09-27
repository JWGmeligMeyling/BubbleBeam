package nl.tudelft.ti2206.network;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import nl.tudelft.ti2206.network.packets.Packet;

import org.junit.Test;

public class ConnectorTest {
	@Test
	public void connectorTest() {
		Host host = spy(new Host());
		Client client = spy(new Client("127.0.0.1"));
		new Thread(new Runnable() {
			@Override
			public void run() {
				host.connect();
			}
		}).start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.connect();
		
		verify(host).connect();
		verify(client).connect();
		
		Packet p = new Packet.CannonRotate(1.65f);
		host.sendPacket(p);
		
		host.start();
		client.start();
		
		
	}
}
