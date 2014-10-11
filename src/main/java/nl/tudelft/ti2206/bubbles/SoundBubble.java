package nl.tudelft.ti2206.bubbles;

import java.applet.Applet;

public class SoundBubble implements DecoratedBubble {
	
	private static final long serialVersionUID = 3958509424351912735L;
	private Bubble bubble;
	private String sound;
	
	public SoundBubble(String sound, Bubble bubble) {
		this.sound = sound;
		this.bubble = bubble;
	}
	
	@Override
	public void popHook() {
		System.out.println("popsWith");
		Applet.newAudioClip(SoundBubble.class.getResource("/" + sound)).play();
		getBubble().popHook();
	}
	
	@Override
	public Bubble getBubble() {
		return bubble;
	}
	
}
