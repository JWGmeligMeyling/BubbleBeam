package nl.tudelft.ti2206.network.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HostTest {
	public static void main(String[] e) {
		ServerSocket server = null;
		Socket client = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			server = new ServerSocket(8989);
			while (client == null) {
				try {
					Thread.sleep(33);
					client = server.accept();
				} catch (IOException | InterruptedException e2) {
				}
			}
			out = new PrintWriter(client.getOutputStream());
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("Connection made");
		while (true) {
			System.out.println("Saying hello");
			out.println("hello");
			out.flush();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
