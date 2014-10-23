package nl.tudelft.ti2206.game.backend;

import java.util.EventListener;

/**
 * A {@link Tickable} is a {@link EventListener} that listens to ga {@link GameTick}
 * 
 * @author Sam Smulders
 *
 */
public interface Tickable extends EventListener {
	
	/**
	 * Invoked for every tick of the {@link GameTick}
	 */
	public void gameTick();
	
}
