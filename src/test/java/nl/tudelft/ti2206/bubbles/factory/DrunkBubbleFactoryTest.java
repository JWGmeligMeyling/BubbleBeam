package nl.tudelft.ti2206.bubbles.factory;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleRight;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import com.google.common.collect.Sets;

@RunWith(Parameterized.class)
public class DrunkBubbleFactoryTest {

	@Parameters(name="createBubble({0},{2})=({1},{3})")
    public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ 0, Color.RED, 1, DrunkBubbleLeft.class },
				{ 1, Color.GREEN, 1, DrunkBubbleLeft.class },
				{ 2, Color.BLUE, 1, DrunkBubbleLeft.class },
				{ 0, Color.RED, 0, DrunkBubbleRight.class },
				{ 1, Color.GREEN, 0, DrunkBubbleRight.class },
				{ 2, Color.BLUE, 0, DrunkBubbleRight.class } });
    }
	
	
	protected DrunkBubbleFactory factory;
	protected Random random;
	protected Set<Color> remainingColors;
	
	@Before
	public void setUp() throws Exception {
		random = Mockito.mock(Random.class);
		factory = new DrunkBubbleFactory(random);
		remainingColors = Sets.newLinkedHashSet();
		remainingColors.add(Color.RED);
		remainingColors.add(Color.GREEN);
		remainingColors.add(Color.BLUE);
	}
	
	protected final int colorIndex, effectIndex;
	protected final Color expectedColor;
	protected final Class<?> expectedType;
	
	public DrunkBubbleFactoryTest(int colorIndex, Color expectedColor,
			int effectIndex, Class<?> expectedType) {
		this.colorIndex = colorIndex;
		this.effectIndex = effectIndex;
		this.expectedColor = expectedColor;
		this.expectedType = expectedType;
	}

	@Test
	public void testCreateBubble() {
		setRandom(remainingColors.size(), colorIndex);
		setRandom(2, effectIndex);
		Bubble bubble = factory.createBubble(remainingColors);
		assertNotNull(bubble);
		assertThat(bubble, Matchers.instanceOf(expectedType));
		assertEquals(expectedColor, bubble.getColor());
	}
	
	protected void setRandom(int bound, int result) {
		Mockito.when(random.nextInt(bound)).thenReturn(result);
	}
	
}
