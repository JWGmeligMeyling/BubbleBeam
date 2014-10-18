package nl.tudelft.ti2206.bubbles.mesh;

import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMeshListener.PopListener;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMeshListener.RowInstertedListener;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMeshListener.ScoreListener;
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
	 * @param bubbleMesh
	 */
	public void rowInsert(final BubbleMesh bubbleMesh) {
		listeners.forEach(listener -> listener.rowInserted(bubbleMesh));
	}
	
	/**
	 * Trigger a score award
	 * @param bubbleMesh
	 * @param amount
	 */
	public void addScore(final BubbleMesh bubbleMesh, final int amount) {
		listeners.forEach(listener -> listener.points(bubbleMesh, amount));
	}
	
	/**
	 * Trigger a pop
	 * @param bubbleMesh
	 * @param bubblesPopped
	 */
	public void pop(final BubbleMesh bubbleMesh, Set<Bubble> bubblesPopped) {
		listeners.forEach(listener -> listener.pop(bubbleMesh, bubblesPopped));
	}

}