package nl.tudelft.ti2206.game;

import java.awt.Point;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.util.Vector2f;

public class MovingBubble {
	
	private static final float SPEED_MULTIPLIER = 2.5f;
	
	protected Vector2f velocity;
	protected Vector2f truePosition;
	protected Bubble bubble;
	
	public MovingBubble(Point position, Bubble bubble, Vector2f velocity) {
		this.bubble = bubble;
		System.out.println(position.toString());
		this.truePosition = new Vector2f(position.x, position.y);
		this.velocity = velocity;
	}
	public void gameStep() {
		truePosition = truePosition.add(velocity.multiply(SPEED_MULTIPLIER));
		bubble.setPosition(new Point((int) Math.round(truePosition.x),
				(int) Math.round(truePosition.y)));
	}
	
	public Bubble getBubble(){
		return bubble;
	}
}
