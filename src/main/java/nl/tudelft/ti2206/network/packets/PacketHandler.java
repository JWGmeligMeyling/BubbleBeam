package nl.tudelft.ti2206.network.packets;

import java.util.ArrayList;

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
	protected ArrayList<PacketListener<P>> packetListeners = new ArrayList<PacketListener<P>>();
	
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
	 * The {@code CannonRotate} {@link PacketHandler} handles the
	 * {@link CannonRotate} {@link Packet}s.
	 * 
	 * @author Sam Smulders
	 */
	public static class CannonRotate extends PacketHandler<Packet.CannonRotate> {
		public void notifyObservers(Packet.CannonRotate packet) {
			packetListeners.forEach(listener -> listener.update(packet));
		}
	}
	
	/**
	 * The {@code CannonShoot} {@link PacketHandler} handles the
	 * {@link CannonShoot} {@link Packet}s.
	 * 
	 * @author Sam Smulders
	 */
	public static class CannonShoot extends PacketHandler<Packet.CannonShoot> {
		public void notifyObservers(Packet.CannonShoot packet) {
			packetListeners.forEach(listener -> listener.update(packet));
		}
	}
	
	/**
	 * The {@code BubbleMeshSync} {@link PacketHandler} handles the
	 * {@link BubbleMeshSync} {@link Packet}s.
	 * 
	 * @author Sam Smulders
	 */
	public static class BubbleMeshSync extends PacketHandler<Packet.BubbleMeshSync> {
		public void notifyObservers(Packet.BubbleMeshSync packet) {
			packetListeners.forEach(listener -> listener.update(packet));
		}
	}
	
	/**
	 * The {@code LoadNewBubble} {@link PacketHandler} handles the
	 * {@link LoadNewBubble} {@link Packet}s.
	 * 
	 * @author Sam Smulders
	 */
	public static class LoadNewBubble extends PacketHandler<Packet.LoadNewBubble> {
		public void notifyObservers(Packet.LoadNewBubble packet) {
			packetListeners.forEach(listener -> listener.update(packet));
		}
	}
}
