package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

public class ColouredBubbleTest {

	private ColouredBubble bubble;
	
	@Before
	public void setUp() throws Exception {
		bubble = new ColouredBubble(Color.RED);
	}
	
	@Test
	public void testRender() {
		Point paintStart = bubble.getPaintStart();
		int diameter = bubble.getDiameter();
		
		Graphics2D graphics = mock(Graphics2D.class);
		bubble.render(graphics);
		verify(graphics).setColor(Color.RED);
		verify(graphics).fillOval(paintStart.x, paintStart.y, diameter, diameter);
	}

	@Test
	public void testGetColor() {
		bubble.setColor(Color.GREEN);
		assertEquals(Color.GREEN, bubble.getColor());
	}
	
	@Test
	public void testIsHittable() {
		assertTrue(bubble.isHittable());
	}

}
