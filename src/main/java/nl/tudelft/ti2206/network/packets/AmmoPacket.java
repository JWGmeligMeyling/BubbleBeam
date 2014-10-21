package nl.tudelft.ti2206.network.packets;

import nl.tudelft.ti2206.bubbles.Bubble;

/**
 * The {@code LoadNewBubble} {@link Packet} is used to send and receive the
 * colour of new bubbles, to synchronise them with the opponent.
 * 
 * @author Sam Smulders
 */
public class AmmoPacket implements Packet {

	private static final long serialVersionUID = -7036909370582903656L;

	public final Bubble loadedBubble, nextBubble;
	
	public AmmoPacket(Bubble loadedBubble, Bubble nextBubble) {
		this.loadedBubble = loadedBubble;
		this.nextBubble = nextBubble;
	}
	
	@Override
	public void notify(PacketHandler packetHandlerCollection) {
		packetHandlerCollection.notify(this);
	}
}