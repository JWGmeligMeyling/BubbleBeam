package nl.tudelft.util;

import java.util.Observable;

public class ObservableObject<T> extends Observable {
	
	protected T value;
	
	public ObservableObject() {};
	
	public ObservableObject(T value) {
		setValue(value);
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
		this.setChanged();
		this.notifyObservers(value);
	}

}
