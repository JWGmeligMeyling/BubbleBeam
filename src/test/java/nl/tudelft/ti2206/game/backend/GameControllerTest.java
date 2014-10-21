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
import nl.tudelft.ti2206.bubbles.mesh.BubbleMeshListener.ScoreListener;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.cannon.CannonModel;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.AmmoPacket;
import nl.tudelft.ti2206.network.packets.BubbleMeshSync;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.ti2206.network.packets.PacketHandler;
import nl.tudelft.util.Vector2f;

import org.junit.Before;
import org.junit.Test;

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

	@Before
	public void setUp() throws Exception {
		
		bubbleMesh = spy(BubbleMesh.parse(SinglePlayerFrame.class
				.getResourceAsStream(DEFAULT_BOARD_PATH)));
		
		cannonModel = spy(new CannonModel());
		gameModel = spy(new GameModel(GameMode.CLASSIC, bubbleMesh));
		tick = spy(new MockedGameTick());
		BubbleFactory factory = mock(BubbleFactory.class);
		cannonController = mock(CannonController.class);
		
		when(cannonController.getModel()).thenReturn(cannonModel); 
		when(factory.createBubble(any())).thenReturn(new AbstractBubble());

		gameController = spy(new GameController(gameModel, cannonController, tick));
		
		assertEquals(cannonController, gameController.getCannonController());
		assertEquals(gameModel, gameController.getModel());

		verify(bubbleMesh).calculatePositions();
		verify(cannonModel).addEventListener(any());
		verify(tick).registerObserver(any());
		resetMocks();
	}

	protected void resetMocks() {
		reset(bubbleMesh, gameModel, cannonController, gameController, tick);
	}
	
	@Test
	public void gameTickTest() {
		ColouredBubble cb=new ColouredBubble(Color.RED);
		cb.setPosition(new Point(WIDTH/2,HEIGHT));
		MovingBubble b = spy(new MovingBubble(new Vector2f(-0.054464497F, -0.9985157F), d, cb));
		gameModel.setShotBubble(b);
		tick.tick();
		
		verify(gameModel).getShotBubble();
		verify(gameModel).getBubbleMesh();
		verify(b).addVelocity();
		verify(b).gameTick();
		for(int i = 0; i < 1000; i++) tick.tick();
	}
	
	@Test
	public void testShootListener() {
		Vector2f direction = new Vector2f(-0.054464497f, -0.9985157f);
		
		cannonModel.trigger(listener -> {
			listener.shoot(direction);
		});
		
		verifyUpdateBubbles(gameModel);
		verify(gameModel).setShotBubble(any());
		verify(gameModel).notifyObservers();
		verify(gameModel).triggerShootEvent(direction);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testShootInvalidState() {
		Vector2f direction = new Vector2f(-0.054464497f, -0.9985157f);
		
		cannonModel.trigger(listener -> {
			listener.shoot(direction);
		});
		
		cannonModel.trigger(listener -> {
			listener.shoot(direction);
		});
	}
	
	protected static void verifyUpdateBubbles(GameModel gameModel) {
		verify(gameModel).getLoadedBubble();
		verify(gameModel).getNextBubble();
		verify(gameModel).setNextBubble(any());
		verify(gameModel).setLoadedBubble(any());
	}
	
	@Test
	public void testScoreListener() {
		BubbleMesh bubbleMesh = gameModel.getBubbleMesh();
		int amount = 20;
		
		bubbleMesh.getEventTarget()
			.trigger(ScoreListener.class, listener -> {
				listener.points(bubbleMesh, amount);
		});
		
		verify(gameModel).incrementScore(amount);
		verify(gameModel).retainRemainingColors(bubbleMesh.getRemainingColours());
		verify(gameModel).notifyObservers();
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
		
		verifyIncrementMisses(gameModel);
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
	
	protected static void verifyIncrementMisses(GameModel gameModel) {
		verify(gameModel).getMisses();
		verify(gameModel).setMisses(anyInt());
	}
	
	protected static void verifyGameOver(GameModel gameModel) {
		verify(gameModel).setGameOver(true);
		verify(gameModel).notifyObservers();
		verify(gameModel).trigger(any(), any());
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
		testSendInitialData(connector);
	}
	
	protected static void testSendInitialData(Connector connector) {
		verify(connector, times(2)).sendPacket(any(Packet.class));
	}
	
	@Test
	public void testSendBubbleMeshOnRowInsert() {
		Connector connector = mock(Connector.class);
		gameController.bindConnectorAsMaster(connector);
		reset(connector);
		
		bubbleMesh.insertRow(gameController);
		verify(connector).sendPacket(any(BubbleMeshSync.class));	
	}
	
	@Test
	public void testCannonShootSend() {
		Vector2f direction = new Vector2f(-0.054464497f, -0.9985157f);

		Connector connector = mock(Connector.class);
		gameController.bindConnectorAsMaster(connector);
		reset(connector);
		reset(gameModel);
		
		gameModel.triggerShootEvent(direction);
		verify(connector, times(2)).sendPacket(any(Packet.class));
		verify(gameModel).getLoadedBubble();
		verify(gameModel).getNextBubble();
	}
	
	@Test
	public void testBindConnectorAsSlave() {
		Connector connector = mock(Connector.class);
		PacketHandler phc = spy(new PacketHandler());
		when(connector.getPacketHandlerCollection()).thenReturn(phc);		
		gameController.bindConnectorAsSlave(connector);
		
		verify(phc, times(2)).addEventListener(any(), any());
	}
	
	@Test
	public void testUpdateAmmoPacket() {
		Connector connector = mock(Connector.class);
		PacketHandler phc = spy(new PacketHandler());
		when(connector.getPacketHandlerCollection()).thenReturn(phc);		
		gameController.bindConnectorAsSlave(connector);
		
		Bubble one = mock(Bubble.class);
		Bubble two = mock(Bubble.class);
		AmmoPacket ammoPacket = new AmmoPacket(one, two);
		phc.notify(ammoPacket);
		
		verify(gameModel).setLoadedBubble(one);
		verify(gameModel).setNextBubble(two);
		verify(gameModel).notifyObservers();
	}
	
	@Test
	public void testBubbleMeshReceive() {
		Connector connector = mock(Connector.class);
		PacketHandler phc = spy(new PacketHandler());
		when(connector.getPacketHandlerCollection()).thenReturn(phc);		
		gameController.bindConnectorAsSlave(connector);
		
		BubbleMesh bubbleMesh = mock(BubbleMesh.class);
		BubbleMeshSync packet = new BubbleMeshSync(bubbleMesh);
		phc.notify(packet);
		
		verify(gameModel).setBubbleMesh(bubbleMesh);
		verify(gameModel).notifyObservers();
	}
	
	@Test
	public void testInsertRowDelegate() {
		gameController.insertRow();
		verify(bubbleMesh).insertRow(gameController);
	}
}
