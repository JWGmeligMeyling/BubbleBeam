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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public interface BubbleMesh extends Iterable<Bubble> {
	
	default Stream<Bubble> stream() {
	    return StreamSupport.stream(spliterator(), false);
	}

	Color getRandomRemainingColor();
	
	/**
	 * Pop this bubble and it's neighbors recursively
	 * @return true iff bubbles popped
	 */
	boolean pop(ColouredBubble target);

	void calculatePositions();

	void insertRow();

	void replaceBubble(Bubble original, Bubble other);

	void removeRemainingColor(Color color);
	
	void addScoreListener(ScoreListener listener);
	
	interface ScoreListener {
		void incrementScore(int amount);
	}
	
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
				else {
					bubble.setTop();
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
				log.info("Changed bubble position to {}", newPosition);
			}
		}

		@Override
		public void replaceBubble(final Bubble original, final Bubble other){
			other.bindTopLeft(original.getTopLeft());
			other.bindTopRight(original.getTopRight());
			other.bindLeft(original.getLeft());
			other.bindRight(original.getRight());
			other.bindBottomLeft(original.getBottomLeft());
			other.bindBottomRight(original.getBottomRight());
			other.setPosition(original.getPosition());
			
			if(startBubble.equals(original)) {
				startBubble = other;
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
		protected boolean pop(final ColouredBubble target, final Set<ColouredBubble> bubblesToPop) {
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
					.filter(bubble -> !bubble.connectedToTop(Sets.newHashSet(bubblesToPop)) && bubblesToPop.add(bubble))
					.forEach(bubble -> this.pop(bubble, bubblesToPop));
			}
			
			return popped;
		}

		protected void updateRemainingColors() {
			remainingColors.removeIf(color -> this.stream()
				.filter(bubble -> bubble instanceof ColouredBubble)
				.map(bubble -> (ColouredBubble) bubble)
				.map(ColouredBubble::getColor)
				.distinct().noneMatch(a -> a.equals(color)));
		}
		
		protected void calculateScore(final Set<ColouredBubble> bubbles) {
			int amount = bubbles.size() * bubbles.size() * 25;
			scoreListeners.forEach(
					scoreListener -> scoreListener.incrementScore(amount));
		}

		@Override
		public void insertRow() {
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
			
			calculatePositions();
			log.info("Finished inserting row");
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
		
		@Override
		public void removeRemainingColor(final Color color) {
			remainingColors.remove(color);
		}

		@Override
		public void addScoreListener(final ScoreListener listener) {
			scoreListeners.add(listener);
		}

	}
}
