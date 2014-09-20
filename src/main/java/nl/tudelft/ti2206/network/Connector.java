package nl.tudelft.ti2206.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
	
	protected DataOutputStream out;
	protected DataInputStream in;
	protected Socket socket;
	protected PacketHandlerCollection packetHandlerCollection = new PacketHandlerCollection();
	protected int port;
	
	private boolean stop;
	
	public Connector(int port) {
		this.port = port;
	}

	protected Packet readPacket() {
		try {
			byte id = in.readByte();
			return PacketFactory.readPacket(id, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
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
	
	public void endConnection(){
		stop = true;
	}
	
	public PacketHandlerCollection getPacketHandlerCollection(){
		return packetHandlerCollection;
	}
	
	@Override
	public void run() {
		connect();
		stop = false;
		while (!stop) {
			acceptPacket();
		}
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract void connect();
}
