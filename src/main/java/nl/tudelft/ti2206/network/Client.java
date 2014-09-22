package nl.tudelft.ti2206.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The {@code Client} is a {@link Connector} who searches for a host with the
 * given address and port to establish a connection.
 * 
 * @author Sam Smulders
 */
public class Client extends Connector {
	public final String ip;
	
	
	public Client(String ip) {
		this.ip = ip;
	}
	
	@Override
	public void connect() {
		try {
			socket = new Socket(ip, PORT);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			ready = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
