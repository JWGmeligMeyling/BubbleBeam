package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;

/**
 * @author Luka Bavdaz
 */
public class DrunkBubbleLeft extends DrunkBubble {
	private static final long serialVersionUID = -5373903833913618824L;
	
	public DrunkBubbleLeft(Bubble bubble) {
		super(bubble);
		acceleration = -DrunkBubble.ACCELERATION;
	}
	
	public DrunkBubbleLeft() {
		this(new AbstractBubble());
	}
	
	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(ARROW_IMAGE,
				(int) bubble.getX() + bubble.getWidth() - IMAGE_TRANSLATION, (int) bubble.getY(),
				-ARROW_WIDTH, ARROW_HEIGHT, null);
		bubble.render(graphics);
	}
}
