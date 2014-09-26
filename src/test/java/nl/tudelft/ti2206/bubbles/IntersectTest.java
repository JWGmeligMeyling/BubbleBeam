package nl.tudelft.ti2206.bubbles;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IntersectTest {

	private final static int RADIUS = 14, DIAMETER = RADIUS * 2;
	
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{ new Point(0,0), new Point(0,0), true },
			{ new Point(0,0), new Point(0,DIAMETER), true },
			{ new Point(0,0), new Point(DIAMETER,0), true },
			{ new Point(0,0), new Point(DIAMETER+1,0), false },
			{ new Point(0,0), new Point(0, DIAMETER+1), false },
			{ new Point(0,0), new Point(DIAMETER*2,0), false },
			{ new Point(0,0), new Point(0,DIAMETER*2), false }
		});
	}
	
	
	private final Point a, b;
	private final boolean outcome;
	
	private AbstractBubble bubble;	
	private Bubble mock;
	
	public IntersectTest(Point a, Point b, boolean outcome) {
		this.a = a;
		this.b = b;
		this.outcome = outcome;
	}
	
	@Before
	public void setUp() throws Exception {
		bubble = new AbstractBubble();
		mock = mock(Bubble.class);
	}

	@Test
	public void testIntersect() {
		bubble.setCenter(a);
		when(mock.getRadius()).thenReturn(RADIUS);
		when(mock.getCenter()).thenReturn(b);
		
		assertEquals(bubble.intersect(mock), outcome);
	}
	

}
