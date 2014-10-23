package nl.tudelft.ti2206.network.packets;

import java.util.EventObject;

public class EventPacket implements Packet {

	private static final long serialVersionUID = 7800905928983574936L;
	protected final EventObject data;
	
	public EventPacket(EventObject data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EventPacket)) {
			return false;
		}
		EventPacket other = (EventPacket) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		return true;
	}

}
