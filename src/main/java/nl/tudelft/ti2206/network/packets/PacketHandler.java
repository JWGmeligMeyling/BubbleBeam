package nl.tudelft.ti2206.network.packets;

import nl.tudelft.util.AbstractEventTarget;


/**
 * The {@code PacketHandlerCollection} is responsible for storing all the
 * different {@link PacketHandler}s and guiding the different notification to
 * the desired {@link PacketHandler}.
 * 
 * @author Sam Smulders
 */
public class PacketHandler extends AbstractEventTarget<PacketListener> {
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(CannonRotate packet) {
		listeners.forEach(listener -> listener.receivedCannonRotate(packet));
	}
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(CannonShoot packet) {
		listeners.forEach(listener -> listener.receivedCannonShoot(packet));
	}
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(BubbleMeshSync packet) {
		listeners.forEach(listener -> listener.receivedBubbleMeshSync(packet));
	}
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(AmmoPacket packet) {
		listeners.forEach(listener -> listener.receivedAmmoPacket(packet));
	}

	public void notify(PoppedPacket packet) {
		listeners.forEach(listener -> listener.receivedPoppedPacket(packet));
	}
	
	public void notify(GameModelPacket packet) {
		listeners.forEach(listener -> listener.receivedGameModelPacket(packet));
	}

}
