package nl.tudelft.ti2206.network.packets;

import java.util.ArrayList;

/**
 * The PacketHandler is a 'Subject' which PacketListeners can observe. For each
 * Packet type there should be a PacketHandler class and PacketListener
 * interface.
 * 
 * The PacketHandlers are responsible for notifying registered
 * observers/listeners about specific Packages.
 * 
 * @author Sam Smulders
 */
public abstract class PacketHandler<P extends Packet> {
	protected ArrayList<PacketListener<P>> packetListeners = new ArrayList<PacketListener<P>>();
	
	public final void registerObserver(PacketListener<P> observer) {
		packetListeners.add(observer);
	}
	
	public final void removeObserver(PacketListener<P> observer) {
		packetListeners.remove(observer);
	}
	
	public static class CannonRotate extends PacketHandler<Packet.CannonRotate> {
		public void notifyObservers(Packet.CannonRotate packet) {
			packetListeners.forEach(listener -> listener.update(packet));
		}
	}
	
	public static class CannonShoot extends PacketHandler<Packet.CannonShoot> {
		public void notifyObservers(Packet.CannonShoot packet) {
			packetListeners.forEach(listener -> listener.update(packet));
		}
	}
	
	public static class BubbleMeshSync extends PacketHandler<Packet.BubbleMeshSync> {
		public void notifyObservers(Packet.BubbleMeshSync packet) {
			packetListeners.forEach(listener -> listener.update(packet));
		}
	}
	
	public static class LoadNewBubble extends PacketHandler<Packet.LoadNewBubble> {
		public void notifyObservers(Packet.LoadNewBubble packet) {
			packetListeners.forEach(listener -> listener.update(packet));
		}
	}
}
