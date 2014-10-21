package nl.tudelft.ti2206.graphics.animations;

/**
 * A FiniteAnimation has a finite duration.
 * 
 * @author Sam Smulders
 */
public abstract class FiniteAnimation implements Animation {
	protected int time = 0;
	protected final int maxTime;
	
	public FiniteAnimation(int maxTime) {
		this.maxTime = maxTime;
	}
	
	public boolean isDone() {
		return time == maxTime;
	}
	
	public void addTime() {
		time++;
	}
	
}
