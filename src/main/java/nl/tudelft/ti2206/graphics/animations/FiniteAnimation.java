package nl.tudelft.ti2206.graphics.animations;


public abstract class FiniteAnimation extends Animation {
	protected int time = 0;
	protected final int maxTime;
	
	public FiniteAnimation(int maxTime) {
		this.maxTime = maxTime;
	}
	
	public boolean isDone() {
		return time == maxTime;
	}
	
}
