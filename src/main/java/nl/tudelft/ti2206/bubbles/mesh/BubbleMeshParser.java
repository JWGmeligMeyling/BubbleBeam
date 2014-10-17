package nl.tudelft.ti2206.bubbles.mesh;

import java.awt.Color;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.Bubble.Direction;

import com.google.common.collect.Lists;

/**
 * A parser to parse {@link BubbleMesh BubbleMeshes} from a text file
 * 
 * @author Jan-Willem Gmelig Meyling
 */
public class BubbleMeshParser {
	
	protected final List<String> rows;
	protected final int rowAmount;
	protected final int rowSize;
	protected final Bubble[][] bubbles;
	
	/**
	 * Construct a new {@code BubbleMeshParser}
	 * @param rows
	 * 		The input for the parser
	 */
	public BubbleMeshParser(final List<String> rows) {
		this.rows = rows;
		this.rowAmount = rows.size();
		if (rowAmount < 1)
			throw new IllegalArgumentException("Wrong file format");
		this.rowSize = rows.get(0).length();
		this.bubbles = new Bubble[rowAmount][rowSize];
	}
	
	/**
	 * Parse
	 * @return the created {@link BubbleMesh}
	 */
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
		
		return new BubbleMeshImpl(bubbles[0][0], bubbles[rowAmount-1][0], rowSize);
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