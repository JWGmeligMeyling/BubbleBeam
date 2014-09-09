package nl.tudelft.ti2206.game;

import java.awt.Point;

import nl.tudelft.ti2206.bubbles.Bubble;

public class MovingBubble {
	
	public MovingBubble(Point position, Bubble bubble, Vector2f velocity) {
		this.bubble = bubble;
		System.out.println(position.toString());
		this.truePosition = new Vector2f(position.x, position.y);
		this.velocity = velocity;
	}
	
	protected Vector2f velocity;
	protected Vector2f truePosition;
	protected Bubble bubble;
	
	public void gameStep() {
		truePosition.add(velocity);
		bubble.setPosition(new Point((int) Math.round(truePosition.x), (int) Math
				.round(truePosition.y)));
		//GuiThrowAway.instance.GUI.repaint();
	}
	
	public Bubble getBubble(){
		return bubble;
	}
}
