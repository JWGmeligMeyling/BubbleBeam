package nl.tudelft.ti2206.network.packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.util.Vector2f;

/**
 * The {@code Packet} classes are responsible for constructing packets to send
 * over an OutputStream out of data given in the constructor, and the Packets
 * are responsible for retrieving the data after receiving over an InputStream.
 * 
 * For each type of data to be send, a new {@code Packet} class should be made.
 * And for each {@code Packet} class there should be an {@link PacketListener}
 * interface to listen with and a {@link PacketHandler} class to register the
 * listeners and notify to from the packet.
 * 
 * Each Packet should have an unique PACKET_ID which should be used to recognise
 * the packet as raw data in the {@link PacketFactory}.
 * 
 * @author Sam Smulders
 * @author Jan-Willem Gmelig Meyling
 */
public interface Packet extends Serializable {
	
	static final Logger log = LoggerFactory.getLogger(Packet.class);
	
	/**
	 * Notifies the responsible {@Link PacketHandler} about this packet.
	 * 
	 * @param packetHandlerCollection
	 */
	void notify(PacketHandlerCollection packetHandlerCollection);
	
	default void write(final ObjectOutputStream outputStream) throws IOException {
		try {
			outputStream.reset();
			outputStream.writeObject(this);
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
	}
	
	static Packet read(final ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
		return (Packet) inputStream.readObject();
	}
	
	/**
	 * The {@code CannonRotate} {@link Packet} is used to send and receive
	 * information about a cannon rotating.
	 * 
	 * @author Sam Smulders
	 */
	public class CannonRotate implements Packet {
		
		private static final long serialVersionUID = -8205066675151884103L;
		
		public final double rotation;
		
		public CannonRotate(double direction) {
			this.rotation = direction;
		}
		
		@Override
		public void notify(PacketHandlerCollection packetHandlerCollection) {
			packetHandlerCollection.notify(this);
		}
	}
	
	/**
	 * The {@code CannonShoot} {@link Packet} is used to send and receive
	 * information about a cannon shooting.
	 * 
	 * @author Sam Smulders
	 */
	public class CannonShoot implements Packet {

		private static final long serialVersionUID = 1546268759069464515L;

		public final Vector2f direction;
		
		public CannonShoot(Vector2f direction) {
			this.direction = new Vector2f(direction);
		}
		
		@Override
		public void notify(PacketHandlerCollection packetHandlerCollection) {
			packetHandlerCollection.notify(this);
		}
	}
	
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
		public void notify(PacketHandlerCollection packetHandlerCollection) {
			packetHandlerCollection.notify(this);
		}
	}
	
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
		public void notify(PacketHandlerCollection packetHandlerCollection) {
			packetHandlerCollection.notify(this);
		}
	}
}
