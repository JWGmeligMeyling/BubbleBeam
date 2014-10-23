package nl.tudelft.ti2206.game.backend.mode;

import static org.junit.Assert.*;
import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TimedGameModeTest {

	protected BubbleMesh bubbleMesh;
	protected GameController gameController;
	protected GameModel gameModel;
	protected TimedGameMode gameMode;

	@Before
	public void setUp() throws Exception {
		bubbleMesh = Mockito.mock(BubbleMesh.class);
		gameController = Mockito.mock(GameController.class);
		gameModel = Mockito.mock(GameModel.class);
		Mockito.when(gameController.getModel()).thenReturn(gameModel);
		
		gameMode = new TimedGameMode(gameController);
		Mockito.reset(gameController, gameModel, bubbleMesh);
		Mockito.when(gameModel.getBubbleMesh()).thenReturn(bubbleMesh);
		
		assertEquals("Timed Mode", gameMode.getModeName());
		assertThat(gameMode.getBubbleFactory(), Matchers.instanceOf(PowerUpBubbleFactory.class));
	}

	@Test
	public void testGameTick() {
		for(int i = 0; i < 30; i++)
			gameMode.gameTick();
		Mockito.verify(bubbleMesh).translate(0, 1);
	}
	


	@Test
	public void testMultipleGameTicks() {
		for(int i = 0; i < 90; i++)
			gameMode.gameTick();
		Mockito.verify(bubbleMesh, Mockito.times(3)).translate(0, 1);
	}

}
