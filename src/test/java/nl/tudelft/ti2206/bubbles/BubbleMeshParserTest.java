package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.awt.Color;
import java.util.List;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMeshImpl;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMeshParser;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class BubbleMeshParserTest {

	@Test
	public void testParseColours() {
		BubbleMeshParser parser = new BubbleMeshParser(Lists.newArrayList("rgbcmy"));
		BubbleMeshImpl mesh = parser.parse();
		Bubble topLeft = mesh.getTopLeftBubble();

		List<Color> expected = ImmutableList.of(Color.RED, Color.GREEN,
				Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW);
		List<Color> result = Lists
				.newArrayList(topLeft.traverse(Direction.RIGHT)
						.map(bubble -> ((ColouredBubble) bubble).getColor())
						.iterator());
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testParseRandomColours() {
		BubbleMeshParser parser = new BubbleMeshParser(Lists.newArrayList(
				"xxxxxxxxxx", "xxxxxxxxxx", "xxxxxxxxxx", "xxxxxxxxxx"));
		BubbleMeshImpl mesh = parser.parse();
		Bubble topLeft = mesh.getTopLeftBubble();

		Integer amount = (int) topLeft.traverse(Direction.RIGHT)
			.map(bubble -> ((ColouredBubble) bubble).getColor())
			.distinct().count();
		
		assertThat(amount, greaterThan(2));
	}
	
	@Test
	public void testParsePlaceHolders() {
		BubbleMeshParser parser = new BubbleMeshParser(Lists.newArrayList(" "));
		BubbleMeshImpl mesh = parser.parse();
		Bubble topLeft = mesh.getTopLeftBubble();

		
		assertThat(topLeft, instanceOf(BubblePlaceholder.class));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidInput() {
		new BubbleMeshParser(Lists.newArrayList()).parse();
	}

}
