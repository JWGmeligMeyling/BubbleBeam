package nl.tudelft.ti2206.cannon;

import java.util.Observable;
import java.util.function.Consumer;

import nl.tudelft.util.AbstractEventTarget;
import nl.tudelft.util.EventTarget;
import nl.tudelft.util.Vector2f;

/**
 * The {@code CannonModel} is the model for the {@link CannonController}
 * 
 * @author Jan-Willem Gmelig Meyling
 * @author Leon Hoek
 *
 */
public class CannonModel extends Observable implements EventTarget<CannonShootListener> {
	
	private AbstractEventTarget<CannonShootListener> eventTarget = new AbstractEventTarget<>();
	
	private CannonState cannonState = new CannonShootState();
	private double angle = Math.PI / 2;
	private Vector2f direction = new Vector2f(0f, 0f);
	
	public Vector2f getDirection() {
		return direction;
	}

	public void setDirection(Vector2f direction) {
		if(this.direction != direction) {
			this.direction = direction;
			this.setChanged();
		}
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		if(angle != this.angle) {
			this.angle = angle;
			this.setChanged();
		}
	}

	public CannonState getCannonState() {
		return cannonState;
	}
	
	public void setCannonState(CannonState cannonState) {
		this.cannonState = cannonState; 
	}
	
	@Override
	public void addEventListener(CannonShootListener listener) {
		eventTarget.addEventListener(listener);
	}

	@Override
	public void removeEventListener(CannonShootListener listener) {
		eventTarget.removeEventListener(listener);
	}
	
	@Override
	public void trigger(Consumer<CannonShootListener> action) {
		eventTarget.trigger(action);
	}

	@Override
	public <A extends CannonShootListener> void trigger(Class<A> clasz,
			Consumer<A> action) {
		eventTarget.trigger(clasz, action);
	}
	
}
