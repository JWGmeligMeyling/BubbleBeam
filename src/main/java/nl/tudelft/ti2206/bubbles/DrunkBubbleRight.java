package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;

/**
 * @author Luka Bavdaz
 */
public class DrunkBubbleRight extends DrunkBubble {
	private static final long serialVersionUID = -2286964640875125242L;
	
	public DrunkBubbleRight(Bubble bubble) {
		super(bubble);
		acceleration = DrunkBubble.ACCELERATION;
	}
	
	public DrunkBubbleRight() {
		this(new AbstractBubble());
	}
	
	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(ARROW_IMAGE, (int) bubble.getX() + IMAGE_TRANSLATION,
				(int) bubble.getY(), ARROW_WIDTH, ARROW_HEIGHT, null);
		bubble.render(graphics);
	}
}
