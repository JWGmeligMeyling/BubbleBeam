package nl.tudelft.ti2206.bubbles.factory;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.BombBubble;
import nl.tudelft.ti2206.bubbles.decorators.DrunkBubbleLeft;
import nl.tudelft.ti2206.bubbles.decorators.JokerBubble;
import nl.tudelft.ti2206.bubbles.decorators.StoneBubble;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Sets;

public class PowerUpBubbleFactoryTest {

	protected PowerUpBubbleFactory factory;
	protected Random random;
	protected Set<Color> remainingColors;
	
	@Before
	public void setUp() throws Exception {
		random = Mockito.mock(Random.class);
		factory = new PowerUpBubbleFactory(random);
		remainingColors = Sets.newLinkedHashSet();
		remainingColors.add(Color.RED);
		remainingColors.add(Color.GREEN);
		remainingColors.add(Color.BLUE);
	}
	
	private static final int PRIMARY_EFFECT = 3;
	private static final int EXTRA_EFFECT = 2;
	private static final int CHANCE = 10;
	
	@Test
	public void testCreateJokerBubble() {
		 // Force primary and secundary effect
		setRandom(CHANCE, 2);
		// Force joker
		setRandom(PRIMARY_EFFECT, 0);
		
		Bubble bubble = factory.createBubble(remainingColors);
		assertNotNull(bubble);
		assertThat(bubble, Matchers.instanceOf(JokerBubble.class));
	}
	
	@Test
	public void testDrunkJokerBubble() {
		 // Force primary and secundary effect
		setRandom(CHANCE, 1);
		// Force joker
		setRandom(PRIMARY_EFFECT, 0);
		// Force drunk bubble left
		setRandom(EXTRA_EFFECT, 1);
		
		Bubble bubble = factory.createBubble(remainingColors);
		assertNotNull(bubble);
		assertThat(bubble, Matchers.instanceOf(DrunkBubbleLeft.class));
	}
	
	@Test
	public void testBombBubble() {
		 // Force primary effect
		setRandom(CHANCE, 2);
		// Force joker
		setRandom(PRIMARY_EFFECT, 1);
		
		Bubble bubble = factory.createBubble(remainingColors);
		assertNotNull(bubble);
		assertThat(bubble, Matchers.instanceOf(BombBubble.class));
	}
	
	@Test
	public void testStoneBubble() {
		 // Force primary effect
		setRandom(CHANCE, 2);
		// Force joker
		setRandom(PRIMARY_EFFECT, 2);
		
		Bubble bubble = factory.createBubble(remainingColors);
		assertNotNull(bubble);
		assertThat(bubble, Matchers.instanceOf(StoneBubble.class));
	}
	
	@Test
	public void testColouredBubble() {
		// Force no effect
		setRandom(CHANCE, 10);
		
		Bubble bubble = factory.createBubble(remainingColors);
		assertNotNull(bubble);
		assertThat(bubble, Matchers.instanceOf(ColouredBubble.class));
	}
	
	@Test
	public void testPowerupWhenEmpty() {
		// Force no effect
		setRandom(CHANCE, 10);
		// Force joker
		setRandom(PRIMARY_EFFECT, 0);
		
		Bubble bubble = factory.createBubble(Sets.newHashSet());
		assertNotNull(bubble);
		assertThat(bubble, Matchers.instanceOf(JokerBubble.class));
	}
	
	protected void setRandom(int bound, int result) {
		Mockito.when(random.nextInt(bound)).thenReturn(result);
	}

}
