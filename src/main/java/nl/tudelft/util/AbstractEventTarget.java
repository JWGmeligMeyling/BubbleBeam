package nl.tudelft.util;

import java.util.EventListener;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class AbstractEventTarget<T extends EventListener> implements EventTarget<T> {
	
	protected Set<T> listeners = Sets.newHashSet();

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

	@Override
	public <A extends T> void trigger(Class<A> clasz, Consumer<A> action) {
		Iterables.filter(listeners, clasz).forEach(action);
	}

}
