package nl.tudelft.ti2206.util.mvc;

import java.util.Observable;

public interface Controller<T extends Observable> {
	
	T getModel();
	
}
