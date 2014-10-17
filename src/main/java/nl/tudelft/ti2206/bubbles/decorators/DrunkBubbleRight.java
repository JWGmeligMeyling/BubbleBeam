package nl.tudelft.ti2206.bubbles.decorators;

import java.awt.Graphics;

import nl.tudelft.ti2206.bubbles.Bubble;

/**
 * @author Luka Bavdaz
 */
public class DrunkBubbleRight extends DrunkBubble {
	private static final long serialVersionUID = -2286964640875125242L;
	
	public DrunkBubbleRight(Bubble bubble) {
		super(bubble);
		acceleration = DrunkBubble.ACCELERATION;
	}
	
	@Override
	public void render(Graphics graphics) {
		if(!snapped) {
			int x = bubble.getX() + bubble.getWidth() + IMAGE_TRANSLATION;
			int y = bubble.getY();
			graphics.drawImage(ARROW_IMAGE, x, y, ARROW_WIDTH, ARROW_HEIGHT, null);
		}
		bubble.render(graphics);
	}
}
