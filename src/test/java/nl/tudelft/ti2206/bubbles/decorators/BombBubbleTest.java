package nl.tudelft.ti2206.bubbles.decorators;

import static org.junit.Assert.*;
import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.AbstractBubbleTest;
import nl.tudelft.ti2206.bubbles.decorators.BombBubble;
import nl.tudelft.ti2206.bubbles.pop.RadialPopBehaviour;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

/**
 * The {@code BombBubbleTest} tests the {@link BombBubble} class. The test case
 * extends the {@link AbstractBubbleTest} because most of the tests for the
 * {@link AbstractBubble} should pass for the {@code BombBubble}. The very few
 * test cases that - should - behave differently, are overridden.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class BombBubbleTest extends AbstractBubbleTest {
	
	protected BombBubble bombBubble;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		bubble = bombBubble = new BombBubble();
	}

	@Test
	public void testPopBehaviour() {
		assertThat(bubble.getPopBehaviour(), Matchers.instanceOf(RadialPopBehaviour.class));
	}

	@Test
	@Override
	public void testIsHittable() {
		assertTrue(bubble.isHittable());
	}

	@Test
	@Override
	public void testPopsWith() {
		assertTrue(bubble.popsWith(mock));
	}
	
}
