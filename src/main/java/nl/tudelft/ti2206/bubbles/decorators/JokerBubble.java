package nl.tudelft.ti2206.bubbles.decorators;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;
import nl.tudelft.ti2206.graphics.animations.ConfettiAnimation;
import nl.tudelft.ti2206.graphics.animations.FiniteAnimation;

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
public class JokerBubble extends DecoratedBubble {
	
	private static final long serialVersionUID = -1415122920739845104L;
	protected final static AudioClip HORN_SOUND = Applet.newAudioClip(SoundBubble.class
			.getResource("/horn.wav"));
	
	protected ColouredBubble bubble;
	protected boolean hasColor = false;
	protected boolean snapped = false;
	
	public JokerBubble() {
		this(new ColouredBubble(Color.WHITE));
	}
	
	/**
	 * Construct a new {@code JokerBubble}
	 */
	public JokerBubble(ColouredBubble bubble) {
		super(new SoundBubble(HORN_SOUND, bubble));
		this.bubble = bubble;
	}
	
	@Override
	public boolean popsWith(Bubble target) {
		return !snapped || bubble.popsWith(target);
	}
	
	@Override
	public void collideHook(Bubble other) {
		if (other.hasColor()) {
			bubble.setColor(other.getColor());
			hasColor = true;
		}
		bubble.snapHook();
	}
	
	@Override
	public void snapHook() {
		// Switch to ColouredBubble behaviour once this joker snapped to the
		// grid and collided with a colouredbubble
		snapped = hasColor;
		bubble.snapHook();
	}
	
	@Override
	public Color getColor() {
		return bubble.getColor();
	}
	
	@Override
	public boolean hasColor() {
		return hasColor;
	}
	
	@Override
	public FiniteAnimation getAnimation(Bubble bubble) {
		return new ConfettiAnimation(60, bubble);
	}
}
