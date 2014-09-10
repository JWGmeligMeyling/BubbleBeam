package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.util.Random;

public class ColouredBubble extends AbstractBubble {

	private static final int RADIUS = 14;
	private static final int DIAGONAL = RADIUS * 2;
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	private static final int SPACING = WIDTH - RADIUS * 2;
	private Colour color = Colour.pickRandom();

	@Override
	public void render(final Graphics graphics) {
		final Graphics2D g2 = (Graphics2D) graphics;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		final Point center = new Point(this.getX(), this.getY());
		center.translate(WIDTH / 2, HEIGHT / 2);
		fillBaseColour(g2, center, color.getColor());
	}
	
	private static float[] BASE_COLOR_GRADIENT_RANGE = new float[] { 0.0f, 1.0f };
	
	private void fillBaseColour(final Graphics2D graphics, final Point center, final Color baseColor) {
		graphics.setColor(baseColor);
		graphics.setPaint(new RadialGradientPaint(center, RADIUS, BASE_COLOR_GRADIENT_RANGE, new Color[] {
			baseColor.brighter(), baseColor.darker()
		}));
		graphics.fillOval(position.x + SPACING, position.y + SPACING, DIAGONAL , DIAGONAL);
		applyHighlight(graphics, center);
		applyShadow(graphics, center);
	}
	
	private static int HIGHLIGHT_OFFSET = 8, HIGHLIGHT_RADIUS = 6, HIGHLIGHT_DIAMETER = HIGHLIGHT_RADIUS * 2;
	private static float[] HIGHLIGHT_GRADIENT_RANGE = new float[] { 0.0f, 1f };
	private static Color[] HIGHLIGHT_GRADIENT_COLOURS = new Color[] {
		 new Color(1.0f, 1.0f, 1.0f, 0.5f), new Color(1.0f, 1.0f, 1.0f, 0f)};
	
	private void applyHighlight(final Graphics2D graphics, final Point center) {
		Point highlightPos = new Point(position.x + HIGHLIGHT_OFFSET, position.y + HIGHLIGHT_OFFSET);
		Point highlightCenter = new Point(highlightPos.x + HIGHLIGHT_RADIUS, highlightPos.y + HIGHLIGHT_RADIUS);
		graphics.setPaint(new RadialGradientPaint(highlightCenter, HIGHLIGHT_RADIUS,
				HIGHLIGHT_GRADIENT_RANGE, HIGHLIGHT_GRADIENT_COLOURS));
		graphics.fillOval(highlightPos.x, highlightPos.y, HIGHLIGHT_DIAMETER, HIGHLIGHT_DIAMETER);
	}
	
	private static int SHADOW_OFFSET = 16, SHADOW_RADIUS = 6, SHADOW_DIAMETER = SHADOW_RADIUS * 2;
	private static float[] SHADOW_GRADIENT_RANGE = new float[] { 0.0f, .9f };
	private static Color[] SHADOW_GRADIENT_COLOURS = new Color[] {
		 new Color(0.0f, 0.0f, 0.0f, 0.2f), new Color(0.0f, 0.0f, 0.0f, 0f)};
	
	private void applyShadow(final Graphics2D graphics, final Point center) {
		Point shadowPos = new Point(position.x + SHADOW_OFFSET, position.y + SHADOW_OFFSET);
		Point shadowCenter = new Point(shadowPos.x + SHADOW_RADIUS, shadowPos.y + SHADOW_RADIUS);
		graphics.setPaint(new RadialGradientPaint(shadowCenter, HIGHLIGHT_RADIUS,
				SHADOW_GRADIENT_RANGE, SHADOW_GRADIENT_COLOURS));
		graphics.fillOval(shadowPos.x, shadowPos.y, SHADOW_DIAMETER, SHADOW_DIAMETER);
	}
	
	public Colour getColor() {
		return color;
	}

	public void setColor(Colour color) {
		this.color = color;
	}

	public enum Colour {
		RED(Color.RED),
		GREEN(Color.GREEN),
		BLUE(Color.BLUE),
		YELLOW(Color.YELLOW),
		MAGENTA(Color.MAGENTA),
		CYAN(Color.CYAN);

		private final Color color;

		private Colour(final Color color) {
			this.color = color;
		}
		
		public Color getColor() {
			return color;
		}

		private static final Random RANDOM_GENERATOR = new Random();
		
		public static Colour pickRandom() {
			Colour[] values = Colour.values();
			int index = RANDOM_GENERATOR.nextInt(values.length);
			return values[index];
		}
		
	}
}
