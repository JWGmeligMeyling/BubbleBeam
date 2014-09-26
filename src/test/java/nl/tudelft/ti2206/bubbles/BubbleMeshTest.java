package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;

import java.awt.Color;
import java.util.Iterator;

import nl.tudelft.ti2206.bubbles.Bubble.Direction;
import nl.tudelft.ti2206.bubbles.BubbleMesh.BubbleMeshParser;
import nl.tudelft.ti2206.bubbles.BubbleMesh.BubbleMeshImpl;
import nl.tudelft.ti2206.bubbles.BubbleMesh.ScoreListener;

import org.junit.Test;

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
	
	@SuppressWarnings("unchecked")
	protected static <T> T testCast(Object obj, Class<T> clasz) {
		assertThat(obj, instanceOf(clasz));
		return (T) obj;
	}

}
