package nl.tudelft.ti2206.network.packets;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
 */
public interface Packet {
	
	/**
	 * Send this packet over a {@link DataOutputStream}.
	 * 
	 * @param out
	 *            the DataOutputStream to send this package over.
	 * @throws IOException
	 */
	public void send(DataOutputStream out) throws IOException;
	
	/**
	 * Notifies the responsible {@Link PacketHandler} about this packet.
	 * 
	 * @param packetHandlerCollection
	 */
	public void notify(PacketHandlerCollection packetHandlerCollection);
	
	/**
	 * The {@code CannonRotate} {@link Packet} is used to send and receive
	 * information about a cannon rotating.
	 * 
	 * @author Sam Smulders
	 */
	public class CannonRotate implements Packet {
		public static final byte PACKET_ID = 0;
		
		public final double rotation;
		
		public CannonRotate(double direction) {
			this.rotation = direction;
		}
		
		public CannonRotate(DataInputStream in) throws IOException {
			this.rotation = in.readDouble();
		}
		
		@Override
		public void send(DataOutputStream out) throws IOException {
			out.writeByte(PACKET_ID);
			out.writeDouble(rotation);
			out.flush();
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
		public static final byte PACKET_ID = 1;
		
		public final Vector2f direction;
		
		public CannonShoot(Vector2f direction) {
			this.direction = new Vector2f(direction);
		}
		
		public CannonShoot(DataInputStream in) throws IOException {
			this.direction = new Vector2f(in.readFloat(), in.readFloat());
		}
		
		@Override
		public void send(DataOutputStream out) throws IOException {
			out.writeByte(PACKET_ID);
			out.writeFloat(direction.x);
			out.writeFloat(direction.y);
			out.flush();
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
		public static final byte PACKET_ID = 2;
		
		public final String parseString;
		
		public BubbleMeshSync(String parseString) {
			this.parseString = parseString;
		}
		
		public BubbleMeshSync(DataInputStream in) throws IOException {
			this.parseString = in.readUTF();
		}
		
		@Override
		public void send(DataOutputStream out) throws IOException {
			out.writeByte(PACKET_ID);
			out.writeUTF(parseString);
			out.flush();
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
	public class LoadNewBubble implements Packet {
		public static final byte PACKET_ID = 3;
		
		public final Color color;
		
		public LoadNewBubble(Color color) {
			this.color = color;
		}
		
		public LoadNewBubble(DataInputStream in) throws IOException {
			this.color = new Color(in.readInt(), in.readInt(), in.readInt());
		}
		
		@Override
		public void send(DataOutputStream out) throws IOException {
			out.writeByte(PACKET_ID);
			out.writeInt(color.getRed());
			out.writeInt(color.getGreen());
			out.writeInt(color.getBlue());
			out.flush();
		}
		
		@Override
		public void notify(PacketHandlerCollection packetHandlerCollection) {
			packetHandlerCollection.notify(this);
		}
	}
}
