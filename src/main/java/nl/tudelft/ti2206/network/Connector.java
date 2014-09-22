package nl.tudelft.ti2206.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.PacketFactory;
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
	
	public final static int PORT = 8989;
	
	protected DataOutputStream out;
	protected DataInputStream in;
	protected Socket socket;
	protected PacketHandlerCollection packetHandlerCollection = new PacketHandlerCollection();
	
	protected boolean ready = false;
	
	/**
	 * Connect the connector by using the {@code connect()} method and then
	 * accepting incoming packets as long the connector is ready.
	 */
	@Override
	public void run() {
		while (ready) {
			acceptPacket();
		}
		try {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read an incoming packet.
	 * 
	 * @return the packet
	 */
	protected Packet readPacket() {
		try {
			byte id = in.readByte();
			return PacketFactory.readPacket(id, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Send a packet over the DataOutputStream.
	 * 
	 * @param packet
	 */
	public void sendPacket(Packet packet) {
		try {
			packet.send(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void acceptPacket() {
		readPacket().notify(packetHandlerCollection);
	}
	
	public void endConnection() {
		ready = false;
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public PacketHandlerCollection getPacketHandlerCollection() {
		return packetHandlerCollection;
	}
	
	/**
	 * Create a new {@link Thread} to run this {@link Connector} object with.
	 */
	public void start() {
		if (ready) {
			new Thread(this).start();
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
}
