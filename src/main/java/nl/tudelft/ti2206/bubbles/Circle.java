package nl.tudelft.ti2206.bubbles;

import java.awt.Point;

/**
 * Circle
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface Circle {

	/**
	 * @return the radius for this {@code Circle}
	 */
	int getRadius();
	
	/**
	 * @return the center for this {@code Circle}
	 */
	Point getCenter();
	
	/**
	 * Set the center for this {@code Circle}
	 * @param center
	 */
	void setCenter(Point center);
	
	/**
	 * @return the diameter of this {@code Circle}
	 */
	default int getDiameter() {
		return getRadius() * 2;
	}
	
	/**
	 * @return the surface for this {@code Circle}
	 */
	default double getSurface() {
		return Math.PI * 2 * getRadius();
	}
	
	/**
	 * Translate this circle
	 * @param dx
	 * @param dy
	 */
	default void translate(int dx, int dy) {
		Point center = getCenter();
		center.translate(dx, dy);
		setCenter(center);
	}
	
	/**
	 * @param other another {@code Circle}
	 * @return the distance between this {@code Circle} and the other {@code Circle}
	 */
	default double getDistance(final Circle other) {
		return getCenter().distance(other.getCenter());
	}
	
	/**
	 * @param other another {@code Circle}
	 * @return true if this {@code Circle} intersects the other {@code Circle}
	 */
	default boolean intersect(final Circle other) {
		return getDistance(other) < (getRadius() + other.getRadius());
	}
	
}
