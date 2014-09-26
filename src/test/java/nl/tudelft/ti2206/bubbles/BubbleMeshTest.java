package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;

import nl.tudelft.ti2206.bubbles.Bubble.Direction;
import nl.tudelft.ti2206.bubbles.BubbleMesh.BubbleMeshParser;
import nl.tudelft.ti2206.bubbles.BubbleMesh.BubbleMeshImpl;
import nl.tudelft.ti2206.bubbles.BubbleMesh.ScoreListener;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.game.GameController;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class BubbleMeshTest {
	
	@Test
	public void testNoPop() {
		BubbleMeshImpl bubbleMesh = spy(new BubbleMeshParser(Lists.newArrayList("rrbgggbbb ", "          ")).parse());
		ScoreListener scoreListener = mock(ScoreListener.class);
		bubbleMesh.addScoreListener(scoreListener);

		Iterator<Bubble> iterator = bubbleMesh.getTopLeftBubble()
				.traverse(Direction.RIGHT).iterator();
		
		ColouredBubble a = testCast(iterator.next(), ColouredBubble.class);
		ColouredBubble b = testCast(iterator.next(), ColouredBubble.class);
		ColouredBubble c = testCast(iterator.next(), ColouredBubble.class);
		
		assertFalse(bubbleMesh.pop(a));
		verify(bubbleMesh).pop(a);
		verify(bubbleMesh).pop(eq(b), anySetOf(ColouredBubble.class));
		
		verify(bubbleMesh, never()).pop(eq(c), any());
		verify(bubbleMesh, never()).replaceBubble(any(), any());
		
		verify(scoreListener, never()).incrementScore(anyInt());
	}

	@Test
	public void testBasicPop() {
		BubbleMeshImpl bubbleMesh = spy(new BubbleMeshParser(Lists.newArrayList("rrrgggbbb ", "          ")).parse());
		ScoreListener scoreListener = mock(ScoreListener.class);
		bubbleMesh.addScoreListener(scoreListener);
		
		Iterator<Bubble> iterator = bubbleMesh.getTopLeftBubble()
				.traverse(Direction.RIGHT).iterator();
		
		ColouredBubble a = testCast(iterator.next(), ColouredBubble.class);
		ColouredBubble b = testCast(iterator.next(), ColouredBubble.class);
		ColouredBubble c = testCast(iterator.next(), ColouredBubble.class);
		
		assertTrue(bubbleMesh.pop(a));
		verify(bubbleMesh).pop(a);
		verify(bubbleMesh).pop(eq(b), anySetOf(ColouredBubble.class));
		verify(bubbleMesh).pop(eq(c), anySetOf(ColouredBubble.class));
		
		verify(bubbleMesh).replaceBubble(eq(a), any(BubblePlaceholder.class));
		verify(bubbleMesh).replaceBubble(eq(b), any(BubblePlaceholder.class));
		verify(bubbleMesh).replaceBubble(eq(c), any(BubblePlaceholder.class));

		assertThat(
				bubbleMesh.getRemainingColours(),
				allOf(hasItems(Color.GREEN, Color.BLUE),
						not(hasItem(Color.RED))));
		
		verify(scoreListener).incrementScore(anyInt());
	}
	
	@Test
	public void testPopIsolatedBubbles() {
		BubbleMeshImpl bubbleMesh = spy(new BubbleMeshParser(Lists.newArrayList("rrrcccbbb ", "g         ", "          ")).parse());
		ScoreListener scoreListener = mock(ScoreListener.class);
		bubbleMesh.addScoreListener(scoreListener);
		
		Iterator<Bubble> iterator = bubbleMesh.getTopLeftBubble()
				.traverse(Direction.RIGHT).iterator();
		
		ColouredBubble a = testCast(iterator.next(), ColouredBubble.class);
		ColouredBubble b = testCast(iterator.next(), ColouredBubble.class);
		ColouredBubble c = testCast(iterator.next(), ColouredBubble.class);
		ColouredBubble d = (ColouredBubble) a.getBubbleAt(Direction.BOTTOMRIGHT);
		
		assertTrue(bubbleMesh.pop(b));
		verify(bubbleMesh).pop(b);
		verify(bubbleMesh).pop(eq(a), anySetOf(ColouredBubble.class));
		verify(bubbleMesh).pop(eq(c), anySetOf(ColouredBubble.class));
		
		verify(bubbleMesh).replaceBubble(eq(a), any(BubblePlaceholder.class));
		verify(bubbleMesh).replaceBubble(eq(b), any(BubblePlaceholder.class));
		verify(bubbleMesh).replaceBubble(eq(c), any(BubblePlaceholder.class));
		verify(bubbleMesh).replaceBubble(eq(d), any(BubblePlaceholder.class));
		
		assertThat(
				bubbleMesh.getRemainingColours(),
				allOf(hasItems(Color.BLUE, Color.CYAN),
						not(hasItems(Color.RED, Color.GREEN))));
		
		verify(scoreListener).incrementScore(anyInt());
	}
	
	@Test
	public void testBubbleMeshIterator() {
		Bubble a = spy(new AbstractBubble());
		Bubble b = spy(new AbstractBubble());
		Bubble c = spy(new AbstractBubble());
		
		a.bind(Direction.RIGHT, b);
		a.bind(Direction.BOTTOMRIGHT, c);
		b.bind(Direction.BOTTOMLEFT, c);
		BubbleMeshImpl bubbleMesh = new BubbleMeshImpl(a,c);
		
		Iterator<Bubble> iterator = bubbleMesh.iterator();
		assertEquals(ImmutableList.of(a, b, c), ImmutableList.copyOf(iterator));
	}
	
	@Test
	public void testCalculatePositions() {
		Bubble a = spy(new AbstractBubble());
		Bubble b = spy(new AbstractBubble());
		Bubble c = spy(new AbstractBubble());
		
		a.bind(Direction.RIGHT, b);
		a.bind(Direction.BOTTOMRIGHT, c);
		b.bind(Direction.BOTTOMLEFT, c);
		BubbleMeshImpl bubbleMesh = new BubbleMeshImpl(a,c);
		
		bubbleMesh.calculatePositions();
		verify(a, never()).calculatePosition();
		verify(b).calculatePosition();
		verify(c).calculatePosition();
		
		verify(b).setPosition(any());
		verify(c).setPosition(any());
	}
	
	@Test
	public void testInsertRow() {
		BubbleMeshImpl bubbleMesh = spy(new BubbleMeshParser(
				Lists.newArrayList("rrrcccbbb ", "g         ", "          ",
						"          ")).parse());
		assertStronglyConnected(bubbleMesh);
		
		GameController gameController = mock(GameController.class);
		when(gameController.getRandomRemainingColor()).thenReturn(Color.RED);
		bubbleMesh.insertRow(gameController);

		Iterable<Color> colors = Iterables.transform(
				Iterables.filter(bubbleMesh, ColouredBubble.class),
				ColouredBubble::getColor);

		assertStronglyConnected(bubbleMesh);
		assertEquals(Arrays.asList(Color.RED, Color.RED, Color.RED, Color.RED,
				Color.RED, Color.RED, Color.RED, Color.RED, Color.RED,
				Color.RED, Color.RED, Color.RED, Color.RED, Color.CYAN,
				Color.CYAN, Color.CYAN, Color.BLUE, Color.BLUE, Color.BLUE,
				Color.GREEN), ImmutableList.copyOf(colors));
		
		verify(bubbleMesh).updateBottomRow();
		verify(bubbleMesh).calculatePositions();
	}
	
	@Test
	public void testInsertRowTwice() {
		BubbleMeshImpl bubbleMesh = new BubbleMeshParser(
				Lists.newArrayList("rrrcccbbb ", "g         ", "          ",
						"          ", "          ")).parse();
		
		GameController gameController = mock(GameController.class);
		when(gameController.getRandomRemainingColor()).thenReturn(Color.RED);
		bubbleMesh.insertRow(gameController);
		bubbleMesh.insertRow(gameController);
		assertStronglyConnected(bubbleMesh);
	}
	
	@Test(expected=GameOver.class)
	public void testGameOverByInsert() {
		BubbleMeshImpl bubbleMesh = new BubbleMeshParser(Lists.newArrayList(
				"rrrcccbbb ", "g         ", "          ")).parse();
		
		GameController gameController = mock(GameController.class);
		when(gameController.getRandomRemainingColor()).thenReturn(Color.RED);
		
		bubbleMesh.insertRow(gameController);
	}
	
	@Test(expected=GameOver.class)
	public void testGameOverByShoot() {
		Bubble a = spy(new AbstractBubble());
		Bubble b = spy(new AbstractBubble());
		Bubble c = spy(new BubblePlaceholder());
		
		a.bind(Direction.RIGHT, b);
		a.bind(Direction.BOTTOMRIGHT, c);
		b.bind(Direction.BOTTOMLEFT, c);

		BubbleMeshImpl bubbleMesh = new BubbleMeshImpl(a,c);
		bubbleMesh.replaceBubble(c, new AbstractBubble());
	}
	
	public static void assertStronglyConnected(BubbleMesh bubbleMesh) {
		for(Bubble bubble : bubbleMesh)
			bubble.getConnections().entrySet().forEach(connection -> {
				Direction direction = connection.getKey();
				Bubble other = connection.getValue();
				assertEquals(bubble, other.getBubbleAt(direction.opposite()));
			});
	}
	
	protected static <T> T testCast(Object obj, Class<T> clasz) {
		assertThat(obj, instanceOf(clasz));
		return clasz.cast(obj);
	}

}
