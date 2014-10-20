package nl.tudelft.ti2206.bubbles.mesh;

import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.game.backend.GameEventListener;

/**
 * Listen to events on the {@link BubbleMesh}
 * 
 * @author Jan-Willem Gmelig Meyling
 * @author Liam Clark
 *
 */
public interface BubbleMeshListener extends GameEventListener {

	/**
	 * Row inserted handler
	 * 
	 * @param bubbleMesh
	 *            The current {@link BubbleMesh}
	 */
	void rowInserted(BubbleMesh bubbleMesh);

	/**
	 * Bubble popped handler
	 * 
	 * @param bubbleMesh
	 *            The current {@link BubbleMesh}
	 * @param bubbles
	 *            The set of Bubbles that have been popped
	 */
	void pop(BubbleMesh bubbleMesh, Set<Bubble> bubbles);

	/**
	 * Points awarded handler
	 * 
	 * @param bubbleMesh
	 *            The current {@link BubbleMesh}
	 * @param amount
	 *            Amount of points awarded
	 */
	void points(BubbleMesh bubbleMesh, int amount);

	/**
	 * Listen for row insert events
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 * @author Liam Clark
	 *
	 */
	public interface RowInstertedListener extends BubbleMeshListener {

		default void bubbleReplaced(BubbleMesh bubbleMesh, Bubble old, Bubble newBubble) {};

		default void pop(BubbleMesh bubbleMesh, Set<Bubble> bubbles) {};

		default void points(BubbleMesh bubbleMesh, int amount) {};
	}

	/**
	 * Listen for score events 
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 * @author Liam Clark
	 *
	 */
	public interface ScoreListener extends BubbleMeshListener {
		
		default void rowInserted(BubbleMesh bubbleMesh) {};
		
		default void bubbleReplaced(BubbleMesh bubbleMesh, Bubble old, Bubble newBubble) {};

		default void pop(BubbleMesh bubbleMesh, Set<Bubble> bubbles) { };
	}

	/**
	 * Listen for pop events
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 * @author Liam Clark
	 */
	public interface PopListener extends BubbleMeshListener {
		
		default void rowInserted(BubbleMesh bubbleMesh) {};

		default void bubbleReplaced(BubbleMesh bubbleMesh, Bubble old,	Bubble newBubble) {};

		default void points(BubbleMesh bubbleMesh, int amount) {};
	}

}
