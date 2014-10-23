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
 * @author Sam Smulders
 * @author Jan-Willem Gmelig Meyling
 */
public interface Packet extends Serializable {
	
	static final Logger log = LoggerFactory.getLogger(Packet.class);
	
	/**
	 * Write a packet to the stream
	 * 
	 * @param outputStream
	 *            {@link ObjectOutputStream} to write this {@code Packet} to
	 * @throws IOException
	 *             Any exception thrown by the underlying OutputStream
	 */
	default void write(final ObjectOutputStream outputStream) throws IOException {
		try {
			synchronized(outputStream) {
				outputStream.writeObject(this);
			}
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
	}
	
	/**
	 * Read a {@link Packet} from an {@link ObjectInputStream}
	 * 
	 * @param inputStream
	 *            {@code ObjectInputStream} to read from
	 * @return the {@code Packet} read
	 * @throws ClassNotFoundException
	 *             If the object from the stream could not be deserialized
	 * @throws IOException
	 *             Any exception thrown by the underlying InputStream
	 */
	static Packet read(final ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
		synchronized(inputStream) {
			return (Packet) inputStream.readObject();
		}
	}
	
}
