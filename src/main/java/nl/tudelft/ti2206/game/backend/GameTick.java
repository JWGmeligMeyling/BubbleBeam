package nl.tudelft.ti2206.game.backend;

import com.google.inject.ImplementedBy;

/**
 * The {@link GameTick} is a timer on which {@link Tickable Tickables} can hook
 * 
 * @author Sam Smulders
 *
 */
@ImplementedBy(GameTickImpl.class)
public interface GameTick {
	
	/**
	 * Register a {@link Tickable}
	 * @param observer
	 * 	{@link Tickable} to register
	 */
	void registerObserver(Tickable observer);

	/**
	 * Remove a {@link Tickable}
	 * @param observer
	 * 	{@link Tickable} to remove
	 */
	void removeObserver(Tickable observer);
	
	/**
	 * Shutdown this {@link GameTick}
	 */
	void shutdown();
	
}
