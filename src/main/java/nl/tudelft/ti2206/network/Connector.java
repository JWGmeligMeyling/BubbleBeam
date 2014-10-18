package nl.tudelft.ti2206.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;
import nl.tudelft.util.AbstractEventTarget;

/**
 * The task of a Connector is to communicate with an other instance of the game.
 * It can send and receive messages.
 * 
 * Extensions of this class should handle the creation of the Socket class.
 *
 * @author Sam_
 */
public class Connector extends AbstractEventTarget<DisconnectEvent> implements
		Runnable, AutoCloseable {

	private static final Logger log = LoggerFactory.getLogger(Connector.class);
	
	public final static int PORT = 8989;
	
	protected PacketHandlerCollection packetHandlerCollection = new PacketHandlerCollection();
	
	protected boolean open = true;
	protected final Socket socket;
	protected final ObjectInputStream in;
	protected final ObjectOutputStream out;
	
	public Connector(final Socket socket) throws IOException {
		this.socket = socket;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.out.flush();
		this.in = new ObjectInputStream(socket.getInputStream());
		
	}
	
	/**
	 * Connect the connector by using the {@code connect()} method and then
	 * accepting incoming packets as long the connector is ready.
	 */
	@Override
	public void run() {
		try(Connector closable = this) {
			log.info("In event loop");
			while (open) {
				acceptPacket(in);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			try {
				close();
			} catch (IOException e1) {
				log.warn(e1.getMessage(), e1);
			}
		}
	}
	
	/**
	 * Read an incoming packet.
	 * 
	 * @return the packet
	 * @throws IOException 
	 */
	protected Packet readPacket(ObjectInputStream inputstream) throws IOException {
		try {
			return Packet.read(inputstream);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Send a packet over the DataOutputStream.
	 * 
	 * @param packet
	 * @throws IOException 
	 */
	public void sendPacket(final Packet packet)  {
		try {
			if (open) {
				packet.write(out);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			try {
				close();
			} catch (IOException e1) {
				log.warn(e1.getMessage(), e1);
			}
		}
	}
	
	protected void acceptPacket(final ObjectInputStream inputstream) throws IOException {
		readPacket(inputstream).notify(packetHandlerCollection);
	}
	
	public PacketHandlerCollection getPacketHandlerCollection() {
		return packetHandlerCollection;
	}
	
	public boolean isReady() {
		return open;
	}
	
	@Override
	public void close() throws IOException {
		listeners.forEach(DisconnectEvent::clientDisconnected);
		log.info("Closing the socket");
		open = false;
		socket.close();
	}
	
}
