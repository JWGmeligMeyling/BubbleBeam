package nl.tudelft.ti2206.network.packets;

/**
 * This is one of four classes needed to send an interger over the network
 * @author Liam Clark
 *
 */
public class PoppedPacket implements Packet {

	private static final long serialVersionUID = -9118488904921915831L;
	protected final int amount;
	
	public PoppedPacket(int amount) {
		this.amount = amount;
	}
	
	public int getAmount(){
		return amount;
	}

	@Override
	public void notify(PacketHandler packetHandlerCollection) {
			packetHandlerCollection.notify(this);
	}
	
}