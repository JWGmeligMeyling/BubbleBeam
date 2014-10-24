package nl.tudelft.ti2206.cannon;

import java.util.Observable;
import java.util.function.Consumer;

import nl.tudelft.util.AbstractEventTarget;
import nl.tudelft.util.EventTarget;
import nl.tudelft.util.Vector2f;
import nl.tudelft.ti2206.game.event.CannonListener;

/**
 * The {@code CannonModel} is the model for the {@link CannonController}
 * 
 * @author Jan-Willem Gmelig Meyling
 * @author Leon Hoek
 *
 */
public class CannonModel extends Observable implements EventTarget<CannonListener> {
	
	private AbstractEventTarget<CannonListener> eventTarget = new AbstractEventTarget<>();
	
	private CannonState cannonState = new CannonShootState();
	private double angle = Math.PI / 2;
	private Vector2f direction = new Vector2f(0f, -1f);
	
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
	public void addEventListener(CannonListener listener) {
		eventTarget.addEventListener(listener);
	}

	@Override
	public void removeEventListener(CannonListener listener) {
		eventTarget.removeEventListener(listener);
	}
	
	@Override
	public void trigger(Consumer<CannonListener> action) {
		eventTarget.trigger(action);
	}

}
