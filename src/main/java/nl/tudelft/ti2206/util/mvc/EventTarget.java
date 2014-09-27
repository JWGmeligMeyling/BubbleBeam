package nl.tudelft.ti2206.util.mvc;

import java.util.EventListener;
import java.util.Set;

public interface EventTarget<T extends EventListener> {

	void addEventListener(T listener);
	
	void removeEventListener(T listener);
	
	Set<T> getListeners();
	
}
