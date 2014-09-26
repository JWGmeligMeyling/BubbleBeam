package nl.tudelft.ti2206.bubbles;

import java.awt.Point;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

/**
 * A {@code} BubblePlaceholder represents a place that a Bubble can snap on to.
 * 
 * @author Jan-Willem Gmelig Meyling
 */
public interface Bubble extends Sprite, Circle, Serializable {
	
	/**
	 * Lay a two way binding to another bubble in a certain {@link Direction}
	 * This calls {@link #setBubbleAt(Direction, Bubble) setBubbleAt} for both
	 * {@code Bubbles}. If the other {@code Bubble} is null, this function
	 * breaks any previous binding without laying a new one.
	 * 
	 * @param direction
	 *            {@link Direction} for the binding
	 * @param other
	 *            The other {@code Bubble}
	 */
	void bind(Direction direction, Bubble other);
	
	/**
	 * Get the {@code Bubble} to which this {@code Bubble} is bound in a given
	 * direction
	 * 
	 * @param direction
	 *            {@link Direction} for the binding
	 * @return the {@code Bubble} at the given {@link Direction} relative to
	 *         this {@code Bubble}
	 */
	Bubble getBubbleAt(Direction direction);
	
	/**
	 * Set the {@code Bubble} in a given {@link Direction}. Used by
	 * {@link #bind(Direction, Bubble) bind}.
	 * 
	 * @param direction
	 *            {@link Direction} for the binding
	 * @param bubble
	 *            {@code Bubble} to bind
	 */
	void setBubbleAt(Direction direction, Bubble bubble);
	
	/**
	 * Check if this Bubble has a binding in a certain {@link Direction}
	 * 
	 * @param direction
	 *            {@link Direction} for the binding
	 * @return true if this {@code Bubble} has a binding in the given
	 *         {@code Direction}
	 */
	boolean hasBubbleAt(Direction direction);
	
	/**
	 * @return true if this is a hittable bubble. A hittable bubble for example
	 *         a {@link ColouredBubble}, whereas the {@link BubblePlaceholder}
	 *         only functions as a snapping position for new
	 *         {@link MovingBubbles} and therefore is not hittable - bubbles
	 *         just go through it until they hit an hittable {@code Bubble}.
	 */
	boolean isHittable();
	
	/**
	 * @return A {@link Collection} containing the surrounding {@code Bubbles}.
	 *         This can be non-hittable bubbles as well.
	 */
	Collection<Bubble> getNeighbours();
	
	/**
	 * @param type
	 * @return A {@link Collection} containing the surrounding {@code Bubbles}
	 *         of a given subtype
	 * @see #getNeighbours()
	 */
	<T extends Bubble> List<T> getNeighboursOfType(Class<T> type);
	
	/**
	 * If a {@link MovingBubble} hits this {@code Bubble}, it should snap to the
	 * given {@link BubblePlaceHolder}.
	 * 
	 * @param b
	 * @return The {@code BubblePlaceHolder} at which the {@code MovingBubble}
	 *         should snap
	 */
	BubblePlaceholder getSnapPosition(Bubble b);
	
	/**
	 * @return Calculate this {@code Bubbles} position relative to it's
	 *         neighbouring {@code Bubbles}
	 */
	Point calculatePosition();
	
	/**
	 * @return the connections as map
	 */
	Map<Direction, Bubble> getConnections();

	/**
	 * Traverse the {@link BubbleMesh} in a given {@link Direction}, with this
	 * {@code Bubble} as starting point.
	 * 
	 * @param direction
	 *            {@link Direction} for the binding
	 * @return {@link Stream} of {@code Bubbles}
	 */
	default Stream<Bubble> traverse(final Direction direction) {
		final List<Bubble> bubbles = Lists.newArrayList(this);
		Bubble current = this;
		while(current.hasBubbleAt(direction)) {
			current = current.getBubbleAt(direction);
			bubbles.add(current);
		}
		return bubbles.stream();
	}
	
	/**
	 * Replace this bubble by binding to all of its neighbours in the same
	 * {@code Direction}. This function is called from the {@link BubbleMesh}
	 * when {@code Bubbles} need to be replaced in the mesh.
	 * 
	 * @param original
	 *            {@code Bubble} to be replaced
	 * @see BubbleMesh#replaceBubble(Bubble, Bubble)
	 */
	default void replace(final Bubble original) {
		this.bind(Direction.TOPLEFT, original.getBubbleAt(Direction.TOPLEFT));
		this.bind(Direction.TOPRIGHT, original.getBubbleAt(Direction.TOPRIGHT));
		this.bind(Direction.LEFT, original.getBubbleAt(Direction.LEFT));
		this.bind(Direction.RIGHT, original.getBubbleAt(Direction.RIGHT));
		this.bind(Direction.BOTTOMLEFT, original.getBubbleAt(Direction.BOTTOMLEFT));
		this.bind(Direction.BOTTOMRIGHT, original.getBubbleAt(Direction.BOTTOMRIGHT));
		this.setPosition(original.getPosition());
	}

	/**
	 * Direction
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 */
	enum Direction {
		TOPLEFT, TOPRIGHT, LEFT, RIGHT, BOTTOMLEFT, BOTTOMRIGHT;
		
		/**
		 * @return the opposite {@code Direction}
		 */
		public Direction opposite() {
			return oppositeFor(this);
		}
		
		/**
		 * @param direction
		 *            direction for which to return the opposite direction
		 * @return the opposite {@code Direction} for the given
		 *         {@code Direction}
		 */
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
