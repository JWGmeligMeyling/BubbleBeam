package nl.tudelft.ti2206.bubbles.listeners;

import java.util.EventListener;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;

public interface BubbleMeshListener extends EventListener {

	void rowInserted(BubbleMesh bubbleMesh);

	void bubbleReplaced(BubbleMesh bubbleMesh, Bubble old, Bubble newBubble);

	void pop(BubbleMesh bubbleMesh, Set<Bubble> bubbles);

	void points(BubbleMesh bubbleMesh, int amount);

	public interface RowInstertedListener extends BubbleMeshListener {

		default void bubbleReplaced(BubbleMesh bubbleMesh, Bubble old, Bubble newBubble) {};

		default void pop(BubbleMesh bubbleMesh, Set<Bubble> bubbles) {		};

		default void points(BubbleMesh bubbleMesh, int amount) {		};
	}

	public interface ScoreListener extends BubbleMeshListener {
		default void rowInserted(BubbleMesh bubbleMesh) {		};

		default void bubbleReplaced(BubbleMesh bubbleMesh, Bubble old, Bubble newBubble) {		};

		default void pop(BubbleMesh bubbleMesh, Set<Bubble> bubbles) { };
	}

	public interface PopListener extends BubbleMeshListener {
		
		default void rowInserted(BubbleMesh bubbleMesh) {};

		default void bubbleReplaced(BubbleMesh bubbleMesh, Bubble old,	Bubble newBubble) {};

		default void points(BubbleMesh bubbleMesh, int amount) { };
	}

}
