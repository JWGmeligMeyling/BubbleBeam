package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class BindingTest {

	@Parameters(name="testBind({0})")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ Direction.TOPLEFT },
				{ Direction.TOPRIGHT  },
				{ Direction.LEFT },
				{ Direction.RIGHT },
				{ Direction.BOTTOMLEFT },
				{ Direction.BOTTOMRIGHT } });
	}

	private final Direction direction, oppositeDirection;
	private final AbstractBubble bubble;
	private final Bubble mock;

	public BindingTest(final Direction direction) {
		this.direction = direction;
		this.oppositeDirection = direction.opposite();
		bubble = new AbstractBubble();
		mock = Mockito.mock(Bubble.class);
	}

	@Test
	public void testBind() {
		assertFalse(bubble.hasBubbleAt(direction));
		bubble.bind(direction, mock);
		assertTrue(bubble.hasBubbleAt(direction));
		assertEquals(mock, bubble.getBubbleAt(direction));
		verify(mock).setBubbleAt(oppositeDirection, bubble);
	}

}
