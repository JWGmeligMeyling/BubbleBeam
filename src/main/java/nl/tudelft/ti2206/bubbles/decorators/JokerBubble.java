package nl.tudelft.ti2206.bubbles.decorators;

import java.awt.Color;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.Coloured;
import nl.tudelft.ti2206.bubbles.ColouredBubble;

/**
 * The {@code JokerBubble} is a special type of {@code Bubble} which determines
 * it's {@code Color} after snapping to another {@code Bubble}. Therefore, it's
 * possible to pop a combination of multiple colors, and with any color you
 * want! See it as a special, power-up {@code Bubble}.
 * 
 * @author Leon Hoek
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class JokerBubble implements DecoratedBubble, Coloured {

	private static final long serialVersionUID = -1415122920739845104L;

	protected ColouredBubble bubble;

	protected boolean hasColor = false;
	protected boolean snapped = false;

	/**
	 * Construct a new {@code JokerBubble}
	 */
	public JokerBubble() {
		bubble = new ColouredBubble(Color.WHITE);
	}

	@Override
	public boolean popsWith(Bubble target) {
		return !snapped || bubble.popsWith(target);
	}

	@Override
	public void collideHook(Bubble other) {
		if (Coloured.class.isInstance(other)) {
			Coloured coloured = Coloured.class.cast(other);
			bubble.setColor(coloured.getColor());
			hasColor = true;
		}
	}

	@Override
	public void snapHook() {
		// Switch to ColouredBubble behaviour once this joker snapped to the
		// grid and collided with a colouredbubble
		snapped = hasColor;
	}

	@Override
	public Bubble getBubble() {
		return bubble;
	}

	@Override
	public Color getColor() {
		return bubble.getColor();
	}
	
	@Override
	public void setBubble(Bubble bubble) {
	}
}
