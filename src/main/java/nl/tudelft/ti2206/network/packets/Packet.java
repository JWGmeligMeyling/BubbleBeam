package nl.tudelft.ti2206.network.packets;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.tudelft.util.Vector2f;

/**
 * The Packets are responsible for constructing packages to send over an
 * OutputStream out of data given in the constructor, and the Packets are
 * responsible for retrieving the data after receiving over an InputStream.
 * 
 * For each type of data to be send, a new Packet class should be made. And for
 * each Packet class there should be an PacketListener interface to listen with
 * and a PacketHandler class to register the listeners and notify to from the
 * packet.
 * 
 * Each Packet should have an unique PACKET_ID which should be used to recognise
 * the packet as raw data in the PacketFactory.
 * 
 * @author Sam Smulders
 */
public interface Packet {
	public void send(DataOutputStream out) throws IOException;
	
	public void notify(PacketHandlerCollection packetHandlerCollection);
	
	public class CannonRotate implements Packet {
		public static final byte PACKET_ID = 0;
		
		public final Vector2f direction;
		
		public CannonRotate(Vector2f direction) {
			this.direction = new Vector2f(direction);
		}
		
		public CannonRotate(DataInputStream in) throws IOException {
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
	
	public class LoadNewBubble implements Packet {
		public static final byte PACKET_ID = 3;
		
		public final Color color;
		
		public LoadNewBubble(Color color) {
			this.color = color;
		}
		
		public LoadNewBubble(DataInputStream in) throws IOException {
			this.color = new Color(in.readByte(), in.readByte(), in.readByte());
		}
		
		@Override
		public void send(DataOutputStream out) throws IOException {
			out.writeByte(PACKET_ID);
			out.write(color.getRed());
			out.write(color.getGreen());
			out.write(color.getBlue());
			out.flush();
		}
		
		@Override
		public void notify(PacketHandlerCollection packetHandlerCollection) {
			packetHandlerCollection.notify(this);
		}
	}
}
