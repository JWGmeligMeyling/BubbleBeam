package nl.tudelft.ti2206.cannon;

import nl.tudelft.util.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CannonLoadedState implements CannonState {

	private static final Logger log = LoggerFactory.getLogger(CannonLoadedState.class);
	
	private final CannonController cannonController;
	
	public CannonLoadedState(CannonController cannonController) {
		this.cannonController = cannonController;
	}
	
	@Override
	public void shoot() {
		cannonController.setState(new CannonShootState());
		Vector2f direction = cannonController.getModel().getDirection();
		log.info("Shooting in direction {}", direction);
		cannonController.getModel().getListeners()
				.forEach(listener -> listener.shoot(direction));
	}

}
