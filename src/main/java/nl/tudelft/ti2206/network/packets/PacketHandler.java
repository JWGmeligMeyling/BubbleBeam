package nl.tudelft.ti2206.network.packets;

import java.util.Set;

import nl.tudelft.ti2206.network.packets.Packet.PoppedPacket;

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
public abstract class PacketHandler<P extends Packet> {

	private static final Logger log = LoggerFactory.getLogger(PacketHandler.class);
	
	protected Set<PacketListener<P>> packetListeners = Sets.newHashSet();
	
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
	
	public abstract void notifyObservers(P packet);
	
	/**
	 * The {@code CannonRotate} {@link PacketHandler} handles the
	 * {@link CannonRotate} {@link Packet}s.
	 * 
	 * @author Sam Smulders
	 */
	public static class CannonRotate extends PacketHandler<Packet.CannonRotate> {
		
		@Override
		public void notifyObservers(Packet.CannonRotate packet) {
			packetListeners.forEach(listener -> listener.update(packet));
		}
		
	}
	
	/**
	 * The {@code CannonShoot} {@link PacketHandler} handles the
	 * {@link CannonShoot} {@link Packet}s.
	 */
	public static class CannonShoot extends PacketHandler<Packet.CannonShoot> {
		
		@Override
		public void notifyObservers(Packet.CannonShoot packet) {
			log.info("Received packet {}, dispatching to {} listeners", packet,
					packetListeners.size());
			packetListeners.forEach(listener -> listener.update(packet));
		}
		
	}
	
	/**
	 * The {@code BubbleMeshSync} {@link PacketHandler} handles the
	 * {@link BubbleMeshSync} {@link Packet}s.
	 */
	public static class BubbleMeshSync extends PacketHandler<Packet.BubbleMeshSync> {
		
		@Override
		public void notifyObservers(Packet.BubbleMeshSync packet) {
			log.info("Received packet {}, dispatching to {} listeners", packet,
					packetListeners.size());
			packetListeners.forEach(listener -> listener.update(packet));
		}
		
	}
	
	/**
	 * The {@code LoadNewBubble} {@link PacketHandler} handles the
	 * {@link LoadNewBubble} {@link Packet}s.
	 */
	public static class LoadNewBubble extends PacketHandler<Packet.AmmoPacket> {
		
		@Override
		public void notifyObservers(Packet.AmmoPacket packet) {
			log.info("Received packet {}, dispatching to {} listeners", packet,
					packetListeners.size());
			packetListeners.forEach(listener -> listener.update(packet));
		}
		
	}
	public static class PoppedHandler extends PacketHandler<Packet.PoppedPacket>{

		@Override
		public void notifyObservers(PoppedPacket packet) {
			log.info("Recieved packet {}, dispatching to {} listeners",packet,packetListeners.size());
			packetListeners.forEach(listener -> listener.update(packet));
		}
		
	}
	
}
