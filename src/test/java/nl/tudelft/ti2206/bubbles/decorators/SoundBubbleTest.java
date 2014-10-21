package nl.tudelft.ti2206.bubbles.decorators;

import java.applet.AudioClip;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.AbstractBubbleTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SoundBubbleTest extends AbstractBubbleTest {

	protected AudioClip sound;
	protected PopSoundBubble soundBubble;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		sound = Mockito.mock(AudioClip.class);
		bubble = soundBubble = new PopSoundBubble(sound, new AbstractBubble());
	}
	
	@Test
	public void testPopHook() {
		bubble.popHook();
		Mockito.verify(sound).play();
	}
	
}
