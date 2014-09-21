package nl.tudelft.util;

import java.awt.Point;

/**
 * A vector class that contains the x and y coordinates as two floats
 * @author Jan-Willem
 *
 */
public class Vector2f extends org.lwjgl.util.vector.Vector2f {

	private static final long serialVersionUID = -7121395633617073454L;

	public Vector2f() {
		this(0.0f, 0.0f);
	}
	
	/**
	 * construct the vector using a Point
	 * @param point
	 */
	public Vector2f(final Point point) {
		this(point.x, point.y);
	}
	
	/**
	 * construct the vector using two floats
	 * @param a
	 * @param b
	 */
	public Vector2f(final float a, final float b) {
		super(a, b);
	}
	
	/**
	 * construct the vector using an other Vector2f
	 * @param direction
	 */
	public Vector2f(Vector2f direction) {
		this.x = direction.x;
		this.y = direction.y;
	}

	/**
	 * The coordinates of two vectors are added to each other
	 * @param other
	 * @return
	 */
	public Vector2f add(final Vector2f other) {
		return new Vector2f(this.x + other.x, this.y + other.y);
	}
	
	/**
	 * The coordinates of two vectors are subtracted from each other
	 * @param other
	 * @return
	 */
	public Vector2f subtract(final Vector2f other) {
		return new Vector2f(this.x - other.x, this.y - other.y);
	}
	
	/**
	 * the coordinates of two vectors are multiplied by eachother
	 * @param other
	 * @return
	 */
	public Vector2f multiply(final Vector2f other) {
		return new Vector2f(this.x * other.x, this.y * other.y);
	}
	
	/**
	 * both coordinates of a vector are multiplied by a float
	 * @param scalar
	 * @return
	 */
	public Vector2f multiply(final float scalar) {
		return new Vector2f(this.x * scalar, this.y * scalar);
	}
	
	/**
	 * the coordinates of two vectors are divided by eachother
	 * @param other
	 * @return
	 */
	public Vector2f divide(final Vector2f other) {
		return new Vector2f(this.x / other.x, this.y / other.y);
	}
	
	/**
	 * both coordinates of a vector are divided by eachother
	 * @param scalar
	 * @return
	 */
	public Vector2f divide(final float scalar) {
		return new Vector2f(this.x / scalar, this.y / scalar);
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof Vector2f && (this.x == ((Vector2f) other).x) && (this.y == ((Vector2f) other).y));
	}

	/**
	 * the vector is resized to length 1, but keeps the same direction
	 * @return
	 */
	public Vector2f normalize() {
		super.normalise();
		return this;
	}
	
	/**
	 * casts the coordinates to integers and returns them in a Point
	 * @return
	 */
	public Point toPoint() {
		return new Point((int) this.x, (int) this.y);
	}

}
