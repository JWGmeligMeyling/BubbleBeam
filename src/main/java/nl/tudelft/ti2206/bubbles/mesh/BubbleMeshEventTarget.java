package nl.tudelft.ti2206.bubbles.mesh;

import nl.tudelft.ti2206.game.event.BubbleMeshListener;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.*;
import nl.tudelft.util.AbstractEventTarget;
import nl.tudelft.util.EventTarget;

/**
 * The {@link EventTarget} for the {@link BubbleMesh}
 * 
 * @author Jan-Willem Gmelig Meyling
 * @author Liam Clark
 *
 */
public class BubbleMeshEventTarget extends AbstractEventTarget<BubbleMeshListener> {
	
	/**
	 * Add a {@link RowInstertedListener}
	 * @param listener
	 */
	public void addRowInsertedListener(RowInstertedListener listener) {
		this.addEventListener(listener);
	}
	
	/**
	 * Add a {@link ScoreListener}
	 * @param listener
	 */
	public void addScoreListener(ScoreListener listener) {
		this.addEventListener(listener);
	}
	
	/**
	 * Add a {@link PopListener}
	 * @param listener
	 */
	public void addPopListener(PopListener listener) {
		this.addEventListener(listener);
	}
	
	/**
	 * Trigger a row insert
	 * @param event Event that should be triggered on the listeners
	 */
	public void rowInsert(final RowInsertEvent event) {
		listeners.forEach(listener -> listener.rowInsert(event));
	}
	
	/**
	 * Trigger a score award
	 * @param event Event that should be triggered on the listeners
	 */
	public void addScore(final ScoreEvent event) {
		listeners.forEach(listener -> listener.score(event));
	}
	
	/**
	 * Trigger a pop
	 * @param event Event that should be triggered on the listeners
	 */
	public void pop(final BubblePopEvent event) {
		listeners.forEach(listener -> listener.pop(event));
	}

}