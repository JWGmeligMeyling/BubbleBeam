package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.Map;

import nl.tudelft.ti2206.bubbles.pop.PopBehaviour;
import nl.tudelft.util.Vector2f;

/**
 * This interface is used to use the decorator pattern with {@link Bubble} sub
 * classes.
 * 
 * @author Leon Hoek
 * @author Jan-Willem Gmelig Meyling
 * @author Sam Smulders
 * @author Luka Bavdaz
 */
public class DecoratedBubble implements Bubble {
	
	private static final long serialVersionUID = -2018291227603535293L;

	protected final Bubble bubble;
	
	public DecoratedBubble() {
		this(new AbstractBubble());
	}
	
	public DecoratedBubble(Bubble bubble) {
		this.bubble = bubble;
	}
	
	@Override
	public Vector2f velocityChange() {
		return bubble.velocityChange();
	}
	
	@Override
	public void render(Graphics graphics) {
		bubble.render(graphics);
	}
	
	@Override
	public void setPosition(Point position) {
		bubble.setPosition(position);
	}
	
	@Override
	public Point getPosition() {
		return bubble.getPosition();
	}
	
	@Override
	public int getX() {
		return bubble.getX();
	}
	
	@Override
	public int getY() {
		return bubble.getY();
	}
	
	@Override
	public int getWidth() {
		return bubble.getWidth();
	}
	
	@Override
	public int getHeight() {
		return bubble.getHeight();
	}
	
	@Override
	public int getRadius() {
		return bubble.getRadius();
	}
	
	@Override
	public Point getCenter() {
		return bubble.getCenter();
	}
	
	@Override
	public void setCenter(Point center) {
		bubble.setCenter(center);
	}
	
	@Override
	public Bubble getSnapPosition(Bubble other) {
		return this.bubble.getSnapPosition(other);
	}
	
	@Override
	public Bubble getBubbleAt(Direction direction) {
		return bubble.getBubbleAt(direction);
	}
	
	@Override
	public void setBubbleAt(Direction direction, Bubble bubble) {
		this.bubble.setBubbleAt(direction, bubble);
	}
	
	@Override
	public boolean hasBubbleAt(Direction direction) {
		return bubble.hasBubbleAt(direction);
	}
	
	@Override
	public boolean isHittable() {
		return bubble.isHittable();
	}
	
	@Override
	public Collection<Bubble> getNeighbours() {
		return bubble.getNeighbours();
	}
	
	@Override
	public Point calculatePosition() {
		return bubble.calculatePosition();
	}
	
	@Override
	public Map<Direction, Bubble> getConnections() {
		return bubble.getConnections();
	}
	
	@Override
	public boolean popsWith(Bubble target) {
		return bubble.popsWith(target);
	}
	
	@Override
	public PopBehaviour getPopBehaviour() {
		return bubble.getPopBehaviour();
	}
	
	@Override
	public void collideHook(Bubble target) {
		bubble.collideHook(target);
	}

	@Override
	public void popHook() {
		bubble.popHook();
	}

	@Override
	public void snapHook() {
		bubble.snapHook();
	}
	
	public Bubble getParent() {
		return bubble;
	}

	@Override
	public boolean hasColor() {
		return bubble.hasColor();
	}

	@Override
	public Color getColor() {
		return bubble.getColor();
	}

}
