package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;
import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.cannon.CannonModel;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.util.Vector2f;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class GameControllerTest {
	
	private final static int WIDTH = 325;
	private final static int HEIGHT = 400;
	private final static Dimension d= new Dimension(WIDTH,HEIGHT); 
	protected static final String DEFAULT_BOARD_PATH = "/board.txt";
	private GameModel model;
	private MockedGameTick tick;
	private CannonController controller;
	private GameController c;

	@Before
	public void setUp() throws Exception {
		BubbleMesh bubbleMesh = spy(BubbleMesh.parse(SinglePlayerFrame.class
				.getResourceAsStream(DEFAULT_BOARD_PATH)));
		model = spy(new GameModel(bubbleMesh));
		tick = spy(new MockedGameTick());
		BubbleFactory factory = mock(BubbleFactory.class);
		CannonModel cmodel = mock(CannonModel.class);
		controller = mock(CannonController.class);
		when(controller.getModel()).thenReturn(cmodel); 
		when(factory.createBubble(any())).thenReturn(new AbstractBubble());

		c = spy(new GameController(model, controller, tick, factory));

		verify(bubbleMesh).calculatePositions();
		verify(cmodel).addEventListener(any());
		verify(tick).registerObserver(any());
		reset(model, controller, c, tick);
	}

	@Test
	public void gameTickTest() {
		ColouredBubble cb=new ColouredBubble(Color.RED);
		cb.setPosition(new Point(WIDTH/2,HEIGHT));
		MovingBubble b = spy(new MovingBubble(new Vector2f(-0.054464497F, -0.9985157F),d,
				cb));
		model.setShotBubble(b);
		tick.tick();
		
		verify(model).getShotBubble();
		verify(model).getBubbleMesh();
		verify(b).addVelocity();
		verify(b).gameTick();
		
	}
}
