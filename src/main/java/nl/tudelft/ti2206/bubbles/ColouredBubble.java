package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import com.google.common.annotations.VisibleForTesting;

/**
 * The Bubbles on the mesh with various colours
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class ColouredBubble extends AbstractBubble implements Coloured {

	private static final long serialVersionUID = -5892206967489729767L;

	private Color color;
	
	private Point paintStart = new Point(center.x, center.y);

	/**
	 * Construct a new {@code ColouredBubble}
	 * 
	 * @param color
	 *            {@link Color} that the {@code Bubble} should have
	 */
	public ColouredBubble(final Color color) {
		snapBehaviour = new SnapToClosest(this);
		this.color = color;
		int offset = -getRadius();
		paintStart.translate(offset, offset);
	}

	@Override
	public void setPosition(final Point position) {
		super.setPosition(position);
		int offset = getWidth() / 2 - getRadius();
		this.paintStart = new Point(position.x + offset, position.y + offset);
	}
	
	@Override
	public void setCenter(final Point center) {
		super.setCenter(center);
		int offset = getWidth() / 2 - getRadius();
		this.paintStart = new Point(position.x + offset, position.y + offset);
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
		int diameter = getRadius() * 2;
		graphics.fillOval(paintStart.x, paintStart.y, diameter, diameter);
	}

	public void setColor(final Color color) {
		this.color = color;
	}
	
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public boolean isHittable() {
		return true;
	}
	
	@Override
	public String toString() {
		return super.toString() + "(" + color.toString() + ")";
	}
	
	@VisibleForTesting
	Point getPaintStart() {
		return paintStart;
	}

	@Override
	public boolean popsWith(Bubble target) {
		if(Coloured.class.isInstance(target)) {
			Coloured other = Coloured.class.cast(target);
			Color otherColor = other.getColor();
			return color.equals(otherColor);
		}
		return false;
	}

}
