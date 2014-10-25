package nl.tudelft.ti2206.graphics.animations;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import nl.tudelft.ti2206.bubbles.Bubble;

/**
 * FallAnimation animates a bubble falling down.
 * 
 * @author Sam Smulders
 */
public class FallAnimation extends FiniteAnimation {
	protected static final int FALL_SPEED = 4;
	protected Bubble bubble;
	protected Point position;
	
	public FallAnimation(Bubble bubble) {
		super(bubble.getHeight());
		this.bubble = bubble;
		Point pos = bubble.getPosition();
		this.position = new Point(pos.x, pos.y);
		this.bubble.setPosition(new Point(0, 0));
	}
	
	@Override
	public void render(final Graphics graphics) {
		final Graphics2D g2 = (Graphics2D) graphics;
		BufferedImage img = renderBubble();
		setAlpha(g2);
		
		int drop = time * FALL_SPEED;
		
		int xBegin = this.position.x;
		int xEnd = xBegin + bubble.getWidth();
		
		int yBegin = this.position.y + drop;
		int yEnd = yBegin + bubble.getHeight();
		
		g2.drawImage(img, xBegin, yBegin, xEnd, yEnd, 0, 0, bubble.getWidth(), bubble.getHeight(),
				null);
	}
	
	private void setAlpha(Graphics2D g2) {
		float alpha = 1f / maxTime * this.getTimeLeft();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2.setComposite(ac);
	}
	
	private BufferedImage renderBubble() {
		BufferedImage img = new BufferedImage(bubble.getWidth(), bubble.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		bubble.render(img.getGraphics());
		return img;
	}
}
