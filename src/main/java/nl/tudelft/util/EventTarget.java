package nl.tudelft.util;

import java.util.EventListener;
import java.util.function.Consumer;

import com.google.inject.ImplementedBy;

/**
 * An {@code EventTarget} is a class on which {@link EventListener
 * EventListeners} can listen for events
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 * @param <T>
 *            Type for the {@link EventListener EventListeners} this
 *            {@code EventTarget} can bind
 */
@ImplementedBy(EventTargetImpl.class)
public interface EventTarget<T extends EventListener> {
	
	/**
	 * Add an {@link EventListener}
	 * 
	 * @param listener
	 *            {@code EventListener} to add
	 */
	void addEventListener(T listener);
	
	/**
	 * Add an {@link EventListener}
	 * 
	 * @param clasz
	 *            Type of {@code EventListener}
	 * @param listener
	 *            {@code EventListener} to add
	 * @param <A>
	 *            Type of the {@code EventListener}
	 */
	default <A extends T> void addEventListener(Class<A> clasz, A listener) {
		addEventListener(listener);
	}
	
	/**
	 * Remove an {@link EventListener}
	 * 
	 * @param listener
	 *            {@code EventListener} to remove
	 */
	void removeEventListener(T listener);
	
	/**
	 * Trigger an action on the listeners
	 * 
	 * @param action
	 *            Action to perform on the listeners
	 */
	void trigger(Consumer<T> action);
	
}
