package nl.tudelft.ti2206.bubbles.decorators;

import java.applet.AudioClip;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;

/**
 * @author Liam Clark
 * @author Leon Hoek
 */
public class SnapSoundBubble extends DecoratedBubble {

	private static final long serialVersionUID = -2388456187288933856L;
	
	private final transient AudioClip sound;
	
	/**
	 * @param sound
	 *            to be played
	 * @param bubble
	 *            the wrapped bubble
	 */
	public SnapSoundBubble(AudioClip sound, Bubble bubble) {
		super(bubble);
		this.sound = sound;
	}
	
	@Override
	public void snapHook(){
		if(sound!= null)
			sound.play();
		bubble.snapHook();
	}

}
