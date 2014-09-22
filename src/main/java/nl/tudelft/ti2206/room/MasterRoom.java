package nl.tudelft.ti2206.room;

import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.util.Vector2f;

/**
 * TODO: Remove this class?
 * 
 * @author Sam Smulders
 */
public abstract class MasterRoom extends Room {
	
	public MasterRoom(Point cannonPosition, Dimension dimension, BubbleMesh bubbleMesh) {
		super(cannonPosition, dimension, bubbleMesh);
	}
	
	// TODO: Can't be called in constructor because of the MultplayerRoom.
	@Override
	public void setup() {
		addBubble();
		addBubble();
		correctBubblePositions();
		super.setup();
	}
	
	@Override
	public void shootBubble(final Vector2f direction) {
		super.shootBubble(direction);
		addBubble();
		correctBubblePositions();
	}
	
	protected void addBubble() {
		System.out.println("Master bubble creation");
		bubbleQueue.add(new ColouredBubble(bubbleMesh.getRandomRemainingColor()));
	}
	
}
