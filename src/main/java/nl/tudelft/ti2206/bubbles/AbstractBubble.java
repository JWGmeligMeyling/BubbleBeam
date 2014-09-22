package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
public abstract class AbstractBubble implements Bubble {
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final int RADIUS = 14;
	public static final int DIAMETER = RADIUS * 2;
	public static final int SPACING = WIDTH - DIAMETER;
	public static final Point ORIGIN = new Point(0,0);

	protected boolean origin = false;
	protected Point position = new Point(ORIGIN.x, ORIGIN.y);
	protected final Map<Direction, Bubble> connections  = Maps.newHashMap();

	@Override
	public void setOrigin(boolean value) {
		origin = value;
	}
	
	@Override
	public void setPosition(final Point position) {
		this.position = position;
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
		return new Point(position.x + RADIUS + SPACING, position.y + RADIUS + SPACING);
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
	
	public Point calculatePosition() {
		if(!origin) {
			if(this.hasBubbleAt(Direction.TOPLEFT)) {
				return new Point(getBubbleAt(Direction.TOPLEFT).getX() + WIDTH / 2, getBubbleAt(Direction.TOPLEFT).getY() + HEIGHT);
			}
			else if(this.hasBubbleAt(Direction.TOPRIGHT)) {
				return new Point(getBubbleAt(Direction.TOPRIGHT).getX() - WIDTH / 2, getBubbleAt(Direction.TOPRIGHT).getY() + HEIGHT);
			}
			else if(this.hasBubbleAt(Direction.LEFT)) {
				return new Point(getBubbleAt(Direction.LEFT).getX() + WIDTH, getBubbleAt(Direction.LEFT).getY());
			}
			else if(this.hasBubbleAt(Direction.RIGHT)) {
				return new Point(getBubbleAt(Direction.RIGHT).getX() - WIDTH, getBubbleAt(Direction.RIGHT).getY());
			}
			else if(this.hasBubbleAt(Direction.BOTTOMLEFT)) {
				return new Point(getBubbleAt(Direction.BOTTOMLEFT).getX() + WIDTH / 2, getBubbleAt(Direction.BOTTOMLEFT).getY() - HEIGHT);
			}
			else if(this.hasBubbleAt(Direction.BOTTOMRIGHT)) {
				return new Point(getBubbleAt(Direction.BOTTOMRIGHT).getX() - WIDTH / 2, getBubbleAt(Direction.BOTTOMRIGHT).getY() - HEIGHT);
			}
		}
		return position;
	}

	@Override
	public void bind(final Direction direction, final Bubble other) {
		this.setBubbleAt(direction, other);
		if(other != null) {
			other.setBubbleAt(direction.opposite(), this);
		}
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
	public boolean intersect(Bubble b){
		return this.getDistance(b) < DIAMETER;
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
	public <T> List<T> getNeighboursOfType(Class<T> type) {
		return Lists.newArrayList(Iterables.filter(getNeighbours(), type));
	}
	
	@Override
	public double getDistance(final Bubble b){
		return this.getCenter().distance(b.getCenter());
	}
	
	@Override
	public BubblePlaceholder getSnapPosition(final Bubble bubble) {
		return getNeighboursOfType(BubblePlaceholder.class)
			.stream().min((BubblePlaceholder a, BubblePlaceholder b) ->
				a.getDistance(bubble) < b.getDistance(bubble) ? -1 : 1)
			.get();
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
	
}
