package nl.tudelft.util;

public abstract class Task implements Runnable {
	
	protected boolean interupt;

	public void interupt(){
		interupt = true;
	}
}
