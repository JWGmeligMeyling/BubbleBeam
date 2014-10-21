package nl.tudelft.ti2206.network.packets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.util.AbstractEventTarget;


/**
 * The {@code PacketHandlerCollection} is responsible for storing all the
 * different {@link PacketHandler}s and guiding the different notification to
 * the desired {@link PacketHandler}.
 * 
 * @author Sam Smulders
 */
public class PacketHandler extends AbstractEventTarget<PacketListener> {
	
	private static final Logger log = LoggerFactory.getLogger(PacketHandler.class);
	
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
		log.info("Received {}", packet);
		listeners.forEach(listener -> listener.receivedCannonShoot(packet));
	}
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(BubbleMeshSync packet) {
		log.info("Received {}", packet);
		listeners.forEach(listener -> listener.receivedBubbleMeshSync(packet));
	}
	
	/**
	 * Notify the desired {@link PacketHandler}
	 * 
	 * @param packet
	 */
	public void notify(AmmoPacket packet) {
		log.info("Received {}", packet);
		listeners.forEach(listener -> listener.receivedAmmoPacket(packet));
	}

	public void notify(PoppedPacket packet) {
		log.info("Received {}", packet);
		listeners.forEach(listener -> listener.receivedPoppedPacket(packet));
	}
	
	public void notify(GameModelPacket packet) {
		log.info("Received {}", packet);
		listeners.forEach(listener -> listener.receivedGameModelPacket(packet));
	}

}
