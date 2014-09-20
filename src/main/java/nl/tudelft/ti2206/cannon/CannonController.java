package nl.tudelft.ti2206.cannon;

import java.util.ArrayList;

import nl.tudelft.util.Vector2f;


public abstract class CannonController {
	
	protected ArrayList<CannonControllerObserver> observers = new ArrayList<CannonControllerObserver>();
	
	public final void registerObserver(CannonControllerObserver observer) {
		observers.add(observer);
	}
	
	public final void removeObserver(CannonControllerObserver observer) {
		observers.remove(observer);
	}
	
	public void notifyObserversRotate(double rotation) {
		observers.forEach(observer -> observer.cannonRotate(rotation));
	}

	public void notifyObserversShoot(Vector2f direction) {
		observers.forEach(observer -> observer.cannonShoot(direction));
	}
}
