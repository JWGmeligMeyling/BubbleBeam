package nl.tudelft.ti2206.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class Host extends Connector implements Runnable {
	
	public Host(int port) {
		super(port);
	}
	
	@Override
	protected void connect() {
		try {
			socket = new ServerSocket(port).accept();
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
