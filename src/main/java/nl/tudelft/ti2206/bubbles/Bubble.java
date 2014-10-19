package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.bubbles.pop.PopBehaviour;
import nl.tudelft.ti2206.bubbles.pop.RecursivePopBehaviour;
import nl.tudelft.ti2206.bubbles.snap.SnapBehaviour;
import nl.tudelft.ti2206.graphics.Sprite;
import nl.tudelft.util.Vector2f;

import com.google.common.collect.Lists;

/**
 * A {@code} BubblePlaceholder represents a place that a Bubble can snap on to.
 * 
 * @author Jan-Willem Gmelig Meyling
 */
public interface Bubble extends Sprite, Circle, Serializable, SnapBehaviour {

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
	default void bind(Direction direction, Bubble other) {
		setBubbleAt(direction, other);
		if (other != null) {
			other.setBubbleAt(direction.opposite(), this);
		}
	}

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
	 * @return true if this {@code Bubble} has a {@link Color}
	 */
	boolean hasColor();
	
	/**
	 * @return the {@link Color} for this {@code Bubble}
	 */
	Color getColor();

	/**
	 * @return A {@link Collection} containing the surrounding {@code Bubbles}.
	 *         This can be non-hittable bubbles as well.
	 */
	Collection<Bubble> getNeighbours();

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
	 * The collideHook is called on the shot {@code Bubble} when it collides
	 * with another Bubble
	 * 
	 * @param target
	 *            The bubble this {@code Bubble} collided with;
	 */
	default void collideHook(Bubble target) {}

	/**
	 * The pop hook is called on the shot {@code Bubble} when it pops
	 */
	default void popHook() {}

	/**
	 * The snap hook is called on the shot {@code Bubble} when it doesn't
	 * collide with another bubble
	 */
	default void snapHook() {}

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
		while (current.hasBubbleAt(direction)) {
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

	public final static PopBehaviour RECURSIVE_POP = new RecursivePopBehaviour();

	/**
	 * Get the {@link PopBehaviour} for this {@code Bubble}
	 * 
	 * @return the {@link PopBehaviour} for this {@code Bubble}
	 */
	default PopBehaviour getPopBehaviour() {
		return RECURSIVE_POP;
	}

	/**
	 * Checks if this {@code Bubble} pops with another {@code Bubble}. For
	 * example, two {@link ColouredBubble ColouredBubbles} with the same
	 * {@link Color} are allowed to pop together.
	 * 
	 * @param target
	 *            the target bubble
	 * @return True if this bubble pops with the target
	 */
	boolean popsWith(Bubble target);

	default Vector2f velocityChange() {
		return new Vector2f(0f, 0f);
	}

}
