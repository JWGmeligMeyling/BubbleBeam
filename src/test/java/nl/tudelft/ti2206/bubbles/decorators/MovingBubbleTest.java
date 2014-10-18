package nl.tudelft.ti2206.bubbles.decorators;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;
import nl.tudelft.util.Vector2f;

import org.junit.Before;
import org.junit.Test;

public class MovingBubbleTest {
	
	private MovingBubble movingBubble;
	private Point origin;
	private Vector2f velocity;
	private Dimension screenSize;
	
	@Before
	public void setUp() throws Exception {
		origin = mock(Point.class);
		velocity = new Vector2f(.5f, .5f);
		screenSize = new Dimension(300, 300);
		
		Bubble shootBubble = new ColouredBubble(Color.RED);
		shootBubble.setPosition(origin);
		movingBubble = new MovingBubble(velocity, screenSize, shootBubble);
		assertEquals(origin, movingBubble.getPosition());
	}
	
	@Test
	public void testGameTick() {
		Point expected = new Vector2f(origin.x + velocity.x, origin.y + velocity.y).toPoint();
		movingBubble.gameTick();
		assertEquals(expected, movingBubble.getPosition());
	}
	
	@Test
	public void testBounceOnWallCollisionLeft() {
		movingBubble.truePosition = new Vector2f(-1, 50);
		movingBubble.gameTick();
		assertEquals(-.5, velocity.x, 1e-4);
	}
	
	@Test
	public void testBounceOnWallCollisionRight() {
		movingBubble.truePosition = new Vector2f(movingBubble.getWidth() + screenSize.width, 50);
		movingBubble.gameTick();
		assertEquals(-.5, velocity.x, 1e-4);
	}
	
}
