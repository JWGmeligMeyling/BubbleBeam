package nl.tudelft.ti2206.game.backend;

import java.util.Iterator;
import java.util.List;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.cannon.CannonModel;
import nl.tudelft.ti2206.game.event.BubbleMeshListener;
import nl.tudelft.ti2206.game.event.CannonListener;
import nl.tudelft.ti2206.game.event.GameListener;
import nl.tudelft.util.Vector2f;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

public class SlaveGamePacketListenerTest {
	
	protected GameModel gameModel;
	protected GameController gameController;
	protected CannonController cannonController;
	protected CannonModel cannonModel;
	protected BubbleMesh bubbleMesh;
	protected SlaveGamePacketListener listener;

	@Before
	public void setUp() throws Exception {
		cannonModel = Mockito.mock(CannonModel.class);
		cannonController = Mockito.mock(CannonController.class);
		Mockito.when(cannonController.getModel()).thenReturn(cannonModel);
		
		bubbleMesh = Mockito.mock(BubbleMesh.class);
		gameModel = Mockito.mock(GameModel.class);
		Mockito.when(gameModel.getBubbleMesh()).thenReturn(bubbleMesh);
		
		gameController = Mockito.mock(GameController.class);
		Mockito.when(gameController.getModel()).thenReturn(gameModel);
		
		listener = new SlaveGamePacketListener(gameController, cannonController);
	}

	@Test
	public void testHandleAmmoLoad() {
		Bubble loadedBubble = Mockito.mock(Bubble.class);
		Bubble nextBubble = Mockito.mock(Bubble.class);
		GameListener.AmmoLoadEvent event = new GameListener.AmmoLoadEvent(gameController, loadedBubble, nextBubble);
		
		listener.handleAmmoLoad(event);
		Mockito.verify(gameModel).setLoadedBubble(loadedBubble);
		Mockito.verify(gameModel).setNextBubble(nextBubble);
		Mockito.verify(gameModel).notifyObservers();
	}
	
	@Test
	public void testHandleCannonRotate() {
		double angle = 55.2d;
		CannonListener.CannonRotateEvent event = new CannonListener.CannonRotateEvent(cannonController, null, angle);
		
		listener.handleCannonRotate(event);
		Mockito.verify(cannonModel).setAngle(angle);
		Mockito.verify(cannonModel).notifyObservers();
	}
	
	@Test
	public void testHandleCannonShoot() {
		Vector2f direction = Mockito.mock(Vector2f.class);
		CannonListener.CannonShootEvent event = new CannonListener.CannonShootEvent(cannonController, direction);
		
		listener.handleCannonShoot(event);
		Mockito.verify(cannonController).setAngle(direction);
		Mockito.verify(cannonController).shoot();
	}
	
	@Test
	public void testHandleRowInsert() {
		List<Bubble> origList = Lists.newArrayList(Mockito.mock(Bubble.class));
		Iterator<Bubble> iterator = origList.iterator();
		List<Bubble> insertedBubbles = Mockito.spy(origList);
		Mockito.when(insertedBubbles.iterator()).thenReturn(iterator);
		
		BubbleMeshListener.RowInsertEvent event = new BubbleMeshListener.RowInsertEvent(bubbleMesh, insertedBubbles);
		
		listener.handleRowInsert(event);
		Mockito.verify(bubbleMesh).insertRow(iterator);
	}
	
	@Test
	public void testGameOver() {
		GameListener.GameOverEvent event = new GameListener.GameOverEvent(gameController, new GameOver());
		listener.handleGameOver(event);
		Mockito.verify(gameController).gameOver(event.getGameOver());
	}
	
	@Test
	public void testGameWinDisconnect() {
		listener.disconnect();
		Mockito.verify(gameController).gameOver(new GameOver(false));
	}

}
