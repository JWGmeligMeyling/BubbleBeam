package nl.tudelft.ti2206.network.test;

import nl.tudelft.ti2206.network.Host;

public class HostTest2 {
	public static void main(String[] s){
		new Thread(new Host(97322)).start();
	}
}
