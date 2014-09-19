package nl.tudelft.ti2206.network.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest {
	public static void main(String[] e) {
		Socket client = null;
		PrintWriter out = null;
		BufferedReader in = null;
		// while (client == null) {
		try {
			client = new Socket("127.0.0.1", 8989);
		} catch (IOException e1) {
		}
		// }
		try {
			out = new PrintWriter(client.getOutputStream());
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		out.write("hoi");
		System.out.println("Connection made");
		while (true) {
			String s;
			System.out.println("waiting for hello");
			try {
				s = in.readLine();
				if (s != null){
					System.out.println(s);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(33);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
