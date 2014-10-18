package nl.tudelft.ti2206.bubbles;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AbstractBubbleTest {

	protected Bubble bubble;
	
	protected Bubble mock;
	
	@Before
	public void setUp() throws Exception {
		bubble = new AbstractBubble();
		mock = mock(Bubble.class);
	}

	@Test
	public void testIsHittable() {
		assertFalse(bubble.isHittable());
	}
	
	@Test
	public void testGetNeighbours() {
		assertThat(bubble.getNeighbours(), empty());
		bubble.bind(Direction.TOPLEFT, mock);
		assertThat(bubble.getNeighbours(), hasItem(mock));
	}
	
	@Test
	public void testReplace() {
		bubble.bind(Direction.TOPLEFT, mock);
		Map<Direction, Bubble> connections = bubble.getConnections();
		AbstractBubble other = new AbstractBubble();
		bubble.replace(other);
		
		assertEquals(connections, other.getConnections());
		assertEquals(bubble.getPosition(), other.getPosition());
	}
	
	@Test
	public void testSetCenter() {
		Point center = new Point(5,5);
		bubble.setCenter(center);
		assertEquals(bubble.getCenter(), center);
	}
	
	@Test
	public void testBasicArithmethic() {
		int radius = bubble.getRadius();
		assertEquals(AbstractBubble.RADIUS, radius);
		assertEquals(radius * 2, bubble.getDiameter());
		assertEquals(Math.PI * 2 * radius, bubble.getSurface(), 1e-4);
	}
	
	@Test
	public void testTranslate() {
		Point point = mock(Point.class);
		bubble.setCenter(point);
		bubble.translate(5, 20);
		verify(point).translate(5, 20);
	}
	
	@Test
	public void testGetDistance() {
		Point a = new Point(0,0);
		Point b = new Point(1,1);
		
		bubble.setCenter(a);
		when(mock.getCenter()).thenReturn(b);
		
		Point am = mock(Point.class);
		when(am.distance(b)).thenReturn(a.distance(b));
		
		assertEquals(a.distance(b), bubble.getDistance(mock), 1e-4);
		verify(mock).getCenter();
		
	}
	
	@Test
	public void testTraverse() {
		bubble.bind(Direction.RIGHT, mock);
		Iterator<Bubble> iterator = bubble.traverse(Direction.RIGHT).iterator();
		assertEquals(bubble, iterator.next());
		assertEquals(mock, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testGetSnapPosition() {
		Bubble placeholder = new BubblePlaceholder();
		bubble.bind(Direction.BOTTOMRIGHT, placeholder);
		assertEquals(placeholder, bubble.getSnapPosition(mock));
	}
	
	@Test
	public void testGraphics() {
		Graphics2D graphics = mock(Graphics2D.class);
		
		Bubble bottomLeft = new AbstractBubble(),
				bottomRight = new AbstractBubble(),
				right = new AbstractBubble();
		
		bubble.bind(Direction.BOTTOMLEFT, bottomLeft);
		bubble.bind(Direction.BOTTOMRIGHT, bottomRight);
		bubble.bind(Direction.RIGHT, right);
		
		bottomLeft.calculatePosition();
		bottomRight.calculatePosition();
		right.calculatePosition();
		
		bubble.render(graphics);
	}
	
	@Test
	public void testPopsWith() {
		assertFalse(bubble.popsWith(mock));
	}
	
}
