package nl.tudelft.ti2206.network.packet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import nl.tudelft.ti2206.bubbles.BubbleMesh.BubbleMeshImpl;
import nl.tudelft.ti2206.bubbles.BubbleMesh.BubbleMeshParser;
import nl.tudelft.ti2206.network.Client;
import nl.tudelft.ti2206.network.Host;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.Packet.CannonRotate;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.util.Vector2f;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class PacketTest {
	
	private Host host;
	private Client client;
	private Thread hostThread;
	private Thread clientThread;
	
	private Packet receivedPacket = null;
	
	@Before
	public void setUp() throws Exception {
		host = spy(new Host());
		client = spy(new Client("127.0.0.1"));
		new Thread(new Runnable() {
			@Override
			public void run() {
				host.connect();
			}
		}).start();
		
		client.connect();
		
		hostThread = new Thread(host);
		clientThread = new Thread(client);
		
		hostThread.start();
		clientThread.start();
	}
	
	@After
	public void tearDown() throws Exception {
		host.endConnection();
		client.endConnection();
		
		/*hostThread.interrupt();
		clientThread.interrupt();*/
		
		receivedPacket = null;
		
		/*host = null;
		client = null;*/
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
		assertEquals(((Packet.CannonShoot) receivedPacket).direction, p.direction);
	}
	
	@Test
	public void bubbleMeshSyncTest() {
		BubbleMeshParser parser = new BubbleMeshParser(Lists.newArrayList("xxxxxxxxxx",
				"xxxxxxxxxx", "xxxxxxxxxx", "xxxxxxxxxx"));
		BubbleMeshImpl mesh = parser.parse();
		Packet.BubbleMeshSync p = new Packet.BubbleMeshSync(mesh);
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
		assertEquals(((Packet.BubbleMeshSync) receivedPacket).bubbleMesh, p.bubbleMesh);
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
