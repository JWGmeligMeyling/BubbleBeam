package nl.tudelft.ti2206.cannon;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.util.Vector2f;

/**
 * The {@code CannonController} is used to handle input from a source to control
 * the cannon. The {@code CannonController} behaviour can be observed by objects
 * of the {@link CannonControllerObserver}, who can be registered and removed
 * from the CannonController.
 * 
 * @author Sam Smulders
 */
public abstract class CannonController {
	
	private static final Logger log = LoggerFactory.getLogger(CannonController.class);
	
	protected ArrayList<CannonControllerObserver> observers = new ArrayList<CannonControllerObserver>();
	
	/**
	 * Adds an observer to the registered observer list.
	 * 
	 * @param observer
	 *            to be added to the registered observer list.
	 */
	public final void registerObserver(CannonControllerObserver observer) {
		observers.add(observer);
	}
	
	/**
	 * Remove an observer from the registered observer list.
	 * 
	 * @param observer
	 *            to be removed
	 */
	public final void removeObserver(CannonControllerObserver observer) {
		observers.remove(observer);
	}
	
	/**
	 * Notify all the observers on cannon rotation behaviour.
	 * 
	 * @param rotation
	 *            of the cannon
	 */
	protected void notifyObserversRotate(double rotation) {
		observers.forEach(observer -> observer.cannonRotate(rotation));
	}
	
	/**
	 * Notify all the observers on cannon shoot behaviour.
	 * 
	 * @param direction
	 *            of shooting
	 */
	protected void notifyObserversShoot(Vector2f direction) {
		log.info("Cannon shoot in direction {}", direction);
		observers.forEach(observer -> observer.cannonShoot(direction));
	}
}
