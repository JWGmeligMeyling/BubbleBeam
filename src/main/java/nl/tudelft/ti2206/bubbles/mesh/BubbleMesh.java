package nl.tudelft.ti2206.bubbles.mesh;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameOver;
import nl.tudelft.ti2206.game.event.BubbleMeshListener;
import nl.tudelft.ti2206.graphics.Sprite;
import nl.tudelft.util.EventTarget;

import com.google.common.collect.Lists;

/**
 * A mesh structure of {@link Bubble Bubbles}
 *  
 * @author Jan-Willem Gmelig Meyling
 */
public interface BubbleMesh extends Iterable<Bubble>, Sprite, Serializable, EventTarget<BubbleMeshListener> {
	
	default Stream<Bubble> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
	
	/**
	 * Pop this bubble and it's neighbors recursively.
	 * 
	 * @param target
	 *            {@code Bubble} to start popping at
	 * @return true iff bubbles popped
	 */
	boolean pop(Bubble target);
	
	/**
	 * Calculate the positions for the bubbles relative to the origin (top left
	 * bubble)
	 */
	void calculatePositions();
	
	/**
	 * Insert a new row of bubbles at the top
	 * 
	 * @param gameController
	 *            A link to the current {@link GameController} in order to
	 *            determine the {@code Colors} for the new row
	 * @throws GameOver
	 *             if a new row can't be inserted, the game is over
	 * @see GameController#getRandomRemainingColor()
	 */
	default void insertRow(GameController gameController) throws GameOver {
		insertRow(new Iterator<Bubble>() {

			@Override
			public boolean hasNext() {
				return !isEmpty();
			}

			@Override
			public Bubble next() {
				return new ColouredBubble(gameController.getRandomRemainingColor());
			}
			
		});
	}
	
	/**
	 * Insert a new row of bubbles at the top
	 * 
	 * @param bubbleProvider
	 *            A Iterator containing Bubbles
	 * @throws GameOver
	 *             if a new row can't be inserted, the game is over
	 * @see GameController#getRandomRemainingColor()
	 */
	void insertRow(Iterator<Bubble> bubbleProvider) throws GameOver;
	
	/**
	 * Replace a {@code Bubble} in the mesh for another {@code Bubble}
	 * 
	 * @param original
	 *            {@code Bubble} to be replaced
	 * @param replacement
	 *            {@code Bubble} to replace the other bubble with
	 */
	void replaceBubble(Bubble original, Bubble replacement);
	
	/**
	 * Check if a {@code Bubble} is at the top row
	 * 
	 * @param target
	 *            {@code Bubble} to check for
	 * @return true if the given {@link Bubble} is in the top row
	 */
	boolean bubbleIsTop(Bubble target);
	
	/**
	 * @return Get a List of colors that still exist in the mesh
	 */
	List<Color> getRemainingColours();
	
	/**
	 * @return true if the {@code BubbleMesh} is empty
	 */
	boolean isEmpty();

	/**
	 * @return the top left {@link Bubble} in the mesh
	 */
	Bubble getTopLeftBubble();

	/**
	 * @return the bottom left {@link Bubble} in the mesh
	 */
	Bubble getBottomLeftBubble();

	/**
	 * Parse a {@code BubbleMesh} from an {@code InputStream}
	 * 
	 * @param inputstream
	 *            {@code InputStream}
	 * @return The parsed {@code BubbleMesh}
	 * @throws IOException
	 *             If an I/O error occurs
	 */
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
	
	/**
	 * Parse a {@code BubbleMesh} from an {@code String}
	 * 
	 * @param string
	 *            {@code String} to parse
	 * @return The parsed {@code BubbleMesh}
	 */
	public static BubbleMesh parse(final String string) {
		return parse(Arrays.asList(string.split("[\r\n]+")));
	}
	
	/**
	 * Parse a {@code BubbleMesh} from a {@code List} of {@code Strings}.
	 * 
	 * @param rows
	 *            {@code List} of {@code Strings} to parse
	 * @return The parsed {@code BubbleMesh}
	 */
	public static BubbleMesh parse(final List<String> rows) {
		return new BubbleMeshParser(rows).parse();
	}

}
