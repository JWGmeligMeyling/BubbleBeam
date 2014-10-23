package nl.tudelft.ti2206.util.mvc;

import java.util.Observable;

/**
 * Controller interface for the Model-View-Controller pattern
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 * @param <T> Model class for this {@code Controller}
 */
public interface Controller<T extends Observable> {
	
	/**
	 * @return The model for this {@code Controller}
	 */
	T getModel();
	
}
