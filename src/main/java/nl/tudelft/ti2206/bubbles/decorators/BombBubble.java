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
import nl.tudelft.ti2206.bubbles.pop.RadialPopBehaviour;
import nl.tudelft.ti2206.graphics.animations.ExplosionAnimation;
import nl.tudelft.ti2206.graphics.animations.FiniteAnimation;

/**
 * The {@code BombBubble} is a {@link Bubble} that explodes on impact, popping
 * bubbles in a radius of {@code POP_RADIUS}. {@code BombBubble} implements the
 * {@link DecoratedBubble} class and is considered a 'powerup' bubble.
 * 
 * @author Leon Hoek
 */
public class BombBubble extends DecoratedBubble {
	
	private static final long serialVersionUID = -5406623504377849151L;
	
	protected static final AudioClip BOMB_SOUND = Applet.newAudioClip(BombBubble.class
			.getResource("/bomb.wav"));
	
	protected static final int POP_RADIUS = 2;
	
	protected final RadialPopBehaviour popBehaviour;
	protected final static transient Image BOMB_IMAGE = _getBubbleImage();
	
	/**
	 * Construct a new {@code BombBubble}
	 */
	public BombBubble() {
		this(new AbstractBubble());
	}
	
	/**
	 * Construct a new {@code BombBubble}
	 * 
	 * @param bubble
	 *            Decorate the {@code BombBubble} with another {@code Bubble}
	 */
	public BombBubble(Bubble bubble) {
		super(new PopSoundBubble(BOMB_SOUND, bubble));
		popBehaviour = new RadialPopBehaviour(POP_RADIUS);
	}
	
	@Override
	public boolean popsWith(Bubble target) {
		// Returns true, it immidiately pops itself and it's closest neighbours
		// and isn't expected to remain in the game
		return true;
	}
	
	protected static Image _getBubbleImage() {
		try {
			return ImageIO.read(BombBubble.class.getResourceAsStream("/scaledBomb.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(BOMB_IMAGE, bubble.getX(), bubble.getY(), bubble.getWidth(),
				bubble.getHeight(), null);
		bubble.render(graphics);
	}
	
	@Override
	public boolean isHittable() {
		return true;
	}
	
	@Override
	public RadialPopBehaviour getPopBehaviour() {
		return popBehaviour;
	}
	
	@Override
	public FiniteAnimation getAnimation() {
		return new ExplosionAnimation(this);
	}
}
