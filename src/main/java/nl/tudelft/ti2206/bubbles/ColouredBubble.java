package nl.tudelft.ti2206.bubbles;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMeshImpl;
import nl.tudelft.ti2206.bubbles.snap.SnapToClosest;

/**
 * The Bubbles on the mesh with various colours
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class ColouredBubble extends AbstractBubble {
	
	private static final long serialVersionUID = -5892206967489729767L;
	
	protected final static AudioClip POPSOUND = Applet.newAudioClip(BubbleMeshImpl.class
			.getResource("/bubble_pop.wav"));
	protected final static AudioClip SNAPSOUND = Applet.newAudioClip(BubbleMeshImpl.class
			.getResource("/bubble_snap.wav"));
	
	protected Color color;
	protected final static HashMap<Color, BufferedImage> BUBBLE_IMAGES = new HashMap<Color, BufferedImage>();
	
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
	
	protected static HashMap<Color, BufferedImage> _createBubbleImages() {
		HashMap<Color, BufferedImage> colorMap = new HashMap<Color, BufferedImage>();
		colorMap.put(Color.RED, _createBubbleImage(Color.RED));
		return colorMap;
	}
	
	protected static BufferedImage getBubbleImage(Color color) {
		if (!BUBBLE_IMAGES.containsKey(color)) {
			BUBBLE_IMAGES.put(color, _createBubbleImage(color));
		}
		return BUBBLE_IMAGES.get(color);
	}
	
	protected static BufferedImage _createBubbleImage(Color color) {
		BufferedImage img = new BufferedImage(AbstractBubble.WIDTH, AbstractBubble.HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		_renderBubble((Graphics2D) img.getGraphics(), color);
		return img;
	}
	
	protected static void _renderBubble(Graphics2D g2, Color color) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(color);
		g2.setPaint(new RadialGradientPaint(AbstractBubble.ORIGIN, RADIUS,
				BASE_COLOR_GRADIENT_RANGE, new Color[] { lighter(color), darker(color) }));
		g2.fillOval(1, 1, AbstractBubble.RADIUS * 2, AbstractBubble.RADIUS * 2);
		applyHighlight(g2, AbstractBubble.ORIGIN);
		applyShadow(g2, AbstractBubble.ORIGIN);
	}
	
	public final static float DARKING_FACTOR = 0.85f;
	public final static float LIGHTING_FACTOR = 1.3f;
	
	private static Color lighter(Color color) {
		return new Color(Math.min((int)  (color.getRed() * LIGHTING_FACTOR), 255),
				Math.min((int) (color.getGreen() * LIGHTING_FACTOR), 255),
				Math.min((int) (color.getBlue() * LIGHTING_FACTOR), 255));
	}
	
	private static Color darker(Color color) {
		return new Color((int) (color.getRed() * DARKING_FACTOR),
				(int) (color.getGreen() * DARKING_FACTOR),
				(int) (color.getBlue() * DARKING_FACTOR));
	}
	
	private static int SHADOW_OFFSET = 9, SHADOW_RADIUS = 6, SHADOW_DIAMETER = SHADOW_RADIUS * 2;
	private static float[] SHADOW_GRADIENT_RANGE = new float[] { 0.0f, 1f };
	private static Color[] SHADOW_GRADIENT_COLOURS = new Color[] {
			new Color(0.0f, 0.0f, 0.0f, 0.2f), new Color(0.0f, 0.0f, 0.0f, 0.0f) };
	
	protected static void applyShadow(Graphics2D g2, Point origin) {
		Point shadowPos = new Point(SHADOW_OFFSET, SHADOW_OFFSET);
		Point shadowCenter = new Point(SHADOW_OFFSET + SHADOW_RADIUS, SHADOW_RADIUS + SHADOW_OFFSET);
		g2.setPaint(new RadialGradientPaint(shadowCenter, SHADOW_RADIUS, SHADOW_GRADIENT_RANGE,
				SHADOW_GRADIENT_COLOURS));
		g2.fillOval(shadowPos.x, shadowPos.y, SHADOW_DIAMETER, SHADOW_DIAMETER);
	}
	
	private static int HIGHLIGHT_OFFSET = 2, HIGHLIGHT_RADIUS = 6,
			HIGHLIGHT_DIAMETER = HIGHLIGHT_RADIUS * 2;
	private static float[] HIGHLIGHT_GRADIENT_RANGE = new float[] { 0.0f, 1f };
	private static Color[] HIGHLIGHT_GRADIENT_COLOURS = new Color[] {
			new Color(1.0f, 1.0f, 1.0f, 0.5f), new Color(1.0f, 1.0f, 1.0f, 0f) };
	
	protected static void applyHighlight(Graphics2D g2, Point origin) {
		Point highlightPos = new Point(HIGHLIGHT_OFFSET, HIGHLIGHT_OFFSET);
		Point highlightCenter = new Point(highlightPos.x + HIGHLIGHT_RADIUS, highlightPos.y
				+ HIGHLIGHT_RADIUS);
		g2.setPaint(new RadialGradientPaint(highlightCenter, HIGHLIGHT_RADIUS,
				HIGHLIGHT_GRADIENT_RANGE, HIGHLIGHT_GRADIENT_COLOURS));
		g2.fillOval(highlightPos.x, highlightPos.y, HIGHLIGHT_DIAMETER, HIGHLIGHT_DIAMETER);
	}
	
	@Override
	public void render(final Graphics graphics) {
		super.render(graphics);
		final Graphics2D g2 = (Graphics2D) graphics;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int xBegin = this.getX();
		int xEnd = xBegin + this.getWidth();
		
		int yBegin = this.getY();
		int yEnd = yBegin + this.getHeight();
		
		g2.drawImage(getBubbleImage(this.color), xBegin, yBegin, xEnd, yEnd, 0, 0, this.getWidth(),
				this.getHeight(), null);
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
	public void snapHook() {
		if (SNAPSOUND != null)
			SNAPSOUND.play();
	}
	
	@Override
	public void popHook() {
		if (POPSOUND != null)
			POPSOUND.play();
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
