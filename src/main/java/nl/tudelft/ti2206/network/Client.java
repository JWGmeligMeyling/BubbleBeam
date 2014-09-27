package nl.tudelft.ti2206.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code Client} is a {@link Connector} who searches for a host with the
 * given address and port to establish a connection.
 * 
 * @author Sam Smulders
 */
public class Client extends Connector {
	public final String ip;
	
	private static final Logger log = LoggerFactory.getLogger(Client.class);
	
	public Client(String ip) {
		this.ip = ip;
	}
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	@Override
	public void connect() {
		try {
			log.info("Setting up client connection at address {}:{}", ip, PORT);
			socket = new Socket(ip, PORT);
			
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			ready = true;
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
	public void endConnection(){
		super.endConnection();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.debug(e.getMessage());
		}
	}
}
