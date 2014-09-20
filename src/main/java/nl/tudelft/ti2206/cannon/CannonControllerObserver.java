package nl.tudelft.ti2206.cannon;

import nl.tudelft.util.Vector2f;

public interface CannonControllerObserver {
	public void cannonRotate(double direction);
	public void cannonShoot(Vector2f direction);
}
