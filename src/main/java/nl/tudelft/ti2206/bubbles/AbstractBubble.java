package nl.tudelft.ti2206.bubbles;

import java.awt.Point;

public abstract class AbstractBubble implements Bubble {
	
	private static final Point ORIGIN = new Point(0,0);
	
	private Bubble topLeft;
	private Bubble topRight;
	private Bubble left;
	private Bubble right;
	private Bubble bottomLeft;
	private Bubble bottomRight;
	
	protected Point position = new Point(ORIGIN.x, ORIGIN.y);
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;

	@Override
	public void setPosition(final Point position) {
		this.position = position;
	}

	@Override
	public Point getPosition() {
		return position;
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
		if(this.topLeft != null) {
			return new Point(topLeft.getX() + WIDTH / 2, topLeft.getY() + HEIGHT);
		}
		else if(this.topRight != null) {
			return new Point(topRight.getX() - WIDTH / 2, topRight.getY() + HEIGHT);
		}
		else if(this.left != null) {
			return new Point(left.getX() + WIDTH, left.getY());
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
			botLeft.setBottomLeft(this);
	}
	
	@Override
	public void bindBottomRight(Bubble botLeft){
		this.setBottomRight(botLeft);
		if(botLeft != null)
			botLeft.setBottomRight(this);
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
		double distance= this.getCentre().distance(b.getCentre());
		return distance<WIDTH;
	}
	
	@Override
	public Point getCentre(){
		return new Point(position.x+(WIDTH/2), position.y+(HEIGHT/2));
	}
	
	public Bubble[] getNeighbours(){
		Bubble[] p = {topLeft, topRight,left,right,bottomLeft,bottomRight};
		return p;
	}
	
	public BubblePlaceholder[] getPlaceHolderNeighbours(){
		Bubble[]p=getNeighbours();
		int i=0;
		for(Bubble b : p){
			if (b instanceof BubblePlaceholder){
				i++;
			}
		}
		BubblePlaceholder[] placeholderNeighbours=new BubblePlaceholder[i];
		i=0;
		for(Bubble b : p){
			if (b instanceof BubblePlaceholder){
				placeholderNeighbours[i]=(BubblePlaceholder) b;
				i++;
			}
		}
		return placeholderNeighbours;
	}
	
}
