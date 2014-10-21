package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import nl.tudelft.ti2206.bubbles.snap.SnapToClosest;

/**
 * The Bubbles on the mesh with various colours
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class ColouredBubble extends AbstractBubble {

	private static final long serialVersionUID = -5892206967489729767L;

	protected Color color;
	
	/**
	 * Construct a new {@code ColouredBubble}
	 * 
	 * @param color
	 *            {@link Color} that the {@code Bubble} should have
	 */
	public ColouredBubble(final Color color) {
		snapBehaviour = new SnapToClosest(this);
		this.color = color;
	}

	@Override
	public void render(final Graphics graphics) {
		super.render(graphics);
		final Graphics2D g2 = (Graphics2D) graphics;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		fillBaseColour(g2, color);
	}

	protected static float[] BASE_COLOR_GRADIENT_RANGE = new float[] { 0.0f, 1.0f };

	protected void fillBaseColour(final Graphics2D graphics, final Color baseColor) {
		graphics.setColor(baseColor);
		int diameter = getDiameter();
		int offset = getWidth() / 2 - getRadius();
		graphics.fillOval(position.x + offset, position.y + offset, diameter, diameter);
	}

	public void setColor(final Color color) {
		this.color = color;
	}
	
	@Override
	public Color getColor() {
		return color;
	}


	@Override
	public boolean hasColor() {
		return true;
	}
	
	@Override
	public boolean isHittable() {
		return true;
	}
	
	@Override
	public boolean popsWith(Bubble target) {
		return target.hasColor() && target.getColor().equals(color);
	}

	@Override
	public String toString() {
		return super.toString() + "(" + color.toString() + ")";
	}

}
