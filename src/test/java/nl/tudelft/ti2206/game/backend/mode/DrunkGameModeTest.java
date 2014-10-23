package nl.tudelft.ti2206.game.backend.mode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import nl.tudelft.ti2206.bubbles.factory.DrunkBubbleFactory;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.mockito.Mockito;

public class DrunkGameModeTest extends ClassicGameModeTest {

	protected DrunkGameMode gameMode;
	
	@Before
	public void setUp() throws Exception {
		gameController = Mockito.mock(GameController.class);
		gameModel = Mockito.mock(GameModel.class);
		Mockito.when(gameController.getModel()).thenReturn(gameModel);
		super.gameMode = gameMode = new DrunkGameMode(gameController);
		Mockito.reset(gameController, gameModel);

		assertEquals("Drunk Mode", gameMode.getModeName());
		assertThat(gameMode.getBubbleFactory(), Matchers.instanceOf(DrunkBubbleFactory.class));
	}

}
