package nl.tudelft.ti2206.util.mvc;

import java.util.EventListener;

public interface EventTarget<T extends EventListener> {

	void addEventListener(T listener);
	
	void removeEventListener(T listener);
	
}
