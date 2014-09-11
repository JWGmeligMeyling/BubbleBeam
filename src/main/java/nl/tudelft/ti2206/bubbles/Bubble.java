package nl.tudelft.ti2206.bubbles;

import java.awt.Point;

/**
 * A {@code} BubblePlaceholder represents a place that a Bubble can snap on to.
 * @author Jan-Willem
 *
 */
public interface Bubble extends Sprite {
	
	Point calculatePosition();
	
	void bindTopLeft(Bubble topLeft);
	void bindTopRight(Bubble topRight);
	void bindLeft(Bubble left);
	void bindRight(Bubble right);
	
	Bubble getTopRight();
	Bubble getTopLeft();
	Bubble getLeft();
	Bubble getRight();
	Bubble getBottomLeft();
	Bubble getBottomRight();
	
	void setTopRight(Bubble bubble);
	void setTopLeft(Bubble bubble);
	void setLeft(Bubble bubble);
	void setRight(Bubble bubble);
	void setBottomRight(Bubble bubble);
	void setBottomLeft(Bubble bubble);
	
	boolean hasTopRight();
	boolean hasTopLeft();
	boolean hasLeft();
	boolean hasRight();
	boolean hasBottomLeft();
	boolean hasBottomRight();
	
	boolean intersect(Bubble b);
	Point getCentre();

}
