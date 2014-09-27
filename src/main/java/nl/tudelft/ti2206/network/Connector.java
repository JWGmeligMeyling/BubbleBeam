package nl.tudelft.ti2206.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.PacketHandlerCollection;

/**
 * The task of a Connector is to communicate with an other instance of the game.
 * It can send and receive messages.
 * 
 * Extensions of this class should handle the creation of the Socket class.
 *
 * @author Sam_
 */
public abstract class Connector implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(Connector.class);
	
	public final static int PORT = 8989;
	
	protected PacketHandlerCollection packetHandlerCollection = new PacketHandlerCollection();
	
	protected boolean ready = false;
	
	/**
	 * Connect the connector by using the {@code connect()} method and then
	 * accepting incoming packets as long the connector is ready.
	 */
	@Override
	public void run() {
		try (Socket socket = getSocket();
			ObjectInputStream inputstream = getInputStream();) {
			
			log.info("In event loop");
			
			while (ready) {
				acceptPacket(inputstream);
			}
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		} finally {
			endConnection();
		}
	}
	
	protected abstract Socket getSocket();
	protected abstract ObjectInputStream getInputStream();
	protected abstract ObjectOutputStream getOutputStream();
	
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
			if (ready) {
				packet.write(getOutputStream());
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			endConnection();
		}
	}
	
	protected void acceptPacket(final ObjectInputStream inputstream) throws IOException {
		readPacket(inputstream).notify(packetHandlerCollection);
	}
	
	public PacketHandlerCollection getPacketHandlerCollection() {
		return packetHandlerCollection;
	}
	
	/**
	 * Create a new {@link Thread} to run this {@link Connector} object with.
	 */
	public void start() {
		if (ready) {
			log.info("Starting event loop");
			new Thread(this).start();
		}
		else {
			log.warn("Connector is not ready yet");
		}
	}
	
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * The method to be called to make a connection with an other
	 * {@link Connector} object.
	 */
	public abstract void connect() throws UnknownHostException, IOException;

	public void endConnection() {
		ready = false;
	}

}
