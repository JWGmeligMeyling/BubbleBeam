package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;

/**
 * The Bubbles on the mesh with various colours
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class ColouredBubble extends AbstractBubble {

	private final Color color;

	/**
	 * Construct a new {@code ColouredBubble}
	 * 
	 * @param color
	 *            {@link Color} that the {@code Bubble} should have
	 */
	public ColouredBubble(final Color color) {
		this.color = color;
	}

	@Override
	public void render(final Graphics graphics) {
		super.render(graphics);
		final Graphics2D g2 = (Graphics2D) graphics;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		final Point center = new Point(this.getX(), this.getY());
		center.translate(WIDTH / 2, HEIGHT / 2);
		fillBaseColour(g2, center, color);
	}

	protected static float[] BASE_COLOR_GRADIENT_RANGE = new float[] { 0.0f, 1.0f };

	protected void fillBaseColour(final Graphics2D graphics, final Point center,
			final Color baseColor) {
		final Point position = getPosition();
		graphics.setColor(baseColor);
//		graphics.setPaint(new RadialGradientPaint(center, RADIUS,
//				BASE_COLOR_GRADIENT_RANGE, new Color[] { baseColor.brighter(),
//						baseColor.darker() }));
		graphics.fillOval(position.x + SPACING, position.y + SPACING,
				2 * RADIUS, 2 * RADIUS);
//		applyHighlight(graphics, center);
//		applyShadow(graphics, center);
	}

	private static int HIGHLIGHT_OFFSET = 8, HIGHLIGHT_RADIUS = 6,
			HIGHLIGHT_DIAMETER = HIGHLIGHT_RADIUS * 2;
	private static float[] HIGHLIGHT_GRADIENT_RANGE = new float[] { 0.0f, 1f };
	private static Color[] HIGHLIGHT_GRADIENT_COLOURS = new Color[] {
			new Color(1.0f, 1.0f, 1.0f, 0.5f), new Color(1.0f, 1.0f, 1.0f, 0f) };

	protected void applyHighlight(final Graphics2D graphics, final Point center) {
		final Point position = getPosition();
		Point highlightPos = new Point(position.x + HIGHLIGHT_OFFSET,
				position.y + HIGHLIGHT_OFFSET);
		Point highlightCenter = new Point(highlightPos.x + HIGHLIGHT_RADIUS,
				highlightPos.y + HIGHLIGHT_RADIUS);
		graphics.setPaint(new RadialGradientPaint(highlightCenter,
				HIGHLIGHT_RADIUS, HIGHLIGHT_GRADIENT_RANGE,
				HIGHLIGHT_GRADIENT_COLOURS));
		graphics.fillOval(highlightPos.x, highlightPos.y, HIGHLIGHT_DIAMETER,
				HIGHLIGHT_DIAMETER);
	}

	private static int SHADOW_OFFSET = 16, SHADOW_RADIUS = 6,
			SHADOW_DIAMETER = SHADOW_RADIUS * 2;
	private static float[] SHADOW_GRADIENT_RANGE = new float[] { 0.0f, .9f };
	private static Color[] SHADOW_GRADIENT_COLOURS = new Color[] {
			new Color(0.0f, 0.0f, 0.0f, 0.2f), new Color(0.0f, 0.0f, 0.0f, 0f) };

	protected void applyShadow(final Graphics2D graphics, final Point center) {
		final Point position = getPosition();
		Point shadowPos = new Point(position.x + SHADOW_OFFSET, position.y
				+ SHADOW_OFFSET);
		Point shadowCenter = new Point(shadowPos.x + SHADOW_RADIUS, shadowPos.y
				+ SHADOW_RADIUS);
		graphics.setPaint(new RadialGradientPaint(shadowCenter,
				HIGHLIGHT_RADIUS, SHADOW_GRADIENT_RANGE,
				SHADOW_GRADIENT_COLOURS));
		graphics.fillOval(shadowPos.x, shadowPos.y, SHADOW_DIAMETER,
				SHADOW_DIAMETER);
	}

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
	
}
