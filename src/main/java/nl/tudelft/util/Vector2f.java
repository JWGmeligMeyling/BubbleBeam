package nl.tudelft.util;

import java.awt.Point;

public class Vector2f extends org.lwjgl.util.vector.Vector2f {

	private static final long serialVersionUID = -7121395633617073454L;

	public Vector2f() {
		this(0.0f, 0.0f);
	}
	
	public Vector2f(final Point point) {
		this(point.x, point.y);
	}
	
	public Vector2f(final float a, final float b) {
		super(a, b);
	}
	
	public Vector2f add(final Vector2f other) {
		return new Vector2f(this.x + other.x, this.y + other.y);
	}
	
	public Vector2f subtract(final Vector2f other) {
		return new Vector2f(this.x - other.x, this.y - other.y);
	}
	
	public Vector2f multiply(final Vector2f other) {
		return new Vector2f(this.x * other.x, this.y * other.y);
	}
	
	public Vector2f multiply(final float scalar) {
		return new Vector2f(this.x * scalar, this.y * scalar);
	}
	
	public Vector2f divide(final Vector2f other) {
		return new Vector2f(this.x / other.x, this.y / other.y);
	}
	
	public Vector2f divide(final float scalar) {
		return new Vector2f(this.x / scalar, this.y / scalar);
	}

	public Vector2f normalize() {
		super.normalise();
		return this;
	}

}
