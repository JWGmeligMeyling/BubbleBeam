package nl.tudelft.ti2206.util.mvc;

import java.util.EventListener;
import java.util.Set;

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
	
}
