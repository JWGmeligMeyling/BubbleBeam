package nl.tudelft.ti2206.game.event;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Set;

import javax.imageio.plugins.bmp.BMPImageWriteParam;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;

/**
 * A {@link BubbleMeshListener} listens for changes to the {@link BubbleMesh}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface BubbleMeshListener extends EventListener {
	
	/**
	 * Abstract base class for {@link BubbleMeshEvent BubbleMeshEvents}. Extends
	 * {@link EventObject}, but forces to use a {@link BubbleMesh} as event
	 * source.
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	abstract class BubbleMeshEvent extends EventObject {

		private static final long serialVersionUID = -7537897994655908360L;
		
		protected final transient BubbleMesh bubbleMesh;
		
		/**
		 * Construct a new {@link BubbleMeshEvent}
		 * 
		 * @param bubbleMesh
		 *            {@link BubbleMesh} for this {@link BubbleMeshEvent}
		 */
		protected BubbleMeshEvent(BubbleMesh bubbleMesh) {
			super(bubbleMesh);
			this.bubbleMesh = bubbleMesh;
		}

		@Override
		public BubbleMesh getSource() {
			return bubbleMesh;
		}

	}
	
	/**
	 * The {@link RowInsertEvent} is triggered when a row gets inserted into the
	 * {@link BMPImageWriteParam}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	class RowInsertEvent extends BubbleMeshEvent {
		
		private static final long serialVersionUID = 2696579104143658958L;
		
		protected final List<Bubble> insertedBubbles;
		
		/**
		 * Construct a new {@link RowInsertEvent}
		 * 
		 * @param bubbleMesh
		 *            {@link BubbleMesh} for this {@link BubbleMeshEvent}
		 * @param insertedBubbles
		 *            a list containing the {@link Bubble Bubbles} that have
		 *            been inserted
		 */
		public RowInsertEvent(BubbleMesh bubbleMesh, List<Bubble> insertedBubbles) {
			super(bubbleMesh);
			this.insertedBubbles = insertedBubbles;
		}

		/**
		 * @return the insertedBubbles
		 */
		public List<Bubble> getInsertedBubbles() {
			return insertedBubbles;
		}

		/**
		 * @return amount of inserted bubbles
		 * @see java.util.Set#size()
		 */
		public int amountOfInsertedBubbles() {
			return insertedBubbles.size();
		}
		
	}
	
	/**
	 * Invoked on this {@code BubbleMeshListener} when a {@link RowInsertEvent}
	 * occurred
	 * 
	 * @param event
	 *            {@code RowInsertEvent}
	 */
	void rowInsert(RowInsertEvent event);
	
	/**
	 * The {@link BubblePopEvent} is triggered when one or more {@link Bubble
	 * Bubbles} pop in the {@link BubbleMesh}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	class BubblePopEvent extends BubbleMeshEvent {
		
		private static final long serialVersionUID = 5334939430531797593L;
		
		protected final Set<Bubble> bubblesPopped;
		
		/**
		 * Construct a new {@link BubblePopEvent}
		 * 
		 * @param bubbleMesh
		 *            {@link BubbleMesh} for this {@link BubbleMeshEvent}
		 * @param bubblesPopped
		 *            List containing the {@link Bubble Bubbles} that have
		 *            popped
		 */
		public BubblePopEvent(BubbleMesh bubbleMesh, Set<Bubble> bubblesPopped) {
			super(bubbleMesh);
			this.bubblesPopped = bubblesPopped;
		}
		
		/**
		 * @return the bubblesPopped
		 */
		public Set<Bubble> getPoppedBubbles() {
			return bubblesPopped;
		}

		/**
		 * @return amount of bubbles popped
		 * @see java.util.Set#size()
		 */
		public int amountOfPoppedBubbles() {
			return bubblesPopped.size();
		}
		
	}
	
	/**
	 * Invoked on this {@code BubbleMeshListener} when a {@link BubblePopEvent} occurred
	 * @param event {@code BubblePopEvent}
	 */
	void pop(BubblePopEvent event);
	
	/**
	 * {@code RowInstertedListener}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 * @author Liam Clark
	 *
	 */
	@FunctionalInterface
	interface RowInstertedListener extends BubbleMeshListener {
		@Override default void pop(BubblePopEvent event) {}
	}
	
	/**
	 * {@code PopListener}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 * @author Liam Clark
	 *
	 */
	@FunctionalInterface
	interface PopListener extends BubbleMeshListener {
		@Override default void rowInsert(RowInsertEvent event) {} 
	}

}
