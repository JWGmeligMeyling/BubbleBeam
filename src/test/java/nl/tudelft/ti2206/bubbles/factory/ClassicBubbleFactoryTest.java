package nl.tudelft.ti2206.bubbles.factory;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.ColouredBubble;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(Parameterized.class)
public class ClassicBubbleFactoryTest {
	
	@Parameters(name="createBubble({0})={1}")
    public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { 0, Color.RED },
				{ 1, Color.GREEN }, { 2, Color.BLUE } });
    }
	
	protected ClassicBubbleFactory factory;
	protected Random random;
	protected Set<Color> remainingColors;
	
	protected final int index;
	protected final Color expected;

	@Before
	public void setUp() throws Exception {
		random = Mockito.mock(Random.class);
		factory = new ClassicBubbleFactory(random);
		remainingColors = Sets.newLinkedHashSet(Lists.newArrayList(Color.RED, Color.GREEN, Color.BLUE));
	}
	
	public ClassicBubbleFactoryTest(int index, Color expected) {
		this.index = index;
		this.expected = expected;
	}

	@Test
	public void testCreateBubble() {
		setRandom(remainingColors.size(), index);
		ColouredBubble bubble = factory.createBubble(remainingColors);
		assertNotNull(bubble);
		assertEquals(expected, bubble.getColor());
	}
	
	protected void setRandom(int bound, int result) {
		Mockito.when(random.nextInt(bound)).thenReturn(result);
	}

}
