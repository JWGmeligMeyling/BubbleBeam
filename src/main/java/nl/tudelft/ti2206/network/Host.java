package nl.tudelft.ti2206.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code Host} is a {@link Connector} who listens to a given port and waits
 * for a connection request to establish a connection.
 * 
 * @author Sam Smulders
 */
public class Host extends Connector {
	private ServerSocket serverSocket;

	private static final Logger log = LoggerFactory.getLogger(Host.class);
	
	@Override
	public void connect() {
		try {
			try {
				serverSocket = new ServerSocket(PORT);
			} catch (BindException e) {
				log.debug(e.getMessage());
				return;
			}
			socket = serverSocket.accept();
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			serverSocket.close();
			ready = true;
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Override
	public void endConnection() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// Exception expected
			}
		}
		super.endConnection();
	}
	
}
