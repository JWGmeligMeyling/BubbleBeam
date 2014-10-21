package nl.tudelft.ti2206.network.packets;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * The {@code PacketHandler} is a 'Subject' which {@link PacketListeners} can
 * observe. For each {@link Packet} type there should be a {@link PacketHandler}
 * class and {@link PacketListener} interface.
 * 
 * The PacketHandlers are responsible for notifying registered
 * observers/listeners about specific Packages.
 * 
 * @author Sam Smulders
 */
public class PacketHandler<P extends Packet> {

	private static final Logger log = LoggerFactory.getLogger(PacketHandler.class);
	
	protected Set<PacketListener<P>> packetListeners = Sets.newHashSet();
	
	private final boolean doLog;
	
	public PacketHandler() {
		this(false);
	}
	
	public PacketHandler(boolean doLog) {
		this.doLog = doLog;
	}
	
	/**
	 * Adds an observer to the registered observer list.
	 * 
	 * @param observer
	 *            to be added to the registered observer list.
	 */
	public final void registerObserver(PacketListener<P> observer) {
		packetListeners.add(observer);
	}
	
	/**
	 * Remove an observer from the registered observer list.
	 * 
	 * @param observer
	 *            to be removed
	 */
	public final void removeObserver(PacketListener<P> observer) {
		packetListeners.remove(observer);
	}
	
	/**
	 * 
	 * @param packet
	 */
	public void notifyObservers(P packet) {
		if(doLog) {
			log.info("Received packet {}, dispatching to {} listeners", packet,
					packetListeners.size());
		}
		packetListeners.forEach(listener -> listener.update(packet));
	}
	
}
