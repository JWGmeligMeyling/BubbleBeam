package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;

import org.junit.Before;
import org.junit.Test;

public class DrunkBubbleLeftTest extends AbstractBubbleTest {

	protected DrunkBubbleLeft drunkBubble;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		bubble = drunkBubble = new DrunkBubbleLeft(new AbstractBubble());
	}
	
	@Test
	public void testVelocityChange() {
		
	}

}
