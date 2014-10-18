package nl.tudelft.ti2206.bubbles.decorators;

import static org.junit.Assert.*;

import java.awt.Color;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.AbstractBubbleTest;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.JokerBubble;

import org.junit.Before;
import org.junit.Test;

/**
 * The {@code JokerBubbleTest} tests the {@link JokerBubble} class. The test case
 * extends the {@link AbstractBubbleTest} because most of the tests for the
 * {@link AbstractBubble} should pass for the {@code JokerBubble}. The very few
 * test cases that - should - behave differently, are overridden.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class JokerBubbleTest extends AbstractBubbleTest {
	
	protected JokerBubble joker;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		bubble = joker = new JokerBubble();
	}

	@Test
	public void testIsHittable() {
		assertTrue(bubble.isHittable());
	}
	
	@Test
	public void testPopsWith() {
		ColouredBubble colouredBubble = new ColouredBubble(Color.RED);
		assertTrue(bubble.popsWith(colouredBubble));
	}
	
	@Test
	public void testPopsWithAfterCollision() {
		ColouredBubble colouredBubble = new ColouredBubble(Color.RED);
		joker.collideHook(colouredBubble);
		joker.snapHook();
		
		assertTrue(bubble.popsWith(colouredBubble));
		
		ColouredBubble anotherColouredBubble = new ColouredBubble(Color.GREEN);
		assertFalse(bubble.popsWith(anotherColouredBubble));
	}

}
