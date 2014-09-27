package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import nl.tudelft.ti2206.bubbles.Bubble.Direction;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.game.GameController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public interface BubbleMesh extends Iterable<Bubble>, Serializable {
	
	default Stream<Bubble> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
	
	/**
	 * Pop this bubble and it's neighbors recursively
	 * 
	 * @return true iff bubbles popped
	 */
	boolean pop(ColouredBubble target);
	
	/**
	 * Calculate the positions for the bubbles
	 */
	void calculatePositions();
	
	/**
	 * Insert a new row of bubbles at the top
	 * 
	 * @throws GameOver
	 */
	void insertRow(GameController gameController) throws GameOver;
	
	/**
	 * Replace a {@code Bubble} in the mesh for another {@code Bubble}
	 * 
	 * @param original
	 *            {@code Bubble} to be replaced
	 * @param replacement
	 */
	void replaceBubble(Bubble original, Bubble replacement);
	
	/**
	 * Add a score listener
	 * 
	 * @param listener
	 */
	void addScoreListener(ScoreListener listener);
	
	/**
	 * @param target
	 * @return true if the given {@link Bubble} is in the top row
	 */
	boolean bubbleIsTop(Bubble target);
	
	/**
	 * @return Get a List of colors that still exist in the mesh
	 */
	List<Color> getRemainingColours();
	
	/**
	 * Replace the {@code BubbleMesh} with another one
	 * 
	 * @param bubbleMesh
	 */
	void replace(BubbleMesh bubbleMesh);
	
	/**
	 * @return the top left {@link Bubble} in the mesh
	 */
	Bubble getTopLeftBubble();
	
	/**
	 * @return the bottom left {@link Bubble} in the mesh
	 */
	Bubble getBottomLeftBubble();
	
	public static BubbleMesh parse(final InputStream inputstream) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream))) {
			List<String> lines = Lists.newArrayList();
			while (reader.ready()) {
				lines.add(reader.readLine());
			}
			BubbleMesh mesh = parse(lines);
			return mesh;
		}
	}
	
	public static BubbleMesh parse(final String string) {
		return parse(Arrays.asList(string.split("[\r\n]+")));
	}
	
	public static BubbleMesh parse(final List<String> rows) {
		return new BubbleMeshParser(rows).parse();
	}
	
	interface ScoreListener {
		void incrementScore(int amount);
	}
	
	public static class BubbleMeshParser {
		
		protected final List<String> rows;
		protected final int rowAmount;
		protected final int rowSize;
		protected final Bubble[][] bubbles;
		
		public BubbleMeshParser(final List<String> rows) {
			this.rows = rows;
			this.rowAmount = rows.size();
			if (rowAmount < 1)
				throw new IllegalArgumentException("Wrong file format");
			this.rowSize = rows.get(0).length();
			this.bubbles = new Bubble[rowAmount][rowSize];
		}
		
		public BubbleMeshImpl parse() {
			for (int i = 0; i < rowAmount; i++) {
				String rowStr = rows.get(i);
				
				for (int j = 0; j < rowSize; j++) {
					
					Bubble bubble = bubbles[i][j] = parseBubble(rowStr.charAt(j));
					
					if (i > 0) {
						if (i % 2 == 0) { // 3rd, 5fth rows , ...
							bubble.bind(Direction.TOPRIGHT, bubbles[i - 1][j]);
							if (j > 0) {
								bubble.bind(Direction.TOPLEFT, bubbles[i - 1][j - 1]);
							}
						} else {
							bubble.bind(Direction.TOPLEFT, bubbles[i - 1][j]);
							if (j < (rowSize - 1)) {
								bubble.bind(Direction.TOPRIGHT, bubbles[i - 1][j + 1]);
							}
						}
					}
					
					if (j > 0) {
						bubble.bind(Direction.LEFT, bubbles[i][j - 1]);
					}
					
				}
				
			}
			
			BubbleMeshImpl result = new BubbleMeshImpl();
			result.topLeftBubble = bubbles[0][0];
			result.bottomLeftBubble = bubbles[rowAmount - 1][0];
			return result;
		}
		
		protected final List<Color> remainingColors = Lists.newArrayList(Color.RED, Color.GREEN,
				Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW);
		
		protected AbstractBubble parseBubble(final char character) {
			final Color color;
			
			switch (character) {
			case ' ':
				return new BubblePlaceholder();
			case 'r':
			case 'R':
				color = Color.RED;
			break;
			case 'g':
			case 'G':
				color = Color.GREEN;
			break;
			case 'b':
			case 'B':
				color = Color.BLUE;
			break;
			case 'C':
			case 'c':
				color = Color.CYAN;
			break;
			case 'M':
			case 'm':
				color = Color.MAGENTA;
			break;
			case 'Y':
			case 'y':
				color = Color.YELLOW;
			break;
			default:
				color = getRandomRemainingColor();
			break;
			}
			
			return new ColouredBubble(color);
		}
		
		protected final Random RANDOM_GENERATOR = new SecureRandom();
		
		protected Color getRandomRemainingColor() {
			final int index = RANDOM_GENERATOR.nextInt(remainingColors.size());
			if (!remainingColors.isEmpty()) {
				return remainingColors.get(index);
			} else {
				throw new IllegalStateException("No colors available!");
			}
		}
		
	}
	
	public static class BubbleMeshImpl implements BubbleMesh {
		
		private static final long serialVersionUID = -2580249152755739807L;
		private static final Logger log = LoggerFactory.getLogger(BubbleMesh.class);
		
		private Bubble topLeftBubble;
		private Bubble bottomLeftBubble;
		
		private transient List<ScoreListener> scoreListeners = Lists.newArrayList();
		
		@Override
		public void calculatePositions() {
			for (Bubble bubble : this) {
				if (bubble != topLeftBubble) {
					Point newPosition = bubble.calculatePosition();
					bubble.setPosition(newPosition);
				}
			}
		}
		
		@Override
		public void replaceBubble(final Bubble original, final Bubble replacement) {
			if (isBottomRowBubble(original)) {
				throw new GameOver();
			}
			
			replacement.replace(original);
			
			if (topLeftBubble.equals(original)) {
				topLeftBubble = replacement;
			}
		}
		
		protected boolean isBottomRowBubble(final Bubble bubble) {
			return bottomLeftBubble.traverse(Direction.RIGHT).anyMatch(
					other -> other.equals(bubble));
		}
		
		@Override
		public boolean pop(final ColouredBubble target) {
			final Set<ColouredBubble> bubblesToPop = Sets.newHashSet(target);
			
			if (this.pop(target, bubblesToPop)) {
				findIsolatedBubbles(bubblesToPop);
				
				bubblesToPop.forEach(bubble -> {
					this.replaceBubble(bubble, new BubblePlaceholder());
					log.info("Bubble popped: {}", bubble);
				});
				
				calculateScore(bubblesToPop);
				return true;
			}
			
			return false;
		}
		
		protected void findIsolatedBubbles(final Set<ColouredBubble> bubblesToPop) {
			
			final Set<Bubble> connectedToTop = Sets.newHashSet();
			final Set<ColouredBubble> allBubbles = Sets.newHashSet(Iterables.filter(this,
					ColouredBubble.class));
			
			allBubbles.removeAll(bubblesToPop);
			allBubbles.stream()
					.filter(bubble -> !connectedToTop(bubble, connectedToTop, bubblesToPop))
					.forEach(bubble -> log.info("Found isolated bubble {}", bubble));
		}
		
		/**
		 * Recursively search for neighboring bubbles of the same color
		 * 
		 * @param bubblesToPop
		 *            {@link Set} of {@code ColouredBubbles} to be popped
		 */
		protected boolean pop(final ColouredBubble target, final Set<ColouredBubble> bubblesToPop) {
			
			final List<ColouredBubble> colouredBubbles = target
					.getNeighboursOfType(ColouredBubble.class);
			Color targetColor = target.getColor();
			
			/*
			 * Find neighboring bubbles of the same colour, and pop them
			 * recursively. Add them to a set in order to check if we have not
			 * already popped this bubble in the current call.
			 */
			colouredBubbles
					.stream()
					.filter(bubble -> bubble.getColor().equals(targetColor)
							&& bubblesToPop.add(bubble))
					.forEach(bubble -> this.pop(bubble, bubblesToPop));
			
			return bubblesToPop.size() > 2;
		}
		
		@Override
		public boolean bubbleIsTop(final Bubble target) {
			return topLeftBubble.traverse(Direction.RIGHT)
					.anyMatch(bubble -> bubble.equals(target));
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
				final Set<Bubble> bubblesConnectedToTop, final Set<ColouredBubble> bubblesToPop) {
			
			return connectedToTop(target, bubblesConnectedToTop, bubblesToPop, Sets.newHashSet());
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
				final Set<Bubble> bubblesConnectedToTop, final Set<ColouredBubble> bubblesToPop,
				final Set<Bubble> walked) {
			
			boolean connectedToTop = false;
			
			// Preconditions to be at top
			// (1) Bubble should be hittable (Coloured)
			// (2) Bubble should not be set to be popped
			// (3) Bubble should not be walked (we don't want a bubble to be
			// connected through itself)
			if (target.isHittable() && !bubblesToPop.contains(target) && walked.add(target)) {
				// (4) The bubble is either:
				// - at the top row, or:
				// - connected to at least one bubble at the top row (recursive
				// call)
				connectedToTop = bubbleIsTop(target)
						|| bubblesConnectedToTop.contains(target)
						|| target
								.getNeighbours()
								.stream()
								.anyMatch(
										bubble -> connectedToTop(bubble, bubblesConnectedToTop,
												bubblesToPop, walked));
				
				// Remove this bubble for the walked set
				walked.remove(target);
				// Cache the calculation result
				if (connectedToTop) {
					bubblesConnectedToTop.add(target);
				} else {
					bubblesToPop.add((ColouredBubble) target);
				}
			}
			
			return connectedToTop;
		}
		
		/**
		 * Calculate the points for this shot and notify the score listeners
		 * 
		 * @param bubbles
		 */
		protected void calculateScore(final Set<ColouredBubble> bubbles) {
			int amount = bubbles.size() * bubbles.size() * 25;
			scoreListeners.forEach(scoreListener -> scoreListener.incrementScore(amount));
		}
		
		@Override
		public void insertRow(final GameController gameController) throws GameOver {
			log.info("Inserting row");
			
			Iterator<Bubble> bubbles = iterator();
			Bubble child = bubbles.next();
			Bubble previousBubble = null;
			boolean shift = !child.hasBubbleAt(Direction.BOTTOMLEFT);
			
			for (int i = 0; i < 10; i++) {
				Bubble bubble = new ColouredBubble(gameController.getRandomRemainingColor());
				
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
						bubble.setPosition(new Point(topLeftBubble.getX() + AbstractBubble.WIDTH
								/ 2, topLeftBubble.getY()));
					topLeftBubble = bubble;
				}
				
				child = bubbles.next();
			}
			
			updateBottomRow();
			calculatePositions();
			log.info("Finished inserting row");
		}
		
		protected void updateBottomRow() throws GameOver {
			
			// Update new bottom row
			if (bottomLeftBubble.hasBubbleAt(Direction.TOPLEFT)) {
				bottomLeftBubble = bottomLeftBubble.getBubbleAt(Direction.TOPLEFT);
			} else {
				bottomLeftBubble = bottomLeftBubble.getBubbleAt(Direction.TOPRIGHT);
			}
			
			bottomLeftBubble.traverse(Direction.RIGHT).forEach(bubble -> {
				if (bubble.isHittable()) {
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
			
			private Queue<Bubble> a = new LinkedList<Bubble>();
			private Queue<Bubble> b = new LinkedList<Bubble>();
			
			protected BubbleMeshIterator() {
				
				addRowToQueue(a, topLeftBubble);
				
				while (!a.isEmpty()) {
					Bubble bubble = a.remove();
					b.add(bubble);
					
					Bubble firstChild = null;
					
					if (bubble.getBubbleAt(Direction.BOTTOMLEFT) != null) {
						firstChild = bubble.getBubbleAt(Direction.BOTTOMLEFT);
					} else if (bubble.getBubbleAt(Direction.BOTTOMRIGHT) != null) {
						firstChild = bubble.getBubbleAt(Direction.BOTTOMRIGHT);
					}
					
					if (firstChild != null && firstChild.getBubbleAt(Direction.LEFT) == null) {
						addRowToQueue(a, firstChild);
					}
				}
			}
			
			private void addRowToQueue(final Queue<Bubble> bubbles, final Bubble leftEdge) {
				Bubble left = leftEdge;
				bubbles.add(left);
				while (left.getBubbleAt(Direction.RIGHT) != null) {
					left = left.getBubbleAt(Direction.RIGHT);
					bubbles.add(left);
				}
			}
			
			@Override
			public boolean hasNext() {
				return !b.isEmpty();
			}
			
			@Override
			public Bubble next() {
				return b.remove();
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		}
		
		@Override
		public void addScoreListener(final ScoreListener listener) {
			if (scoreListeners == null)
				scoreListeners = Lists.newArrayList();
			scoreListeners.add(listener);
		}
		
		@Override
		public List<Color> getRemainingColours() {
			return Lists.newArrayList(Lists
					.newArrayList(Iterables.filter(this, ColouredBubble.class)).stream()
					.map(ColouredBubble::getColor).distinct().iterator());
		}
		
		@Override
		public void replace(BubbleMesh bubbleMesh) {
			this.topLeftBubble = bubbleMesh.getTopLeftBubble();
			this.bottomLeftBubble = bubbleMesh.getBottomLeftBubble();
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
		public boolean equals(Object other) {
			if (other instanceof BubbleMeshImpl) {
				BubbleMeshIterator otherIterator = ((BubbleMeshImpl) other).iterator();
				BubbleMeshIterator thisIterator = this.iterator();
				while (thisIterator.hasNext()) {
					if (!thisIterator.next().equals(otherIterator.next())) {
						System.out.println("equals" + "false1");
						return false;
					}
				}
				System.out.println("equals" + "true");
				return true;
			} else {
				System.out.println("equals" + "false2");
				return false;
			}
		}
	}
	
}
