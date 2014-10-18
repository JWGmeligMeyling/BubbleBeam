package nl.tudelft.ti2206.cannon;

import nl.tudelft.ti2206.game.GameEventListener;
import nl.tudelft.util.Vector2f;

/**
 * A CannonShoot listener listens for Cannon shoot events
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface CannonShootListener extends GameEventListener {

	/**
	 * Function called when the {@link Cannon} shoots
	 * @param direction
	 * 		Direction in which the cannon shoots
	 */
	void shoot(Vector2f direction);

}
