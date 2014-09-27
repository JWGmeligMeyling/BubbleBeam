package nl.tudelft.ti2206.network;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import nl.tudelft.ti2206.network.packets.Packet;

import org.junit.Test;

public class ConnectorTest {
	
	public final static int PORT = 8989;
	
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
	
	@Test
	public void connectorTest() throws IOException, InterruptedException, ExecutionException {
		ServerSocket serverSocket = new ServerSocket(PORT);
		
		Future<Connector> hostFuture = executor.submit(() -> {
			Socket socket = serverSocket.accept();
			Connector host = new Connector(socket);
			return spy(host);
		});
		
		Future<Connector> clientFuture = executor.submit(() -> {
			Socket socket = new Socket("127.0.0.1", PORT);
			Connector client = new Connector(socket);
			return spy(client);
		});
		
		Connector host = hostFuture.get();
		Connector client = clientFuture.get();
		
		executor.execute(host);
		executor.execute(client);
		
		verify(client, times(0)).acceptPacket(any());
		
		sleep(100);
		
		verify(client, times(1)).acceptPacket(any());
		
		Packet p = new Packet.CannonRotate(1.65f);
		host.sendPacket(p);
		sleep(100);
		
		verify(client, times(2)).acceptPacket(any());
		serverSocket.close();
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
