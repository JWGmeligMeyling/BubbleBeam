package nl.tudelft.ti2206.cannon;

import java.util.EventListener;

import nl.tudelft.util.Vector2f;

public interface CannonShootListener extends EventListener {

	void shoot(Vector2f direction);

}
