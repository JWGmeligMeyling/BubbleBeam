package nl.tudelft.ti2206.network;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

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
		
		client.connect();
		
		verify(host).connect();
		verify(client).connect();
		
		try {
			verify(client, times(0)).acceptPacket(any());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		host.start();
		client.start();
		
		sleep(100);
		
		try {
			verify(client, times(1)).acceptPacket(any());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Packet p = new Packet.CannonRotate(1.65f);
		host.sendPacket(p);
		
		sleep(100);
		
		try {
			verify(client, times(2)).acceptPacket(any());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
