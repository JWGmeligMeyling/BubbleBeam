package nl.tudelft.util;

import java.awt.Point;

/**
 * Vector class for vector calculations
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class Vector2f extends org.lwjgl.util.vector.Vector2f {
	
	private static final long serialVersionUID = -7121395633617073454L;
	
	/**
	 * Construct a new vector
	 */
	public Vector2f() {
		this(0.0f, 0.0f);
	}
	
	/**
	 * Construct a new vector
	 * 
	 * @param point
	 *            {@link Point} containing the values for this vector
	 */
	public Vector2f(final Point point) {
		this(point.x, point.y);
	}
	
	/**
	 * Construct a new vector
	 * 
	 * @param a
	 *            first value
	 * @param b
	 *            second value
	 */
	public Vector2f(final float a, final float b) {
		super(a, b);
	}
	
	/**
	 * Copy another vector
	 * 
	 * @param vector
	 *            values for this vector
	 */
	public Vector2f(Vector2f vector) {
		this.x = vector.x;
		this.y = vector.y;
	}
	
	/**
	 * Add another vector to this vector
	 * 
	 * @param other
	 *            another vector
	 * @return new vector containing the sum between the two vectors
	 */
	public Vector2f add(final Vector2f other) {
		return new Vector2f(this.x + other.x, this.y + other.y);
	}
	
	/**
	 * Subtract another vector from this vector
	 * 
	 * @param other
	 *            another vector
	 * @return new vector containing the difference between the two vectors
	 */
	public Vector2f subtract(final Vector2f other) {
		return new Vector2f(this.x - other.x, this.y - other.y);
	}
	
	/**
	 * Multiply this vector by another vector
	 * 
	 * @param other
	 *            another vector
	 * @return new vector
	 */
	public Vector2f multiply(final Vector2f other) {
		return new Vector2f(this.x * other.x, this.y * other.y);
	}
	
	/**
	 * Multiply this vector by a scalar
	 * 
	 * @param scalar
	 *            scalar to multiply with
	 * @return new vector
	 */
	public Vector2f multiply(final float scalar) {
		return new Vector2f(this.x * scalar, this.y * scalar);
	}
	
	/**
	 * Divide this vector with another vector
	 * 
	 * @param other
	 *            another vector
	 * @return new vector
	 */
	public Vector2f divide(final Vector2f other) {
		return new Vector2f(this.x / other.x, this.y / other.y);
	}
	
	/**
	 * Divide this vector by a scaler
	 * 
	 * @param scalar
	 *            scalar to divide with
	 * @return new vector
	 */
	public Vector2f divide(final float scalar) {
		return new Vector2f(this.x / scalar, this.y / scalar);
	}
	
	/**
	 * Normalize this vector
	 * 
	 * @return this vector
	 */
	public Vector2f normalize() {
		super.normalise();
		return this;
	}
	
	/**
	 * Convert this vector to a point
	 * @return a point with the values of this vector
	 */
	public Point toPoint() {
		return new Point((int) this.x, (int) this.y);
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof Vector2f && (this.x == ((Vector2f) other).x) && (this.y == ((Vector2f) other).y));
	}
}
