package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;
import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.game.event.GameListener;
import nl.tudelft.ti2206.game.event.CannonListener.*;
import nl.tudelft.ti2206.cannon.CannonModel;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.game.backend.mode.ClassicGameMode;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.PacketListener;
import nl.tudelft.util.Vector2f;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameControllerTest {
	
	private final static int WIDTH = 325;
	private final static int HEIGHT = 400;
	private final static Dimension d= new Dimension(WIDTH,HEIGHT); 
	protected static final String DEFAULT_BOARD_PATH = "/board.txt";
	
	protected BubbleMesh bubbleMesh;
	protected GameModel gameModel;
	protected MockedGameTick tick;
	protected CannonModel cannonModel;
	protected CannonController cannonController;
	protected GameController gameController;
	protected GameListener gameListener;

	@Before
	public void setUp() throws Exception {
		
		bubbleMesh = spy(BubbleMesh.parse(SinglePlayerFrame.class
				.getResourceAsStream(DEFAULT_BOARD_PATH)));
		
		cannonModel = spy(new CannonModel());

		gameModel = spy(new GameModel(ClassicGameMode.class, bubbleMesh));
		gameListener = mock(GameListener.class);
		gameModel.addEventListener(gameListener);
		
		tick = spy(new MockedGameTick());
		BubbleFactory factory = mock(BubbleFactory.class);
		cannonController = mock(CannonController.class);
		
		when(cannonController.getModel()).thenReturn(cannonModel); 
		when(factory.createBubble(any())).thenReturn(new AbstractBubble());

		gameController = spy(new GameController(gameModel, cannonController));
		gameController.bindGameTick(tick);
		
		assertEquals(cannonController, gameController.getCannonController());
		assertEquals(gameModel, gameController.getModel());
		assertThat(gameController.getGameMode(), Matchers.instanceOf(ClassicGameMode.class));

		verify(bubbleMesh).calculatePositions();
		verify(cannonModel).addEventListener(any());
		verify(tick).registerObserver(any());
		resetMocks();
	}

	protected void resetMocks() {
		reset(bubbleMesh, gameModel, gameListener, cannonController, gameController, tick);
	}
	
	@Test
	public void gameTickTest() {
		ColouredBubble colouredBubble = new ColouredBubble(Color.RED);
		colouredBubble.setPosition(new Point(WIDTH/2,HEIGHT));
		MovingBubble movingBubble = spy(new MovingBubble(new Vector2f(-0.054464497F, -0.9985157F), d, colouredBubble));
		gameModel.setShotBubble(movingBubble);
		tick.tick();
		
		verify(gameModel).getShotBubble();
		verify(gameModel).getBubbleMesh();
		verify(movingBubble).addVelocity();
		verify(movingBubble).gameTick();
		for(int i = 0; i < 1000; i++) tick.tick();
	}
	
	@Test
	public void testIncrementPoints() {
		int amount = 2000;
		gameController.incrementScore(amount);
		verify(gameModel).incrementScore(amount);
		verify(gameModel).notifyObservers();
		verify(gameListener).score(new GameListener.ScoreEvent(gameController, amount));
	}
	
	@Test
	public void testShootListener() {
		Vector2f direction = new Vector2f(-0.054464497f, -0.9985157f);
		CannonShootEvent event = new CannonShootEvent(cannonController, direction);
		
		cannonModel.trigger(listener -> {
			listener.shoot(event);
		});
		
		verifyUpdateBubbles(gameModel);
		verify(gameModel).setShotBubble(any());
		verify(gameModel).notifyObservers();
		verify(gameListener).shoot(event);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testShootInvalidState() {
		Vector2f direction = new Vector2f(-0.054464497f, -0.9985157f);
		CannonShootEvent event = new CannonShootEvent(cannonController, direction);
		
		cannonModel.trigger(listener -> {
			listener.shoot(event);
		});
		
		cannonModel.trigger(listener -> {
			listener.shoot(event);
		});
	}
	
	protected static void verifyUpdateBubbles(GameModel gameModel) {
		verify(gameModel).getLoadedBubble();
		verify(gameModel).getNextBubble();
		verify(gameModel).setNextBubble(any());
		verify(gameModel).setLoadedBubble(any());
	}
	
	@Test
	public void testCollide() {
		BubbleMesh bubbleMesh = mock(BubbleMesh.class);
		MovingBubble shotBubble = mock(MovingBubble.class);
		Bubble hitTarget = mock(ColouredBubble.class);
		Bubble snapPosition = mock(BubblePlaceholder.class);
		
		when(hitTarget.getSnapPosition(shotBubble)).thenReturn(snapPosition);
		when(bubbleMesh.pop(shotBubble)).thenReturn(false);
		
		gameController.collide(bubbleMesh, shotBubble, hitTarget);
		
		verify(shotBubble).collideHook(hitTarget);
		verify(bubbleMesh).replaceBubble(snapPosition, shotBubble);
		
		verify(cannonController).load();
		verify(gameModel).setShotBubble(null);
	}
	
	@Test
	public void testCollideWithPops() {
		BubbleMesh bubbleMesh = mock(BubbleMesh.class);
		MovingBubble shotBubble = mock(MovingBubble.class);
		Bubble hitTarget = mock(ColouredBubble.class);
		Bubble snapPosition = mock(BubblePlaceholder.class);
		
		when(bubbleMesh.isEmpty()).thenReturn(false);
		when(hitTarget.getSnapPosition(shotBubble)).thenReturn(snapPosition);
		when(bubbleMesh.pop(shotBubble)).thenReturn(true);
		
		gameController.collide(bubbleMesh, shotBubble, hitTarget);
		
		verify(shotBubble).collideHook(hitTarget);
		verify(bubbleMesh).replaceBubble(snapPosition, shotBubble);
		
		verify(cannonController).load();
		verify(gameModel).setShotBubble(null);
	}
	
	@Test
	public void testCollideWithGameOver() {
		BubbleMesh bubbleMesh = mock(BubbleMesh.class);
		MovingBubble shotBubble = mock(MovingBubble.class);
		Bubble hitTarget = mock(ColouredBubble.class);
		Bubble snapPosition = mock(BubblePlaceholder.class);
		
		when(hitTarget.getSnapPosition(shotBubble)).thenReturn(snapPosition);
		when(bubbleMesh.pop(shotBubble)).thenThrow(new GameOver());
		
		gameController.collide(bubbleMesh, shotBubble, hitTarget);
		
		verify(shotBubble).collideHook(hitTarget);
		verify(bubbleMesh).replaceBubble(snapPosition, shotBubble);
		verifyGameOver(gameModel);
		verify(gameModel).setShotBubble(null);
	}
	
	protected static void verifyGameOver(GameModel gameModel) {
		verify(gameModel).setGameOver(true);
		verify(gameModel).notifyObservers();
		verify(gameModel).trigger(any());
	}
	
	@Test
	public void testGetRandomRemainingColor() {
		
		Set<Color> colors = gameModel.getRemainingColors();
		reset(gameModel);
		
		Color color = gameController.getRandomRemainingColor();
		verify(gameModel).getRemainingColors();
		assertTrue(colors.contains(color));
	}
	
	@Test
	public void testBindConnectorAsMaster() {
		Connector connector = mock(Connector.class);
		gameController.bindConnectorAsMaster(connector);
	}
	
	@Test
	public void testSendBubbleMeshOnRowInsert() {
		Connector connector = mock(Connector.class);
		gameController.bindConnectorAsMaster(connector);
		reset(connector);
		
		bubbleMesh.insertRow(gameController);
		verify(connector).sendPacket(any());	
	}
	
	@Test
	public void testCannonShootSend() {
		Vector2f direction = new Vector2f(-0.054464497f, -0.9985157f);
		CannonShootEvent event = new CannonShootEvent(cannonController, direction);

		Connector connector = mock(Connector.class);
		gameController.bindConnectorAsMaster(connector);
		reset(connector);
		reset(gameModel);
		
		cannonModel.trigger(listener -> listener.shoot(event));
		verify(gameListener).shoot(any());
		verify(gameListener).ammo(any());
	}
	
	@Test
	public void testBindConnectorAsSlave() {
		Connector connector = mock(Connector.class);
		gameController.bindConnectorAsSlave(connector);
		verify(connector).addEventListener(any());
	}
	
	@Test
	public void testUpdateAmmoPacket() {
		ArgumentCaptor<PacketListener> captor = ArgumentCaptor.forClass(PacketListener.class);
		Connector connector = mock(Connector.class);
		
		gameController.bindConnectorAsSlave(connector);
		verify(connector).addEventListener(captor.capture());
		
		PacketListener listener = captor.getValue();

		Bubble one = mock(Bubble.class);
		Bubble two = mock(Bubble.class);
		GameListener.AmmoLoadEvent event = new GameListener.AmmoLoadEvent(gameController, one, two);
		listener.handleAmmoLoad(event);
		
		verify(gameModel).setLoadedBubble(one);
		verify(gameModel).setNextBubble(two);
		verify(gameModel).notifyObservers();
	}
	
	@Test
	public void testInsertRowDelegate() {
		gameController.insertRow();
		verify(bubbleMesh).insertRow(gameController);
	}
}
