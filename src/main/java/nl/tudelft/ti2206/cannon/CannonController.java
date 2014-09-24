package nl.tudelft.ti2206.cannon;

import nl.tudelft.ti2206.cannon.CannonListener.CannonRotateListener;
import nl.tudelft.ti2206.cannon.CannonListener.CannonShootListener;
import nl.tudelft.ti2206.util.mvc.Controller;
import nl.tudelft.ti2206.util.mvc.EventTarget;
import nl.tudelft.util.Vector2f;

public interface CannonController extends Controller<CannonModel>, EventTarget<CannonListener> {
	
	public static final double MIN_ANGLE = Math.PI / 10;
	public static final double MIN_DIRECTION_Y = Math.sin(MIN_ANGLE);
	public static final double MIN_DIRECTION_X = Math.cos(MIN_ANGLE);
	
	void load();
	
	void shoot();
	
	default void setAngle(final Vector2f direction) {
		final CannonModel model = this.getModel();
		
		if (direction.y > -MIN_DIRECTION_Y) {
			direction.x = (float) (MIN_DIRECTION_X * Math.signum(direction.x));
			direction.y = (float) -MIN_DIRECTION_Y;
		}
		
		model.setDirection(direction);
		model.setAngle(Math.atan(direction.x / direction.y) + Math.PI / 2);
		model.notifyObservers();
	}
	
	default void addCannonShootListener(final CannonShootListener listener) {
		this.addEventListener(listener);
	}

	default void addCannonRotateListener(final CannonRotateListener listener) {
		this.addEventListener(listener);
	}
	
}