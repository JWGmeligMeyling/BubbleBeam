package nl.tudelft.ti2206.bubbles.decorators;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.graphics.Animation;

public class PopAnimationBubble implements DecoratedBubble {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8350671636811479775L;
	private Bubble bubble;
	private Animation animation;
	
	/**
	 * @param animation
	 *            to be played
	 * @param bubble
	 *            the wrapped bubble
	 */
	public PopAnimationBubble(Animation animation, Bubble bubble) {
		this.animation = animation;
		this.bubble = bubble;
	}
	
	@Override
	public void popHook() {
		// TODO: Do something
	}
	
	@Override
	public Bubble getBubble() {
		return bubble;
	}
	
	@Override
	public void setBubble(Bubble bubble) {
		this.bubble = bubble;
	}
}
