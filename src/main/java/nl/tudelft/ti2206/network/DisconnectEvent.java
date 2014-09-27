package nl.tudelft.ti2206.network;

import java.util.EventListener;

public interface DisconnectEvent extends EventListener {

	void clientDisconnected();
	
}
