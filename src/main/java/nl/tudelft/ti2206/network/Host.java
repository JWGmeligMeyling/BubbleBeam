package nl.tudelft.ti2206.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * The {@code Host} is a {@link Connector} who listens to a given port and waits
 * for a connection request to establish a connection.
 * 
 * @author Sam Smulders
 */
public class Host extends Connector {
	@Override
	public void connect() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			socket = serverSocket.accept();
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			serverSocket.close();
			ready = true;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
