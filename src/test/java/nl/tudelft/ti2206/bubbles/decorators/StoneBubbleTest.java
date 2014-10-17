package nl.tudelft.ti2206.bubbles.decorators;

import static org.junit.Assert.*;
import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.AbstractBubbleTest;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;

import org.junit.Before;
import org.junit.Test;

public class StoneBubbleTest extends AbstractBubbleTest {

	protected StoneBubble stoneBubble;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		bubble = stoneBubble = new StoneBubble(new AbstractBubble());
	}
	
	@Override
	@Test
	public void testGraphics() {}
	
	@Override
	@Test
	public void testIsHittable() {
		assertTrue(bubble.isHittable());
	}
	
}
