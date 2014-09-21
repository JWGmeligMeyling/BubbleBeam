package nl.tudelft.ti2206.cannon;

import nl.tudelft.util.Vector2f;

/**
 * The {@code CannonControllerObserver} is used to listen to the behaviour of
 * the {@link CannonController}.
 * 
 * @author Sam Smulders
 */
public interface CannonControllerObserver {
	/**
	 * Receive update about the cannon rotation behaviour.
	 * 
	 * @param direction
	 *            of the cannon.
	 */
	public void cannonRotate(double direction);
	
	/**
	 * Receive update about the cannon shoot behaviour.
	 * 
	 * @param direction
	 *            of the bubble out of the cannon.
	 */
	public void cannonShoot(Vector2f direction);
}
