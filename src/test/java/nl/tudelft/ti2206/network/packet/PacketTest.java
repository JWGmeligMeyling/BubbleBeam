package nl.tudelft.ti2206.network.packet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.Packet.AmmoPacket;
import nl.tudelft.ti2206.network.packets.Packet.CannonRotate;
import nl.tudelft.ti2206.network.packets.Packet.CannonShoot;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.util.Vector2f;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PacketTest {

	
	public final static int PORT = 8989;
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
	
	private Connector host;
	private Connector client;
	
	private Packet receivedPacket = null;
	
	@Before
	public void setUp() throws IOException, InterruptedException, ExecutionException {
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
		
		host = hostFuture.get();
		client = clientFuture.get();
		
		executor.execute(host);
		executor.execute(client);
		serverSocket.close();
	}
	
	@After
	public void tearDown() throws IOException {
		client.close();
		host.close();
		receivedPacket = null;
		host = null;
		client = null;
	}
	
	@Test
	public void cannonRotateTest() {
		Packet.CannonRotate p = new Packet.CannonRotate(1.23f);
		PacketListener<CannonRotate> packetHandler = new PacketListener<CannonRotate>() {
			
			@Override
			public void update(CannonRotate packet) {
				receivedPacket = packet;
			}
		};
		host.getPacketHandlerCollection().registerCannonRotateHandler(packetHandler);
		client.sendPacket(p);
		sleep(100);
		assertNotNull(receivedPacket);
		assertTrue(((Packet.CannonRotate) receivedPacket).rotation == p.rotation);
	}
	
	@Test
	public void cannonShootTest() {
		Packet.CannonShoot p = new Packet.CannonShoot(new Vector2f(1f, 0f));
		PacketListener<CannonShoot> packetHandler = new PacketListener<CannonShoot>() {
			@Override
			public void update(CannonShoot packet) {
				receivedPacket = packet;
			}
		};
		host.getPacketHandlerCollection().registerCannonShootHandler(packetHandler);
		client.sendPacket(p);
		sleep(100);
		
		assertNotNull(receivedPacket);
		assertTrue(((Packet.CannonShoot) receivedPacket).direction.equals(p.direction));
	}
	
	// TODO: Write Bubble equals method to execute test.
	// @Test
	// public void bubbleMeshSyncTest() {
	// BubbleMeshParser parser = new
	// BubbleMeshParser(Lists.newArrayList("xxxxxxxxxx",
	// "xxxxxxxxxx", "xxxxxxxxxx", "xxxxxxxxxx"));
	// BubbleMeshImpl mesh = parser.parse();
	// Packet.BubbleMeshSync p = new Packet.BubbleMeshSync(mesh);
	// PacketListener<BubbleMeshSync> packetHandler = new
	// PacketListener<BubbleMeshSync>() {
	// @Override
	// public void update(BubbleMeshSync packet) {
	// receivedPacket = packet;
	// }
	// };
	// host.getPacketHandlerCollection().registerBubbleMeshSyncListener(packetHandler);
	// client.sendPacket(p);
	// sleep(100);
	// assertNotNull(receivedPacket);
	// assertEquals(((Packet.BubbleMeshSync) receivedPacket).bubbleMesh,
	// p.bubbleMesh);
	// }
	
	@Test
	public void ammoPacketTest() {
		Color ammo1 = Color.RED;
		Color ammo2 = Color.BLUE;
		Packet.AmmoPacket p = new Packet.AmmoPacket(ammo1, ammo2);
		PacketListener<AmmoPacket> packetHandler = new PacketListener<AmmoPacket>() {
			@Override
			public void update(AmmoPacket packet) {
				receivedPacket = packet;
			}
		};
		host.getPacketHandlerCollection().registerLoadNewBubbleListener(packetHandler);
		client.sendPacket(new Packet.CannonShoot(new Vector2f(0, 1f)));
		client.sendPacket(p);
		sleep(100);
		
		assertNotNull(receivedPacket);
		assertTrue(((Packet.AmmoPacket) receivedPacket).loadedBubble.equals(ammo1));
		assertTrue(((Packet.AmmoPacket) receivedPacket).nextBubble.equals(ammo2));
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
