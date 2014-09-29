package nl.tudelft.ti2206.cannon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import nl.tudelft.util.Vector2f;

import org.junit.Before;
import org.junit.Test;

public class AbstractCannonControllerTest {
	
	protected CannonModel cannonModel;
	protected AbstractCannonController cannonController;
	protected CannonShootListener shootListener;

	@Before
	public void setUp() throws Exception {
		cannonModel = spy(new CannonModel());
		cannonController = spy(new AbstractCannonController(cannonModel));
		shootListener = mock(CannonShootListener.class);
		cannonModel.addEventListener(shootListener);
		
		assertEquals(cannonModel, cannonController.getModel());
		resetMocks();
	}
	
	protected void resetMocks() {
		reset(cannonModel);
		reset(cannonController);
		reset(shootListener);
	}
	
	@Test
	public void testSetAngle() {
		Vector2f direction = new Vector2f(.8f, .7f);
		cannonController.setAngle(direction);
		verify(cannonModel, times(1)).setDirection(direction);
		verify(cannonModel, times(1)).setAngle(anyDouble());
	}
	
	@Test
	public void testShoot() {
		CannonLoadedState cannonState = spy(new CannonLoadedState(cannonController));
		cannonController.setState(cannonState);
		resetMocks();
		
		Vector2f direction = new Vector2f(.8f, .7f);
		when(cannonModel.getDirection()).thenReturn(direction);

		cannonController.shoot();
		verify(cannonState, times(1)).shoot();
		verify(cannonModel, times(1)).setCannonState(any(CannonShootState.class));
		verify(shootListener, times(1)).shoot(direction);
	}

	@Test
	public void testLoad() {
		CannonShootState state = new CannonShootState();
		cannonModel.setCannonState(state);
		assertEquals(state, cannonModel.getCannonState());
		resetMocks();
		
		cannonController.load();
		verify(cannonModel, times(1)).setCannonState(any(CannonLoadedState.class));
	}

}
