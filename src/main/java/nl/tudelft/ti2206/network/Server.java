package nl.tudelft.ti2206.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
	
	public ArrayList<Client> clientList = new ArrayList<Client>();
	public final int port;
	private boolean acceptNewClients;
	
	public Server(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		while (acceptNewClients) {
			Socket socket;
			try {
				socket = serverSocket.accept();
				// Client client = new Client(socket);
				// new Thread(client).start();
				/*
				 * TODO Requires a way to remove the clients from the server
				 * after disconnecting..
				 */
				// clientList.add(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
