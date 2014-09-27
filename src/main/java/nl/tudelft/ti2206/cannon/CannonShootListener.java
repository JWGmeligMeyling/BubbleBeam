package nl.tudelft.ti2206.cannon;

import java.util.EventListener;

import nl.tudelft.util.Vector2f;

/**
 * A CannonShoot listener listens for Cannon shoot events
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface CannonShootListener extends EventListener {

	/**
	 * Function called when the {@link Cannon} shoots
	 * @param direction
	 * 		Direction in which the cannon shoots
	 */
	void shoot(Vector2f direction);

}
