package nl.tudelft.ti2206.network;

import org.junit.Test;

public class ClientTest {
	private int port = 87432;
	private String local = "127.0.0.1";
	
	@Test
	public void clientConnectTest() {
		Client client = new Client(local, port);
		client.run();
	}
}
