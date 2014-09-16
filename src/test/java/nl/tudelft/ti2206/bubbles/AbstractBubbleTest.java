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
