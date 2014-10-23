package nl.tudelft.ti2206.graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

/**
 * The {@code Sprite} interface describes that an object can be rendered on to
 * the screen, and therefore has a position and a {@link Dimension}.
 * 
 * @author Jan-Willem Gmelig Meyling
 */
public interface Sprite {
	
	/**
	 * Render this {@code Sprite}
	 * 
	 * @param graphics
	 *            {@link Graphics} instance to paint this {@code Sprite} on
	 */
	void render(Graphics graphics);	
	
	/**
	 * Set the position for this {@code Sprite}
	 * 
	 * @param position
	 *            {@link Point} for this position
	 */
	void setPosition(Point position);
	
	/**
	 * Translate this sprite
	 * 
	 * @param dx
	 *            the distance to move this point along the X axis
	 * @param dy
	 *            the distance to move this point along the Y axis
	 */
	void translate(int dx, int dy);
	
	/**
	 * @return the position for this {@code Sprite}
	 */
	Point getPosition();
	
	/**
	 * @return the X variable for this {@code Sprite}
	 */
	default int getX() {
		return getPosition().x;
	}
	
	/**
	 * @return the Y variable for this {@code Sprite}
	 */
	default int getY() {
		return getPosition().y;
	}
	
	/**
	 * @return the width for this {@code Sprite}
	 */
	int getWidth();
	
	/**
	 * @return the height for this {@code Sprite}
	 */
	int getHeight();
	
	/**
	 * @return the dimensions for this {@code Sprite}
	 */
	default Dimension getSize() {
		return new Dimension(getWidth(), getHeight());
	}
	
}
