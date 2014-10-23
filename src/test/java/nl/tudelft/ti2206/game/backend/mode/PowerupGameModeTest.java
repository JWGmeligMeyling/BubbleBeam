package nl.tudelft.ti2206.game.backend.mode;

import static org.junit.Assert.*;
import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.mockito.Mockito;

public class PowerupGameModeTest extends ClassicGameModeTest {

	protected PowerupGameMode gameMode;
	
	@Before
	public void setUp() throws Exception {
		gameController = Mockito.mock(GameController.class);
		gameModel = Mockito.mock(GameModel.class);
		Mockito.when(gameController.getModel()).thenReturn(gameModel);
		super.gameMode = gameMode = new PowerupGameMode(gameController);
		Mockito.reset(gameController, gameModel);

		assertEquals("Power-up Mode", gameMode.getModeName());
		assertThat(gameMode.getBubbleFactory(), Matchers.instanceOf(PowerUpBubbleFactory.class));
	}

}
