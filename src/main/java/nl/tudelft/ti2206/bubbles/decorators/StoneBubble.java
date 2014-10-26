package nl.tudelft.ti2206.bubbles.decorators;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;
import nl.tudelft.ti2206.graphics.animations.FallAnimation;
import nl.tudelft.ti2206.graphics.animations.FiniteAnimation;

/**
 * Stone bubble implementation
 * 
 * @author Liam Clark
 *
 */

public class StoneBubble extends DecoratedBubble {
	
	private static final long serialVersionUID = -8390605724378971542L;
	private final static transient Image STONE_IMAGE = _getBubbleImage();
	
	protected final static AudioClip STONE_SOUND = Applet.newAudioClip(StoneBubble.class
			.getResource("/stone.wav"));
	
	public StoneBubble() {
		this(new AbstractBubble());
	}
	
	public StoneBubble(Bubble bubble) {
		super(new SnapSoundBubble(STONE_SOUND, bubble));
	}
	
	protected static Image _getBubbleImage() {
		try {
			return ImageIO.read(StoneBubble.class.getResourceAsStream("/scaledStone.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean popsWith(Bubble target) {
		return false;
	}
	
	@Override
	public boolean isHittable() {
		return true;
	}
	
	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(STONE_IMAGE, bubble.getX(), bubble.getY(), bubble.getWidth(),
				bubble.getHeight(), null);
		bubble.render(graphics);
	}
	
	@Override
	public FiniteAnimation getAnimation() {
		return new FallAnimation(this);
	}
}
