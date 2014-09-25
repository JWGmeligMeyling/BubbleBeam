package nl.tudelft.ti2206.cannon;

import java.util.EventListener;

import nl.tudelft.util.Vector2f;

public interface CannonListener extends EventListener {

	void shoot(Vector2f direction);

	void rotate(double angle);

	public interface CannonShootListener extends CannonListener {

		default void rotate(double angle) {
		};

	}

	public interface CannonRotateListener extends CannonListener {

		default void shoot(Vector2f direction) {
		};

	}

}
