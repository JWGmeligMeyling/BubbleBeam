package nl.tudelft.ti2206.cannon;

import java.util.Observable;

import nl.tudelft.util.Vector2f;

public class CannonModel extends Observable {
	
	private double angle = Math.PI / 2;
	private Vector2f direction = new Vector2f(0f, 0f);
	private boolean loaded = true; // TODO make this false till game starts?

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

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
}
