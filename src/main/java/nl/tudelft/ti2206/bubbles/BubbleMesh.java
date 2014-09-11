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
import java.util.Observable;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class BubbleMesh extends Observable implements Iterable<Bubble> {
	
	private static final Logger log = LoggerFactory.getLogger(BubbleMesh.class);

	private final Set<Color> remainingColors = Sets.newHashSet(Color.RED,
			Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW);

	private final List<Bubble> startBubbles;
	
	public static BubbleMesh parse(File file) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)))) {
			List<String> lines = Lists.newArrayList();
			while (reader.ready()) {
				lines.add(reader.readLine());
			}
			return parse(lines);
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
		BubbleMesh result = new BubbleMesh();
		
		for(int i = 0; i < rowAmount; i++) {
			String rowStr = rows.get(i);
			for(int j = 0; j < rowSize; j++) {
				Bubble bubble = bubbles[i][j] = rowStr.charAt(j) == 'x' ? new ColouredBubble(
						result.getRandomRemainingColor())
						: new BubblePlaceholder();
				
				if(i > 0) {
					if(i % 2 == 0) { // 3rd, 5fth rows , ... 
						bubble.bindTopRight(bubbles[i-1][j]);
						if(j > 1) {
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
			}
		}
		
		result.startBubbles.add(bubbles[0][0]);
		return result;
	}
	
	public BubbleMesh() {
		this.startBubbles = Lists.newArrayList();
	}

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
			}
			
			if(previousBubble != null) {
				previousBubble.bindRight(bubble);
			}
			previousBubble = bubble;
			
			if(i == 0) {
				if(shift)
					bubble.getPosition().translate(AbstractBubble.WIDTH / 2, 0);
				this.startBubbles.clear();
				this.startBubbles.add(bubble);
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
	
	public void calculatePositions() {
		for(Bubble bubble : this) {
			Point newPosition = bubble.calculatePosition();
			bubble.setPosition(newPosition);
			log.info("Changed bubble position to {}", newPosition);
		}
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public class BubbleMeshIterator implements Iterator<Bubble> {
		
		private Queue<Bubble> a = new LinkedList<Bubble>();
		private Queue<Bubble> b = new LinkedList<Bubble>();
		
		protected BubbleMeshIterator() {
			
			for(Bubble startBubble : startBubbles) {
				addRowToQueue(a, startBubble);
			}
			
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

	public Color getRandomRemainingColor() {
		final int index = RANDOM_GENERATOR.nextInt(remainingColors.size());
		int i = 0;
		
		for(Color color : remainingColors) {
			if(i == index) {
				return color;
			}
			i++;
		}
		
		throw new IllegalStateException("No colors available!");
	}
	
	public void removeRemainingColor(final Color color) {
		remainingColors.remove(color);
	}

}
