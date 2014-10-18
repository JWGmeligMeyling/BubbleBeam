package nl.tudelft.ti2206.bubbles.decorators;

import static org.junit.Assert.*;
import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.AbstractBubbleTest;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;

import org.junit.Before;
import org.junit.Test;

public class SoundBubbleTest extends AbstractBubbleTest {

	protected SoundBubble soundBubble;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		bubble = soundBubble = new SoundBubble("sound", new AbstractBubble());
	}
	
	@Override
	@Test
	public void testGraphics() {}
	
}
