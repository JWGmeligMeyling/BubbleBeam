package nl.tudelft.ti2206.bubbles;

import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

/**
 * A {@code} BubblePlaceholder represents a place that a Bubble can snap on to.
 * 
 * @author Jan-Willem Gmelig Meyling
 */
public interface Bubble extends Sprite {
		
	void bind(Direction direction, Bubble other);
	Bubble getBubbleAt(Direction direction);
	void setBubbleAt(Direction direction, Bubble bubble);
	boolean hasBubbleAt(Direction direction);
	
	boolean intersect(Bubble b);
	boolean isHittable();
	
	Point getCenter();
	int getRadius();
	double getDistance(Bubble b);
	
	Collection<Bubble> getNeighbours();
	<T> List<T> getNeighboursOfType(Class<T> type);
	BubblePlaceholder getSnapPosition(Bubble b);

	void setOrigin(boolean value);
	Point calculatePosition();
	
	default Stream<Bubble> traverse(final Direction direction) {
		final List<Bubble> bubbles = Lists.newArrayList(this);
		Bubble current = this;
		while(current.hasBubbleAt(direction)) {
			current = current.getBubbleAt(direction);
			bubbles.add(current);
		}
		return bubbles.stream();
	}
	
	enum Direction {
		TOPLEFT, TOPRIGHT, LEFT, RIGHT, BOTTOMLEFT, BOTTOMRIGHT;
		
		public Direction opposite() {
			return oppositeFor(this);
		}
		
		public static Direction oppositeFor(final Direction direction) {
			switch(direction) {
			case BOTTOMLEFT: return TOPRIGHT;
			case BOTTOMRIGHT: return TOPLEFT;
			case LEFT: return RIGHT;
			case RIGHT: return LEFT;
			case TOPLEFT: return BOTTOMRIGHT;
			case TOPRIGHT: return BOTTOMLEFT;
			}
			throw new IllegalArgumentException();
		}
		
	}

}
