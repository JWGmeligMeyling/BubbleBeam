package nl.tudelft.ti2206.util.mvc;

import java.util.Observable;

public interface View<C extends Controller<M>, M extends Observable> {
	
	C getController();
	
	default M getModel() {
		return getController().getModel();
	}
	
}
