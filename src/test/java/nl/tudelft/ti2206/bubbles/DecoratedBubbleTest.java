package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DecoratedBubbleTest {

	protected AbstractBubble bubble;
	protected DecoratedBubble decoratedBubble;
	
	@Before
	public void setUp() throws Exception {
		bubble = Mockito.mock(AbstractBubble.class);
		decoratedBubble = new DecoratedBubble(bubble);
	}

	@Test
	public void testCalculatePosition() {
		decoratedBubble.calculatePosition();
		Mockito.verify(bubble).calculatePosition();
	}
	
	@Test
	public void testCollideHook() {
		Bubble mock = Mockito.mock(Bubble.class);
		decoratedBubble.collideHook(mock);
		Mockito.verify(bubble).collideHook(mock);
	}
	
	@Test
	public void testSnapHook() {
		decoratedBubble.snapHook();
		Mockito.verify(bubble).snapHook();
	}
	
	@Test
	public void testPopHook() {
		decoratedBubble.popHook();
		Mockito.verify(bubble).popHook();
	}
	
	@Test
	public void testGetBubbleAt() {
		decoratedBubble.getBubbleAt(Direction.LEFT);
		Mockito.verify(bubble).getBubbleAt(Direction.LEFT);
	}
	
	@Test
	public void testGetCenter() {
		decoratedBubble.getCenter();
		Mockito.verify(bubble).getCenter();}
	
	@Test
	public void testGetColor() {
		decoratedBubble.getColor();
		Mockito.verify(bubble).getColor();
	}
	
	@Test
	public void testHasColor() {
		decoratedBubble.hasColor();
		Mockito.verify(bubble).hasColor();
	}
	
	@Test
	public void testGetConnections() {
		decoratedBubble.getConnections();
		Mockito.verify(bubble).getConnections();
	}
	
	@Test
	public void testGetHeight() {
		decoratedBubble.getHeight();
		Mockito.verify(bubble).getHeight();
	}
	
	@Test
	public void testGetNeighbours() {
		decoratedBubble.getNeighbours();
		Mockito.verify(bubble).getNeighbours();
	}
	
	@Test
	public void testGetParent() {
		assertEquals(bubble, decoratedBubble.getParent());
	}
	
	@Test
	public void testGetPopBehaviour() {
		decoratedBubble.getPopBehaviour();
		Mockito.verify(bubble).getPopBehaviour();
	}
	
	@Test
	public void testGetPosition() {
		decoratedBubble.getPosition();
		Mockito.verify(bubble).getPosition();
	}
	
	@Test
	public void testGetRadius() {
		decoratedBubble.getRadius();
		Mockito.verify(bubble).getRadius();
	}
	
	@Test
	public void testGetWidth() {
		decoratedBubble.getWidth();
		Mockito.verify(bubble).getWidth();
	}
	
	@Test
	public void testGetX() {
		decoratedBubble.getX();
		Mockito.verify(bubble).getX();
	}
	
	@Test
	public void testGetY() {
		decoratedBubble.getY();
		Mockito.verify(bubble).getY();
	}
	
	@Test
	public void testGetSnapPosition() {
		Bubble mock = Mockito.mock(Bubble.class);
		decoratedBubble.getSnapPosition(mock);
		Mockito.verify(bubble).getSnapPosition(mock);
	}
	
	@Test
	public void testVelocityChange() {
		decoratedBubble.velocityChange();
		Mockito.verify(bubble).velocityChange();
	}

}
