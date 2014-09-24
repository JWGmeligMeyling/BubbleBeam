package nl.tudelft.ti2206.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code Host} is a {@link Connector} who listens to a given port and waits
 * for a connection request to establish a connection.
 * 
 * @author Sam Smulders
 */
public class Host extends Connector {

	private static final Logger log = LoggerFactory.getLogger(Host.class);

	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	@Override
	public void connect() {
		try {
			log.info("Setting up server connection at port {}", PORT);
			
			serverSocket = new ServerSocket(PORT);
			socket = serverSocket.accept();
			
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
			ready = true;
			
		}  catch (BindException e) {
			log.warn(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	

	@Override
	protected Socket getSocket() {
		return socket;
	}

	@Override
	protected ObjectInputStream getInputStream() {
		return in;
	}

	@Override
	protected ObjectOutputStream getOutputStream() {
		return out;
	}
	
	@Override
	public void endConnection() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			log.debug(e.getMessage(), e);
		}
	}
	
}
