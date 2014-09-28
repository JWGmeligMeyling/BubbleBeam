package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The {@code AbstractBubble} contains logic that is shared between the Bubble
 * implementations
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class AbstractBubble implements Bubble {
	
	private static final long serialVersionUID = -3403214887629166657L;
	public static final int WIDTH = 22, HEIGHT = WIDTH;
	public static final int RADIUS = 10;
	public static final Point ORIGIN = new Point(2,2);

	protected Point position = new Point(ORIGIN.x, ORIGIN.y);
	protected Point center = new Point(ORIGIN.x + WIDTH / 2, ORIGIN.y + HEIGHT / 2);
	protected final Map<Direction, Bubble> connections  = Maps.newTreeMap();
	protected SnapBehaviour snapBehaviour;
	
	public AbstractBubble() {
		snapBehaviour = new SnapToClosest(this);
	}

	@Override
	public void setPosition(final Point position) {
		int width = getWidth(), height = getHeight();
		this.position = position;
		this.center = new Point(position.x + width / 2, position.y + height / 2);
	}
	
	@Override
	public void setCenter(final Point center) {
		int width = getWidth(), height = getHeight();
		this.center = center;
		this.position = new Point(center.x - width / 2, center.y - height / 2);
	}

	@Override
	public Point getPosition() {
		return position;
	}
	
	@Override
	public int getWidth() {
		return WIDTH;
	}
	
	@Override
	public int getHeight() {
		return HEIGHT;
	}
	
	@Override
	public Point getCenter() {
		return center;
	}
	
	@Override
	public int getRadius() {
		return RADIUS;
	}

	@Override
	public int getX() {
		return position.x;
	}

	@Override
	public int getY() {
		return position.y;
	}

	private static final List<Direction> inOrderDirections = ImmutableList.of(
			Direction.TOPLEFT, Direction.TOPRIGHT, Direction.LEFT,
			Direction.RIGHT, Direction.BOTTOMLEFT, Direction.BOTTOMRIGHT);
	
	@Override
	public Point calculatePosition() {
		for(Direction direction : inOrderDirections) {
			Bubble bubble = this.getBubbleAt(direction);
			if(bubble == null) 
				continue;
			switch(direction) {
			case TOPLEFT:
				return new Point(bubble.getX() + WIDTH / 2, bubble.getY() + HEIGHT);
			case TOPRIGHT:
				return new Point(bubble.getX() - WIDTH / 2, bubble.getY() + HEIGHT);
			case LEFT:
				return new Point(bubble.getX() + WIDTH, bubble.getY());
			case RIGHT:
				return new Point(bubble.getX() - WIDTH, bubble.getY());
			case BOTTOMLEFT:
				return new Point(bubble.getX() + WIDTH / 2, bubble.getY() - HEIGHT);
			case BOTTOMRIGHT:
				return new Point(bubble.getX() - WIDTH / 2, bubble.getY() - HEIGHT);
			}
		}
		return position;
	}

	@Override
	public Bubble getBubbleAt(final Direction direction) {
		return connections.get(direction);
	}
	
	@Override
	public void setBubbleAt(final Direction direction, final Bubble bubble) {
		if(bubble == null) {
			connections.remove(direction);
		}
		else {
			connections.put(direction, bubble);
		}
	}
	
	@Override
	public boolean hasBubbleAt(final Direction direction) {
		return connections.containsKey(direction);
	}
	
	@Override
	public boolean isHittable() {
		return false;
	}
	
	@Override
	public Collection<Bubble> getNeighbours() {
		return connections.values();
	}
	
	@Override
	public <T extends Bubble> List<T> getNeighboursOfType(Class<T> type) {
		return Lists.newArrayList(Iterables.filter(getNeighbours(), type));
	}
	
	@Override
	public BubblePlaceholder getSnapPosition(final Bubble bubble) {
		return snapBehaviour.getSnapPosition(bubble);
	}
	
	@Override
	public void render(Graphics graphics) {
//		renderDebugLines((Graphics2D) graphics);
	}
	
	protected void renderDebugLines(final Graphics2D g2) {
		g2.setColor(Color.black);
		if(this.hasBubbleAt(Direction.RIGHT) && this.getBubbleAt(Direction.RIGHT).getBubbleAt(Direction.LEFT).equals(this)){
			g2.drawLine(this.getCenter().x, this.getCenter().y,this.getBubbleAt(Direction.RIGHT).getCenter().x ,this.getBubbleAt(Direction.RIGHT).getCenter().y);
		}
		
		if(this.hasBubbleAt(Direction.BOTTOMRIGHT) && this.getBubbleAt(Direction.BOTTOMRIGHT).getBubbleAt(Direction.TOPLEFT).equals(this)){
			g2.drawLine(this.getCenter().x, this.getCenter().y,this.getBubbleAt(Direction.BOTTOMRIGHT).getCenter().x ,this.getBubbleAt(Direction.BOTTOMRIGHT).getCenter().y);
		}
		
		if(this.hasBubbleAt(Direction.BOTTOMLEFT) && this.getBubbleAt(Direction.BOTTOMLEFT).getBubbleAt(Direction.TOPRIGHT).equals(this)){
			g2.drawLine(this.getCenter().x, this.getCenter().y,this.getBubbleAt(Direction.BOTTOMLEFT).getCenter().x ,this.getBubbleAt(Direction.BOTTOMLEFT).getCenter().y);
		}
	}
	
	@Override
	@VisibleForTesting
	public Stream<Bubble> traverse(Direction direction) {
		// Mockito doesn't have support for spying default methods yet
		final List<Bubble> bubbles = Lists.newArrayList(this);
		Bubble current = this;
		while(current.hasBubbleAt(direction)) {
			current = current.getBubbleAt(direction);
			bubbles.add(current);
		}
		return bubbles.stream();
	}
	
	@Override
	@VisibleForTesting
	public Map<Direction, Bubble> getConnections() {
		return connections;
	}

	@Override
	public boolean popsWith(Bubble target) {
		return false;
	}
	
}
