package nl.tudelft.ti2206.game;

public abstract class Task implements Runnable {
	
	protected boolean interupt;

	public void interupt(){
		interupt = true;
	}
}
