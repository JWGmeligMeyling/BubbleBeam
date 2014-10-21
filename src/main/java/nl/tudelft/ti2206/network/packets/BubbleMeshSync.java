package nl.tudelft.ti2206.network.packets;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;

/**
 * The {@code BubbleMeshSync} {@link Packet} is used to send and receive the
 * {@link BubbleMesh}.
 * 
 * @author Sam Smulders
 */
public class BubbleMeshSync implements Packet {

	private static final long serialVersionUID = -8341409595602559487L;

	public final BubbleMesh bubbleMesh;
	
	public BubbleMeshSync(final BubbleMesh bubbleMesh) {
		this.bubbleMesh = bubbleMesh;
	}
	
	@Override
	public void notify(PacketHandler packetHandlerCollection) {
		packetHandlerCollection.notify(this);
	}
}