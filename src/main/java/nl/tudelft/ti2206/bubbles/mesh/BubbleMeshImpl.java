package nl.tudelft.ti2206.bubbles.mesh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.Direction;
import nl.tudelft.ti2206.bubbles.pop.PopBehaviour;
import nl.tudelft.ti2206.game.backend.GameOver;
import nl.tudelft.ti2206.game.event.BubbleMeshListener;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.BubblePopEvent;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.RowInsertEvent;
import nl.tudelft.ti2206.logger.Logger;
import nl.tudelft.ti2206.logger.LoggerFactory;
import nl.tudelft.util.EventTargetImpl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Default implementation for {@link BubbleMesh}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class BubbleMeshImpl extends EventTargetImpl<BubbleMeshListener> implements BubbleMesh {
	
	private static final long serialVersionUID = -2580249152755739807L;
	private static final Logger log = LoggerFactory.getLogger(BubbleMesh.class);
	
	protected final int rowWidth;
	protected Bubble topLeftBubble;
	protected Bubble bottomLeftBubble;
	protected final Point position = new Point(0,0);
	
	private Point lastUpdatePosition = new Point(0,0);
	private int bubbleHeight = 22;
	
	/**
	 * Construct a new BubbleMesh
	 * 
	 * @param topLeftBubble
	 *            The topleft {@code Bubble}
	 * @param bottomLeftBubble
	 *            The bottomleft {@code Bubble}
	 * @param rowWidth
	 *            the width of this mesh
	 */
	public BubbleMeshImpl(final Bubble topLeftBubble, final Bubble bottomLeftBubble, final int rowWidth) {
		super();
		this.topLeftBubble = topLeftBubble;
		this.bottomLeftBubble = bottomLeftBubble;
		this.rowWidth = rowWidth;
	}

	@Override
	public void calculatePositions() {
		for (Bubble bubble : this) {
			if(bubble != topLeftBubble) {
				Point newPosition = bubble.calculatePosition();
				bubble.setPosition(newPosition);
			}
		}
	}
	
	@Override
	public void replaceBubble(final Bubble original,
			final Bubble replacement) throws GameOver {
		
		if(isBottomRowBubble(original)) {
			throw new GameOver();
		}
		
		replacement.replace(original);
		
		if (topLeftBubble.equals(original)) {
			topLeftBubble = replacement;
		}
		else if(bottomLeftBubble.equals(original)) {
			bottomLeftBubble = replacement;
		}
	}
	
	protected boolean isBottomRowBubble(final Bubble bubble) {
		return bottomLeftBubble.traverse(Direction.RIGHT)
				.anyMatch(other -> other.equals(bubble));
	}
	
	@Override
	public boolean pop(final Bubble target) {
		
		final PopBehaviour popBehaviour = target.getPopBehaviour();
		final Set<Bubble> bubblesToPop = popBehaviour.getBubblesToPop(target);
		
		if(popBehaviour.isValidPop(bubblesToPop)) {
			findIsolatedBubbles(bubblesToPop);
			
			bubblesToPop.forEach(bubble -> {
				bubble.popHook();
				this.replaceBubble(bubble, new BubblePlaceholder());
				log.info("Bubble popped: {}", bubble);
			});
			
			target.popHook();

			final BubblePopEvent event = new BubblePopEvent(this, bubblesToPop);
			listeners.forEach(listener -> listener.pop(event));
			return true;
		}
		
		target.snapHook();
		return false;
	}
	
	protected void findIsolatedBubbles(final Set<Bubble> bubblesToPop) {
		
		final Set<Bubble> connectedToTop = Sets.newHashSet();
		final Set<Bubble> allBubbles = Sets.newHashSet(Iterables
				.filter(this, Bubble::isHittable));
		
		allBubbles.removeAll(bubblesToPop);
		allBubbles.stream()
			.filter(bubble -> !connectedToTop(bubble, connectedToTop, bubblesToPop))
			.forEach(bubble -> log.info("Found isolated bubble {}", bubble));
	}
	
	@Override
	public boolean bubbleIsTop(final Bubble target) {
		return topLeftBubble.traverse(Direction.RIGHT).anyMatch(bubble -> bubble.equals(target));
	}
	
	/**
	 * 
	 * @param target
	 *            The {@link Bubble} to check for
	 * @param bubblesConnectedToTop
	 *            Bubbles that are connected to the top. The list will be
	 *            filled with temporary values to optimize this function. To
	 *            make the calculation even faster, a non empty list can be
	 *            supplied.
	 * @param bubblesToPop
	 *            Bubbles should not be connected to the top through one of
	 *            the bubbles in this list. Bubbles we find to be not
	 *            connected to the top are stored in this list for
	 *            optimization.
	 * @return true iff the target {@link Bubble} is connected to the top,
	 *         which means it is in the top row or connected to a top
	 *         (hittable) bubble through other (hittable) bubbles.
	 */
	protected boolean connectedToTop(final Bubble target,
			final Set<Bubble> bubblesConnectedToTop,
			final Set<Bubble> bubblesToPop) {
		
		return connectedToTop(target, bubblesConnectedToTop, bubblesToPop,
				Sets.newHashSet());
	}
	
	/**
	 * 
	 * @param target
	 *            The {@link Bubble} to check for
	 * @param bubblesConnectedToTop
	 *            Bubbles that are connected to the top. The list will be
	 *            filled with temporary values to optimize this function. To
	 *            make the calculation even faster, a non empty list can be
	 *            supplied.
	 * @param bubblesToPop
	 *            Bubbles should not be connected to the top through one of
	 *            the bubbles in this list. Bubbles we find to be not
	 *            connected to the top are stored in this list for
	 *            optimization.
	 * @param walked
	 *            The bubbles we have visited in this lookup
	 * @return true iff the target {@link Bubble} is connected to the top,
	 *         which means it is in the top row or connected to a top
	 *         (hittable) bubble through other (hittable) bubbles.
	 */
	protected boolean connectedToTop(final Bubble target,
			final Set<Bubble> bubblesConnectedToTop,
			final Set<Bubble> bubblesToPop,
			final Set<Bubble> walked) {

		boolean connectedToTop = false;
		
		// Preconditions to be at top
		// (1) Bubble should be hittable (Coloured)
		// (2) Bubble should not be set to be popped
		// (3) Bubble should not be walked (we don't want a bubble to be connected through itself)
		if(target.isHittable() && !bubblesToPop.contains(target) && walked.add(target)) {
			// (4) The bubble is either:
			//     - at the top row, or:
			//     - connected to at least one bubble at the top row (recursive call)
			connectedToTop = bubbleIsTop(target) || bubblesConnectedToTop.contains(target) ||
					target.getNeighbours().stream()
					.anyMatch(bubble -> connectedToTop(bubble, bubblesConnectedToTop, bubblesToPop, walked));
			
			// Remove this bubble for the walked set
			walked.remove(target);
			// Cache the calculation result
			if(connectedToTop) {
				bubblesConnectedToTop.add(target);
			}
			else {
				bubblesToPop.add(target);
			}
		}
		
		return connectedToTop;
	}
	
	@Override
	public void insertRow(final Iterator<Bubble> bubbleProvider) throws GameOver {
		log.info("Inserting row");
		
		Iterator<Bubble> bubbles = iterator();
		assert bubbles.hasNext();
		Bubble child = bubbles.next();
		Bubble previousBubble = null;
		boolean shift = !child.hasBubbleAt(Direction.BOTTOMLEFT);
		
		for (int i = 0; i < rowWidth; i++) {
			Bubble bubble = bubbleProvider.next();
			
			if (shift) {
				child.bind(Direction.TOPRIGHT, bubble);
				if (child.hasBubbleAt(Direction.RIGHT)) {
					child.getBubbleAt(Direction.RIGHT).bind(Direction.TOPLEFT, bubble);
				}
			} else {
				child.bind(Direction.TOPLEFT, bubble);
				if (child.hasBubbleAt(Direction.LEFT)) {
					child.getBubbleAt(Direction.LEFT).bind(Direction.TOPRIGHT, bubble);
				}
			}
			
			if (previousBubble != null) {
				previousBubble.bind(Direction.RIGHT, bubble);
			}
			previousBubble = bubble;
			
			if (i == 0) {
				if (shift)
					bubble.translate(bubble.getWidth() / 2, 0);
				bubble.translate(position.x, position.y);
				topLeftBubble = bubble;
			}
			
			assert bubbles.hasNext();
			child = bubbles.next();
		}
		
		updateBottomRow();

		calculatePositions();
		
		List<Bubble> insertedBubbles = Lists.newArrayList(topLeftBubble.traverse(
				Direction.RIGHT).iterator());
		final RowInsertEvent event = new RowInsertEvent(this, insertedBubbles);
		listeners.forEach(listener -> listener.rowInsert(event));
		
		log.info("Finished inserting row");
	}
	
	protected void updateBottomRow() throws GameOver {
		
		// Update new bottom row
		if(bottomLeftBubble.hasBubbleAt(Direction.TOPLEFT)) {
			bottomLeftBubble = bottomLeftBubble.getBubbleAt(Direction.TOPLEFT);
		}
		else {
			bottomLeftBubble = bottomLeftBubble.getBubbleAt(Direction.TOPRIGHT);
		}
		
		bottomLeftBubble.traverse(Direction.RIGHT).forEach(bubble -> {
			if(bubble.isHittable()) {
				// There shouldn't be a hittable bubble in the bottom row
				throw new GameOver();
			}

			// Clear bindings to removed row
			bubble.bind(Direction.BOTTOMLEFT, null);
			bubble.bind(Direction.BOTTOMRIGHT, null);
		});
	}
	
	@Override
	public BubbleMeshIterator iterator() {
		return new BubbleMeshIterator();
	}
	
	public class BubbleMeshIterator implements Iterator<Bubble> {
		
		private Bubble currentRowStart;
		
		private Iterator<Bubble> currentRowIterator;
		
		protected BubbleMeshIterator() {
			Preconditions.checkNotNull(topLeftBubble);
			currentRowStart = topLeftBubble;
			currentRowIterator = currentRowStart.traverse(Direction.RIGHT)
					.iterator();
		}
		
		@Override
		public boolean hasNext() {
			if(currentRowIterator.hasNext()) {
				return true;
			}
			else {
				Bubble newRowStart = currentRowStart.getBubbleAt(Direction.BOTTOMLEFT);
				if(newRowStart == null) {
					newRowStart = currentRowStart.getBubbleAt(Direction.BOTTOMRIGHT);
				}
				currentRowStart = newRowStart;
			}
			
			if(currentRowStart != null) {
				currentRowIterator = currentRowStart.traverse(Direction.RIGHT)
						.iterator();
				return currentRowIterator.hasNext();
			}
			
			return false;
		}
		
		@Override
		public Bubble next() {
			if(!hasNext()) throw new IllegalStateException();
			return currentRowIterator.next();
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	@Override
	public List<Color> getRemainingColours() {
		return Lists.newArrayList(Lists
			.newArrayList(Iterables.filter(this, Bubble::hasColor))
			.stream().map(Bubble::getColor).distinct()
			.iterator());
	}

	@Override
	public boolean isEmpty() {
		for(Bubble bubble : this)
			if(bubble.isHittable())
				return false;
		return true;
	}

	@Override
	public Bubble getTopLeftBubble() {
		return topLeftBubble;
	}
	
	@Override
	public Bubble getBottomLeftBubble() {
		return bottomLeftBubble;
	}

	@Override
	public void render(Graphics graphics) {
		forEach(bubble -> bubble.render(graphics));
	}

	@Override
	public void setPosition(Point position) {
		int dx = position.x - this.position.x;
		int dy = position.y - this.position.y;
		this.translate(dx, dy);
	}

	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void translate(int dx, int dy) throws GameOver {
		position.translate(dx, dy);
		topLeftBubble.translate(dx, dy);
		calculatePositions();

		while(position.y - lastUpdatePosition.y >= bubbleHeight) {
			updateBottomRow();
			lastUpdatePosition.translate(0, bubbleHeight);
		}
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        listeners = Sets.newHashSet();
    }

}