package nl.tudelft.ti2206.bubbles.decorators;

import java.applet.AudioClip;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;

/**
 * The {@code}SoundBubble plays a sound once it is popped.
 * 
 * @author Sam Smulders
 */
public class SoundBubble extends DecoratedBubble {
	
	private static final long serialVersionUID = 3958509424351912735L;
	
	private final AudioClip sound;
	
	/**
	 * @param sound
	 *            to be played
	 * @param bubble
	 *            the wrapped bubble
	 */
	public SoundBubble(AudioClip sound, Bubble bubble) {
		super(bubble);
		this.sound = sound;
	}
	
	@Override
	public void popHook() {
		if(sound != null)
			sound.play();
		bubble.popHook();
	}
	
}
