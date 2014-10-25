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
	
	protected final Bubble bubble;
	protected final Point position;
	protected final int width, height;
	
	public FallAnimation(Bubble bubble) {
		super(bubble.getHeight());
		Point pos = bubble.getPosition();

		this.bubble = bubble;
		this.position = new Point(pos.x, pos.y);
		this.width = bubble.getWidth();
		this.height = bubble.getHeight();
		this.bubble.setPosition(new Point(0, 0));
	}
	
	@Override
	public void render(final Graphics graphics) {
		final Graphics2D g2 = (Graphics2D) graphics;
		BufferedImage img = renderBubble();
		setAlpha(g2);
		
		int drop = time * FALL_SPEED;
		int xBegin = this.position.x;
		int xEnd = xBegin + width;
		int yBegin = this.position.y + drop;
		int yEnd = yBegin + height;
		
		g2.drawImage(img, xBegin, yBegin, xEnd, yEnd, 0, 0, width, height, null);
	}
	
	private void setAlpha(Graphics2D g2) {
		float alpha = 1f / maxTime * this.getTimeLeft();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2.setComposite(ac);
	}
	
	private BufferedImage renderBubble() {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		bubble.render(img.getGraphics());
		return img;
	}
}
