package nl.tudelft.ti2206.bubbles;

/**
 * The {@code SnapBehaviour} defines the way a {@link MovingBubble} should snap
 * on to this Bubble. For example: moving bubbles snap to {@link ColouredBubble
 * ColouredBubbles} by finding the closest {@link BubblePlaceholder}, but in the 
 * top row, they snap to any hit {@link BubblePlaceholder} itself.
 * 
 * @author Luka Bavdaz
 * @author Sam Smulders
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface SnapBehaviour {
	
	/**
	 * If a {@link MovingBubble} hits this {@code Bubble}, it should snap to the
	 * given {@link BubblePlaceHolder}.
	 * 
	 * @param b
	 * @return The {@code BubblePlaceHolder} at which the {@code MovingBubble}
	 *         should snap
	 */
	BubblePlaceholder getSnapPosition(Bubble bubble);
	
}
