package nl.tudelft.ti2206.game.backend;

import nl.tudelft.ti2206.game.event.CannonListener;

/**
 * The {@code GameControllerCannonListener} listens for events on the
 * {@link CannonModel}, delegates the event as {@link GameEvent} on the
 * {@link GameModel} and invokes the right actions on the {@link GameController}
 * 
 * @author jgmeligmeyling
 *
 */
public class GameControllerCannonListener implements CannonListener {
	
	protected final GameController gameController;
	
	/**
	 * Construct a new {@link GameControllerCannonListener}
	 * 
	 * The {@code GameControllerCannonListener} listens for events on the
	 * {@link CannonModel}, delegates the event as {@link GameEvent} on the
	 * {@link GameModel} and invokes the right actions on the
	 * {@link GameController}
	 * 
	 * @param gameController
	 */
	public GameControllerCannonListener(GameController gameController) {
		this.gameController = gameController;
	}
	
	@Override
	public void shoot(final CannonShootEvent event) {
		gameController.getModel().trigger(listener -> listener.shoot(event));
		gameController.shoot(event.getDirection());
	}

	@Override
	public void rotate(final CannonRotateEvent event) {
		gameController.getModel().trigger(listener -> listener.rotate(event));
	}
	
}