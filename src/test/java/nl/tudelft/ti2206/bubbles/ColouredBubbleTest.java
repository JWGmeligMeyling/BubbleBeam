package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ColouredBubbleTest {
	
	private ColouredBubble bubble;
	private Graphics2D graphics;
	
	@Before
	public void beforeClass() {
		bubble = new ColouredBubble(Color.RED);
		graphics = Mockito.mock(Graphics2D.class);
	}

	@Test
	public void test() {
		bubble.render(graphics);
		verify(graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

}
