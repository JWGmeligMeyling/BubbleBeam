package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BubblePlaceholderTest {

	private BubblePlaceholder bubble;
	
	@Before
	public void setUp() throws Exception {
		bubble = new BubblePlaceholder();
	}


	@Test
	public void testGetSnapPosition() {
		assertEquals(bubble, bubble.getSnapPosition(bubble));
	}

}
