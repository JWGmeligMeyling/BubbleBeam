package nl.tudelft.ti2206.bubbles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class AbstractBubble implements Bubble {
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final int RADIUS = 14;
	public static final int SPACING = WIDTH - RADIUS * 2;
	
	protected static final Point ORIGIN = new Point(0,0);
	
	private boolean origin = false;
	private boolean top = false;
	
	private Bubble topLeft;
	private Bubble topRight;
	private Bubble left;
	private Bubble right;
	private Bubble bottomLeft;
	private Bubble bottomRight;
	
	private Point position = new Point(ORIGIN.x, ORIGIN.y);

	public void setOrigin() {
		origin = true;
	}
	
	public void setTop() {
		top = true;
	}
	
	public boolean connectedToTop() {
		return connectedToTop(Sets.newHashSet());
	}
	
	@Override
	public boolean connectedToTop(final Set<Bubble> traversed) {
		return top
			|| traversed.add(this)
			&& (
				(hasTopLeft() && !traversed.contains(topLeft) && topLeft.connectedToTop(traversed))
			||	(hasTopRight() && !traversed.contains(topRight) && topRight.connectedToTop(traversed))
			||	(hasLeft() && !traversed.contains(left) && left.connectedToTop(traversed))
			||	(hasRight() && !traversed.contains(right) && right.connectedToTop(traversed)));
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
			if(this.hasTopLeft()) {
				return new Point(topLeft.getX() + WIDTH / 2, topLeft.getY() + HEIGHT);
			}
			else if(this.hasTopRight()) {
				return new Point(topRight.getX() - WIDTH / 2, topRight.getY() + HEIGHT);
			}
			else if(this.hasLeft()) {
				return new Point(left.getX() + WIDTH, left.getY());
			}
			else if(this.hasRight()) {
				return new Point(right.getX() - WIDTH, right.getY());
			}
			else if(this.hasBottomLeft()) {
				return new Point(bottomLeft.getX() + WIDTH / 2, bottomLeft.getY() - HEIGHT);
			}
			else if(this.hasBottomRight()) {
				return new Point(bottomRight.getX() - WIDTH / 2, bottomRight.getY() - HEIGHT);
			}
		}
		return position;
	}

	
	@Override
	public void bindTopRight(Bubble topRight) {
		this.setTopRight(topRight);
		if(topRight != null)
			topRight.setBottomLeft(this);
	}

	@Override
	public void bindLeft(Bubble left) {
		this.setLeft(left);
		if(left != null) {
			left.setRight(this);
		}
	}

	@Override
	public void bindRight(Bubble right) {
		this.setRight(right);
		if(right != null) {
			right.setLeft(this);
		}
	}

	@Override
	public void bindTopLeft(Bubble topLeft) {
		this.setTopLeft(topLeft);
		if(topLeft != null)
			topLeft.setBottomRight(this);
	}
	@Override
	public void bindBottomLeft(Bubble botLeft){
		this.setBottomLeft(botLeft);
		if(botLeft != null)
			botLeft.setTopRight(this);
	}
	
	@Override
	public void bindBottomRight(Bubble botLeft){
		this.setBottomRight(botLeft);
		if(botLeft != null)
			botLeft.setTopLeft(this);
	}
	
	
	
	@Override
	public Bubble getTopLeft() {
		return topLeft;
	}

	@Override
	public void setTopLeft(Bubble topLeft) {
		this.topLeft = topLeft;
	}

	@Override
	public Bubble getTopRight() {
		return topRight;
	}

	@Override
	public void setTopRight(Bubble topRight) {
		this.topRight = topRight;
	}

	@Override
	public Bubble getLeft() {
		return left;
	}

	@Override
	public void setLeft(Bubble left) {
		this.left = left;
	}

	@Override
	public Bubble getRight() {
		return right;
	}

	@Override
	public void setRight(Bubble right) {
		this.right = right;
	}

	@Override
	public Bubble getBottomLeft() {
		return bottomLeft;
	}

	@Override
	public void setBottomLeft(Bubble bottomLeft) {
		this.bottomLeft = bottomLeft;
	}

	@Override
	public Bubble getBottomRight() {
		return bottomRight;
	}

	@Override
	public void setBottomRight(Bubble bottomRight) {
		this.bottomRight = bottomRight;
	}
	
	@Override
	public boolean hasTopLeft() {
		return topLeft != null;
	}
	
	@Override
	public boolean hasTopRight() {
		return topRight != null;
	}
	
	@Override
	public boolean hasLeft() {
		return left != null;
	}
	
	@Override
	public boolean hasRight() {
		return right != null;
	}
	
	@Override
	public boolean hasBottomLeft() {
		return bottomLeft != null;
	}
	
	@Override
	public boolean hasBottomRight() {
		return bottomRight != null;
	}
	
	@Override
	public boolean intersect(Bubble b){
		double distance= this.getDistance(b);
		return distance<WIDTH-5;
	}
	
	@Override
	public List<Bubble> getNeighbours() {
		List<Bubble> neighbours = Lists.newArrayList();
		if(this.hasTopLeft()) neighbours.add(topLeft);
		if(this.hasTopRight()) neighbours.add(topRight);
		if(this.hasLeft()) neighbours.add(left);
		if(this.hasRight()) neighbours.add(right);
		if(this.hasBottomLeft()) neighbours.add(bottomLeft);
		if(this.hasBottomRight()) neighbours.add(bottomRight);
		return neighbours;
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
	//	renderDebugLines((Graphics2D) graphics);
	}
	
	protected void renderDebugLines(final Graphics2D g2) {
		g2.setColor(Color.black);
		if(this.hasRight() && this.right.getLeft().equals(this)){
			g2.drawLine(this.getCenter().x, this.getCenter().y,this.getRight().getCenter().x ,this.getRight().getCenter().y);
		}
		
		if(this.hasBottomRight() && this.bottomRight.getTopLeft().equals(this)){
			g2.drawLine(this.getCenter().x, this.getCenter().y,this.getBottomRight().getCenter().x ,this.getBottomRight().getCenter().y);
		}
		
		if(this.hasBottomLeft() && this.bottomLeft.getTopRight().equals(this)){
			g2.drawLine(this.getCenter().x, this.getCenter().y,this.getBottomLeft().getCenter().x ,this.getBottomLeft().getCenter().y);
		}
	}
	
	@Override
	public void replaceWith(final Bubble toReplace){
		toReplace.bindTopLeft(topLeft);
		toReplace.bindTopRight(topRight);
		toReplace.bindLeft(left);
		toReplace.bindRight(right);
		toReplace.bindBottomLeft(bottomLeft);
		toReplace.bindBottomRight(bottomRight);
		toReplace.setPosition(position);
	}
	
}
