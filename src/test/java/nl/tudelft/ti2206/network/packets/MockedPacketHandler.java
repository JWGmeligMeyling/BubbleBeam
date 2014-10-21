package nl.tudelft.ti2206.network.packets;

import com.google.common.collect.Iterables;

public class MockedPacketHandler extends PacketHandler {

	public <T extends PacketListener> T getListenerOfType(Class<T> clasz) {
		return Iterables.filter(listeners, clasz).iterator().next();
	}

}
