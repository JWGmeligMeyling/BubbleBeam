package nl.tudelft.util;

import java.util.EventListener;
import java.util.function.Consumer;

public interface EventTarget<T extends EventListener> {

	void addEventListener(T listener);
	
	void removeEventListener(T listener);
	
	void trigger(Consumer<T> action);
	
	<A extends T> void trigger(Class<A> clasz, Consumer<A> action);
	
}
