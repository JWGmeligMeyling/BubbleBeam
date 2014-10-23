package nl.tudelft.ti2206.game.backend.mode;

import java.util.Set;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.factory.ClassicBubbleFactory;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.event.BubbleMeshListener;
import nl.tudelft.ti2206.game.event.GameListener;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Sets;

public class ClassicGameModeTest {
	
	protected GameController gameController;
	protected GameModel gameModel;
	protected ClassicGameMode gameMode;

	@Before
	public void setUp() throws Exception {
		gameController = Mockito.mock(GameController.class);
		gameModel = Mockito.mock(GameModel.class);
		Mockito.when(gameController.getModel()).thenReturn(gameModel);
		gameMode = new ClassicGameMode(gameController);
		Mockito.reset(gameController, gameModel);
		
		assertEquals("Classic Mode", gameMode.getModeName());
		assertThat(gameMode.getBubbleFactory(), Matchers.instanceOf(ClassicBubbleFactory.class));
	}

	@Test
	public void testShotMissed() {
		for(int i = 0; i < 5; i++)
			gameMode.shotMissed(new GameListener.ShotMissedEvent(gameController));
		Mockito.verify(gameController).insertRow();
		gameMode.shotMissed(new GameListener.ShotMissedEvent(gameController));
	}
	
	@Test
	public void testShotMissedNoShootOnSixt() {
		for(int i = 0; i < 5; i++)
			gameMode.shotMissed(new GameListener.ShotMissedEvent(gameController));
		Mockito.verify(gameController).insertRow();
		gameMode.shotMissed(new GameListener.ShotMissedEvent(gameController));
		Mockito.verifyNoMoreInteractions(gameController);
	}
	
	@Test
	public void testShotMissedSecondTime() {
		for(int i = 0; i < 10; i++)
			gameMode.shotMissed(new GameListener.ShotMissedEvent(gameController));
		Mockito.verify(gameController, Mockito.times(2)).insertRow();
	}
	
	@Test
	public void testBubblePop() {
		BubbleMesh bubbleMesh = Mockito.mock(BubbleMesh.class);
		Set<Bubble> bubblesPopped = Sets.newHashSet(Mockito.mock(Bubble.class));
		BubbleMeshListener.BubblePopEvent event = new BubbleMeshListener.BubblePopEvent(bubbleMesh, bubblesPopped);
		gameMode.pop(event);
		Mockito.verify(gameController).incrementScore(25);
	}

}
