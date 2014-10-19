package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

/**
 * The {@code ColouredBubbleTest} tests the {@link ColouredBubble} class. The
 * test case extends the {@link AbstractBubbleTest} because most of the tests
 * for the {@link AbstractBubble} should pass for the {@code ColouredBubble}.
 * The very few test cases that - should - behave differently, are overridden.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class ColouredBubbleTest extends AbstractBubbleTest {

	private ColouredBubble colouredBubble;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		bubble = colouredBubble = new ColouredBubble(Color.RED);
	}
	
	@Test
	public void testRender() {
		Point paintStart = colouredBubble.getPaintStart();
		int diameter = colouredBubble.getDiameter();
		
		Graphics2D graphics = mock(Graphics2D.class);
		colouredBubble.render(graphics);
		verify(graphics).setColor(Color.RED);
		verify(graphics).fillOval(paintStart.x, paintStart.y, diameter, diameter);
	}

	@Test
	@Override
	public void testGetColor() {
		colouredBubble.setColor(Color.GREEN);
		assertEquals(Color.GREEN, colouredBubble.getColor());
	}
	
	@Test
	@Override
	public void testIsHittable() {
		assertTrue(colouredBubble.isHittable());
	}
	
	@Test
	@Override
	public void testPopsWith() {
		ColouredBubble sameColouredBubble = new ColouredBubble(Color.RED);
		colouredBubble.collideHook(sameColouredBubble);
		assertTrue(colouredBubble.popsWith(sameColouredBubble));
		
		ColouredBubble anotherColouredBubble = new ColouredBubble(Color.GREEN);
		assertFalse(colouredBubble.popsWith(anotherColouredBubble));
	}

}
