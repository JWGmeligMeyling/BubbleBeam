package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractBubbleTest {

	private AbstractBubble bubble;
	private Bubble other;
	
	@Before
	public void beforeClass() {
		bubble = new AbstractBubble() {

			@Override
			public void render(Graphics graphics) {
				throw new UnsupportedOperationException();
			}
			
		};
		
		other = Mockito.mock(Bubble.class);
	}
	
	@Test
	public void testPosition() {
		Point point = new Point(1,2);
		bubble.setPosition(point);
		assertEquals(bubble.getPosition(), point);
		assertEquals(bubble.getX(), 1);
		assertEquals(bubble.getY(), 2);
	}
	
	@Test
	public void testNullBindings() {
		bubble.bindLeft(null);
		bubble.bindRight(null);
		bubble.bindTopLeft(null);
		bubble.bindTopRight(null);
	}
	
	@Test
	public void testBottomLeftAccessors() {
		assertNull(bubble.getBottomLeft());
		assertFalse(bubble.hasBottomLeft());
		bubble.setBottomLeft(other);
		assertEquals(other, bubble.getBottomLeft());
		assertTrue(bubble.hasBottomLeft());
	}
	
	@Test
	public void testBottomRightAccessors() {
		assertNull(bubble.getBottomRight());
		assertFalse(bubble.hasBottomRight());
		bubble.setBottomRight(other);
		assertEquals(other, bubble.getBottomRight());
		assertTrue(bubble.hasBottomRight());
	}
	
	@Test
	public void testLeftAccessors() {
		assertNull(bubble.getLeft());
		assertFalse(bubble.hasLeft());
		bubble.setLeft(other);
		assertEquals(other, bubble.getLeft());
		assertTrue(bubble.hasLeft());
	}
	
	@Test
	public void testRightAccessors() {
		assertNull(bubble.getRight());
		assertFalse(bubble.hasRight());
		bubble.setRight(other);
		assertEquals(other, bubble.getRight());
		assertTrue(bubble.hasRight());
	}
	
	@Test
	public void testTopLeftAccessors() {
		assertNull(bubble.getTopLeft());
		assertFalse(bubble.hasTopLeft());
		bubble.setTopLeft(other);
		assertEquals(other, bubble.getTopLeft());
		assertTrue(bubble.hasTopLeft());
	}
	
	@Test
	public void testTopRightAccessors() {
		assertNull(bubble.getTopRight());
		assertFalse(bubble.hasTopRight());
		bubble.setTopRight(other);
		assertEquals(other, bubble.getTopRight());
		assertTrue(bubble.hasTopRight());
	}
	
	@Test
	public void testBindTopLeft() {
		bubble.bindTopLeft(other);
		verify(other).setBottomRight(bubble);
		assertEquals(other, bubble.getTopLeft());
	}

	@Test
	public void testBindTopRight() {
		bubble.bindTopRight(other);
		verify(other).setBottomLeft(bubble);
		assertEquals(other, bubble.getTopRight());
	}

	@Test
	public void testBindBottomLeft() {
		bubble.bindBottomLeft(other);
		verify(other).setTopRight(bubble);
		assertEquals(other, bubble.getBottomLeft());
	}
	
	@Test
	public void testBindBottomRight() {
		bubble.bindBottomRight(other);
		verify(other).setTopLeft(bubble);
		assertEquals(other, bubble.getBottomRight());
	}
	
	@Test
	public void testBindLeft() {
		bubble.bindLeft(other);
		verify(other).setRight(bubble);
		assertEquals(other, bubble.getLeft());
	}
	
	@Test
	public void testBindRight() {
		bubble.bindRight(other);
		verify(other).setLeft(bubble);
		assertEquals(other, bubble.getRight());
	}
	
	@Test
	public void testCalculateTopLeft() {
		when(other.getX()).thenReturn(5);
		when(other.getY()).thenReturn(6);
		bubble.bindTopLeft(other);
		Point position = bubble.calculatePosition();
		Point expectation = new Point(5 + AbstractBubble.WIDTH / 2, 6 + AbstractBubble.HEIGHT);
		assertEquals(expectation, position);
	}

	@Test
	public void testCalculateTopRight() {
		when(other.getX()).thenReturn(5);
		when(other.getY()).thenReturn(6);
		bubble.bindTopRight(other);
		Point position = bubble.calculatePosition();
		Point expectation = new Point(5 - AbstractBubble.WIDTH / 2, 6 + AbstractBubble.HEIGHT);
		assertEquals(expectation, position);
	}
	
	@Test
	public void testCalculateLeft() {
		when(other.getX()).thenReturn(5);
		when(other.getY()).thenReturn(6);
		bubble.bindLeft(other);
		Point position = bubble.calculatePosition();
		Point expectation = new Point(5 + AbstractBubble.WIDTH, 6);
		assertEquals(expectation, position);
	}
	
	@Test
	public void testCalculateFallback() {
		Point position = bubble.getPosition();
		bubble.calculatePosition();
		assertEquals(position, bubble.getPosition());
	}
	
}
