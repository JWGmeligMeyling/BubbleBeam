package nl.tudelft.ti2206.network.packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	default void write(final ObjectOutputStream outputStream) throws IOException {
		try {
			synchronized(outputStream) {
				outputStream.reset();
				outputStream.writeObject(this);
			}
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
	}
	
	static Packet read(final ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
		synchronized(inputStream) {
			return (Packet) inputStream.readObject();
		}
	}
	
}
