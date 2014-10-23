package nl.tudelft.util;

import java.util.EventListener;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Sets;

/**
 * Implementation for {@link EventTarget}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 * @param <T> {@link EventListener} type for this {@link EventTarget}
 */
public class AbstractEventTarget<T extends EventListener> implements EventTarget<T> {
	
	protected transient Set<T> listeners = Sets.newHashSet();

	@Override
	public void addEventListener(T listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeEventListener(T listener) {
		listeners.remove(listener);
	}

	@Override
	public void trigger(Consumer<T> action) {
		listeners.forEach(action);
	}

}
