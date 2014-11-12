package nl.tudelft.ti2206.cannon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.util.Vector2f;
import nl.tudelft.ti2206.game.event.CannonListener.*;

/**
 * Cannon loaded state is the state for the cannon when the cannon is loaded
 * @author Jan-Willem Gmelig Meyling
 * @author Leon Hoek *
 */
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
		
		CannonShootEvent event = new CannonShootEvent(cannonController, direction);
		cannonController.getModel().trigger(listener -> listener.shoot(event));
	}
	
}
