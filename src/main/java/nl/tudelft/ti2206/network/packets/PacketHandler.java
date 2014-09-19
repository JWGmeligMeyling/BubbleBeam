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
public abstract class PacketHandler {
	
	protected ArrayList<PacketListener> packetListeners = new ArrayList<PacketListener>();
	
	public final void registerObserver(PacketListener observer) {
		packetListeners.add(observer);
	}
	
	public final void removeObserver(PacketListener observer) {
		packetListeners.remove(observer);
	}
	
	public abstract void notifyObservers(Packet packet);
	
	public static class CannonRotate extends PacketHandler {
		@Override
		public void notifyObservers(Packet packet) {
			for (PacketListener listener : packetListeners) {
				((PacketListener.CannonRotate) listener).update((Packet.CannonRotate) packet);
			}
		}
	}
	
	public static class CannonShoot extends PacketHandler {
		@Override
		public void notifyObservers(Packet packet) {
			for (PacketListener listener : packetListeners) {
				((PacketListener.CannonShoot) listener).update((Packet.CannonShoot) packet);
			}
		}
	}
	
	public static class BubbleMeshSync extends PacketHandler {
		@Override
		public void notifyObservers(Packet packet) {
			for (PacketListener listener : packetListeners) {
				((PacketListener.BubbleMeshSync) listener).update((Packet.BubbleMeshSync) packet);
			}
		}
	}
	
	public static class LoadNewBubble extends PacketHandler {
		@Override
		public void notifyObservers(Packet packet) {
			for (PacketListener listener : packetListeners) {
				((PacketListener.LoadNewBubble) listener).update((Packet.LoadNewBubble) packet);
			}
		}
	}
}
