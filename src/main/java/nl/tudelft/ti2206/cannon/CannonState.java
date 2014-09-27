package nl.tudelft.ti2206.cannon;

/**
 * {@code CannonState} is the state of the Cannon and can be either
 * {@link CannonLoadedState} or {@link CannonShootState}
 * 
 * @author Jan-Willem Gmelig Meyling
 * @author Leon Hoek
 */
public interface CannonState {
	
	/**
	 * Shoot the cannon
	 */
	void shoot();
	
}
