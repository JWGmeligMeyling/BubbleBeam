package nl.tudelft.ti2206.bubbles.decorators;

import java.applet.Applet;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;

/**
 * The {@code}SoundBubble plays a sound once it is popped.
 * 
 * @author Sam Smulders
 */
public class SoundBubble extends DecoratedBubble {
	
	private static final long serialVersionUID = 3958509424351912735L;
	
	private final String sound;
	
	/**
	 * @param sound
	 *            to be played
	 * @param bubble
	 *            the wrapped bubble
	 */
	public SoundBubble(String sound, Bubble bubble) {
		super(bubble);
		this.sound = sound;
	}
	
	@Override
	public void popHook() {
		Applet.newAudioClip(SoundBubble.class.getResource("/" + sound)).play();
		bubble.popHook();
	}
	
}
