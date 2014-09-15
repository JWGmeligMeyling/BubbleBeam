package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import nl.tudelft.ti2206.exception.GameOver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

public interface BubbleMesh extends Iterable<Bubble> {
	
	default Stream<Bubble> stream() {
	    return StreamSupport.stream(spliterator(), false);
	}
	
	/**
	 * @return a random remaining {@link Color}
	 */
	Color getRandomRemainingColor();
	
	/**
	 * Pop this bubble and it's neighbors recursively
	 * @return true iff bubbles popped
	 */
	boolean pop(ColouredBubble target);

	/**
	 * Calculate the positions for the bubbles
	 */
	void calculatePositions();

	/**
	 * Insert a new row of bubbles at the top
	 * @throws GameOver
	 */
	void insertRow() throws GameOver;
	
	/**
	 * Replace a {@code Bubble} in the mesh for another {@code Bubble}
	 * @param original {@code Bubble} to be replaced
	 * @param replacement 
	 */
	void replaceBubble(Bubble original, Bubble replacement);
	
	void addScoreListener(ScoreListener listener);
	
	/**
	 * @param target
	 * @return true if the given {@link Bubble} is in the top row
	 */
	boolean bubbleIsTop(Bubble target);
	
	public static BubbleMesh parse(File file) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)))) {
			List<String> lines = Lists.newArrayList();
			while (reader.ready()) {
				lines.add(reader.readLine());
			}
			BubbleMesh mesh=parse(lines);
			return mesh;
		}
	}
	
	public static BubbleMesh parse(String string) {
		return parse(Arrays.asList(string.split("[\r\n]+")));
	}
	
	public static BubbleMesh parse(List<String> rows) {
		int rowAmount = rows.size();
		if (rowAmount < 1)
			throw new IllegalArgumentException("Wrong file format");
		int rowSize = rows.get(0).length();
		
		Bubble[][] bubbles = new Bubble[rowAmount][rowSize];
		BubbleMeshImpl result = new BubbleMeshImpl();
		
		for(int i = 0; i < rowAmount; i++) {
			String rowStr = rows.get(i);
			
			for(int j = 0; j < rowSize; j++) {
				
				AbstractBubble bubble;
				bubbles[i][j] = bubble = parseBubble(rowStr.charAt(j), result);
				
				if(i > 0) {
					if(i % 2 == 0) { // 3rd, 5fth rows , ... 
						bubble.bindTopRight(bubbles[i-1][j]);
						if(j > 0) {
							bubble.bindTopLeft(bubbles[i-1][j-1]);
						}
					}
					else {
						bubble.bindTopLeft(bubbles[i-1][j]);
						if(j < (rowSize - 1)) {
							bubble.bindTopRight(bubbles[i-1][j+1]);
						}
					}
				}
				
				if(j > 0) {
					bubble.bindLeft(bubbles[i][j-1]);
				}
				
				if(i == 0 && j == 0) {
					bubble.setOrigin(true);
				}
			}
		}
		
		result.startBubble = bubbles[0][0];
		return result;
	}
	
	static AbstractBubble parseBubble(final char character, final BubbleMesh bubbleMesh) {
		if(character == ' ') {
			return new BubblePlaceholder();
		}
		else {
			Color color;
			switch(character) {
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
				color = bubbleMesh.getRandomRemainingColor();
				break;
			}
			return new ColouredBubble(color);
		}
		
	}
	
	interface ScoreListener {
		void incrementScore(int amount);
	}

	public static class BubbleMeshImpl implements BubbleMesh {
		
		private static final Logger log = LoggerFactory.getLogger(BubbleMesh.class);

		private final List<Color> remainingColors = Lists.newArrayList(Color.RED,
				Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW);

		private Bubble startBubble;
		
		private final List<ScoreListener> scoreListeners = Lists.newArrayList();
		
		@Override
		public void calculatePositions() {
			for(Bubble bubble : this) {
				Point newPosition = bubble.calculatePosition();
				bubble.setPosition(newPosition);
			}
		}

		@Override
		public void replaceBubble(final Bubble original, final Bubble replacement){
			replacement.bindTopLeft(original.getTopLeft());
			replacement.bindTopRight(original.getTopRight());
			replacement.bindLeft(original.getLeft());
			replacement.bindRight(original.getRight());
			replacement.bindBottomLeft(original.getBottomLeft());
			replacement.bindBottomRight(original.getBottomRight());
			replacement.setPosition(original.getPosition());
			
			if(startBubble.equals(original)) {
				startBubble = replacement;
			}
		}

		@Override
		public boolean pop(final ColouredBubble target) {
			Set<ColouredBubble> bubblesToPop = Sets.newHashSet(target);
			if(this.pop(target, bubblesToPop)) {
				bubblesToPop.forEach(bubble -> {
					this.replaceBubble(bubble, new BubblePlaceholder());
				});
				
				updateRemainingColors();
				calculateScore(bubblesToPop);
				return true;
			}
			return false;
		}
		
		/**
		 * Recursively search for neighboring bubbles of the same color
		 * 
		 * @param bubblesToPop
		 *            {@link Set} of {@code ColouredBubbles} to be popped
		 */
		protected boolean pop(final ColouredBubble target,
				final Set<ColouredBubble> bubblesToPop) {
			
			final List<ColouredBubble> colouredBubbles = 
					target.getNeighboursOfType(ColouredBubble.class);
			Color targetColor = target.getColor();
			
			// Find neighboring bubbles of the same colour, and pop them
			// recursively. Add them to a set in order to check if we have not already popped
			// this bubble in the current call.
			colouredBubbles.stream()
				.filter(bubble -> bubble.getColor().equals(targetColor) && bubblesToPop.add(bubble))
				.forEach(bubble -> this.pop(bubble, bubblesToPop));
			
			boolean popped = bubblesToPop.size() > 2;
			
			if(popped) {
				// If bubbles have been popped, check for isolated regions
				// These can be found by calling the pop function on neighboring
				// bubbles that can only connect to the top through a popped
				// bubble. These bubbles can be found by calling the connected
				// to top function.
				colouredBubbles.stream()
					.filter(bubble -> !connectedToTop(bubble, Queues.newArrayDeque(bubblesToPop)) && bubblesToPop.add(bubble))
					.forEach(bubble -> this.pop(bubble, bubblesToPop));
			}
			
			return popped;
		}
		
		@Override
		public boolean bubbleIsTop(final Bubble target) {
			return startBubble.traverseRight().anyMatch(bubble -> bubble.equals(target));
		}

		/**
		 * @param target
		 * @param bubblesHit
		 * @return true if a bubble is connected to a top row bubble
		 */
		protected boolean connectedToTop(final ColouredBubble target,
				final Queue<ColouredBubble> bubblesHit) {
			
			boolean result = false;
			if(!bubblesHit.contains(target)) {
				bubblesHit.add(target);
				result = bubbleIsTop(target) || target.getNeighboursOfType(ColouredBubble.class).stream()
						.anyMatch(bubble -> connectedToTop(bubble, bubblesHit));
				bubblesHit.remove(target);
			}
			
			return result;
		}

		/**
		 * Update the remaining colors list based on the colors that still exist
		 * in the mesh
		 */
		protected void updateRemainingColors() {
			remainingColors.removeIf(color -> this.stream()
				.filter(bubble -> bubble instanceof ColouredBubble)
				.map(bubble -> (ColouredBubble) bubble)
				.map(ColouredBubble::getColor)
				.distinct().noneMatch(a -> a.equals(color)));
		}
		
		/**
		 * Calculate the points for this shot and notify the score listeners
		 * @param bubbles
		 */
		protected void calculateScore(final Set<ColouredBubble> bubbles) {
			int amount = bubbles.size() * bubbles.size() * 25;
			scoreListeners.forEach(
					scoreListener -> scoreListener.incrementScore(amount));
		}

		@Override
		public void insertRow() throws GameOver {
			log.info("Inserting row");
			Iterator<Bubble> bubbles = iterator();
			Bubble child = bubbles.next();
			Bubble previousBubble = null;
			boolean shift = !child.hasBottomLeft();
			
			for(int i = 0; i < 10; i++) {
				Bubble bubble = new ColouredBubble(getRandomRemainingColor());
				
				if(shift){
					child.bindTopRight(bubble);
					if(child.hasRight()) {
						child.getRight().bindTopLeft(bubble);
					}
				}
				else {
					child.bindTopLeft(bubble);
					if(child.hasLeft()) {
						child.getLeft().bindTopRight(bubble);
					}
				}
				
				if(previousBubble != null) {
					previousBubble.bindRight(bubble);
				}
				previousBubble = bubble;
				
				if(i == 0) {
					startBubble.setOrigin(false);
					if(shift)
						bubble.setPosition(new Point(startBubble.getX()
								+ AbstractBubble.WIDTH / 2, startBubble.getY()));
					startBubble = bubble;
					startBubble.setOrigin(true);
				}
				
				child = bubbles.next();
			}
			
			removeBottomRow();
			calculatePositions();
			log.info("Finished inserting row");
		}

		protected void removeBottomRow() throws GameOver {
			Bubble current = getBottomLeft();
			while(current.hasRight()) {
				if(current instanceof ColouredBubble)
					throw new GameOver();
				if(current.hasTopLeft())
					current.getTopLeft().setBottomRight(null);
				if(current.hasTopRight())
					current.getTopRight().setBottomLeft(null);
				current = current.getRight();
			}
			
		}
		
		protected Bubble getBottomLeft() {
			Bubble current = startBubble;
			while(current.hasBottomLeft() || current.hasBottomRight()) {
				while(current.hasBottomRight())
					current = current.getBottomRight();
				while(current.hasBottomLeft())
					current = current.getBottomLeft();
			}
			while(current.hasLeft())
				current = current.getLeft();
			return current;
		}

		@Override
		public BubbleMeshIterator iterator() {
			return new BubbleMeshIterator();
		}
		
		public class BubbleMeshIterator implements Iterator<Bubble> {
			
			private Queue<Bubble> a = new LinkedList<Bubble>();
			private Queue<Bubble> b = new LinkedList<Bubble>();
			
			protected BubbleMeshIterator() {
				
				addRowToQueue(a, startBubble);
				
				while(!a.isEmpty()) {
					Bubble bubble = a.remove();
					b.add(bubble);
					
					Bubble firstChild = null;
					
					if(bubble.getBottomLeft() != null) {
						firstChild = bubble.getBottomLeft();
					}
					else if (bubble.getBottomRight() != null) {
						firstChild = bubble.getBottomRight();
					}
					
					if(firstChild != null && firstChild.getLeft() == null) {
						addRowToQueue(a, firstChild);
					}
				}
			}
			
			private void addRowToQueue(final Queue<Bubble> bubbles, final Bubble leftEdge) {
				Bubble left = leftEdge;
				bubbles.add(left);
				while(left.getRight() != null) {
					left = left.getRight();
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

		protected final Random RANDOM_GENERATOR = new Random();
		
		@Override
		public Color getRandomRemainingColor() {
			final int index = RANDOM_GENERATOR.nextInt(remainingColors.size());
			if(!remainingColors.isEmpty()) {
				return remainingColors.get(index);
			}
			else {
				throw new IllegalStateException("No colors available!");
			}
		}
		
		/**
		 * Remove a color from the remaining colors list
		 * @param color
		 */
		public void removeRemainingColor(final Color color) {
			remainingColors.remove(color);
		}

		@Override
		public void addScoreListener(final ScoreListener listener) {
			scoreListeners.add(listener);
		}

	}
}
