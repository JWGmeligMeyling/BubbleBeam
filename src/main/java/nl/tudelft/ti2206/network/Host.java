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
	protected void connect() {
		try {
			socket = new ServerSocket(PORT).accept();
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
