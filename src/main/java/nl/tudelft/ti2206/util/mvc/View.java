
package nl.tudelft.ti2206.util.mvc;

import java.util.Observable;

/**
 * View interface for the Model-View-Controller pattern
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 * @param <C> {@link Controller} for this {@code View}
 * @param <M> Model for this {@code View}
 */
public interface View<C extends Controller<M>, M extends Observable> {
	
	/**
	 * @return get the {@link Controller} for this {@code View}
	 */
	C getController();
	
	/**
	 * @return get the {@code Model} for this {@code View}
	 */
	default M getModel() {
		return getController().getModel();
	}
	
}
