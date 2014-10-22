package nl.tudelft.ti2206.network.packets;

import java.util.EventObject;

public class EventPacket implements Packet {

	private static final long serialVersionUID = 7800905928983574936L;
	protected final EventObject data;
	
	public EventPacket(EventObject data) {
		this.data = data;
	}

}
