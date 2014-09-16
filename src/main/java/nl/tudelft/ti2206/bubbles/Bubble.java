package nl.tudelft.ti2206.bubbles;

import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

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
	void bindBottomLeft(Bubble botLeft);
	void bindBottomRight(Bubble botRight);
	
	Bubble getBubbleAt(Direction direction);
	void setBubbleAt(Direction direction, Bubble bubble);
	boolean hasBubbleAt(Direction direction);
	
	boolean intersect(Bubble b);
	Point getCenter();
	int getRadius();
	double getDistance(Bubble b);
	
	Collection<Bubble> getNeighbours();
	<T> List<T> getNeighboursOfType(Class<T> type);
	BubblePlaceholder getSnapPosition(Bubble b);

	void setOrigin(boolean value);
	
	default Stream<Bubble> traverseRight() {
		final List<Bubble> bubbles = Lists.newArrayList(this);
		Bubble current = this;
		while(current.hasBubbleAt(Direction.RIGHT)) {
			current = current.getBubbleAt(Direction.RIGHT);
			bubbles.add(current);
		}
		return bubbles.stream();
	}
	
	enum Direction {
		TOPLEFT, TOPRIGHT, LEFT, RIGHT, BOTTOMLEFT, BOTTOMRIGHT;
	}

}
