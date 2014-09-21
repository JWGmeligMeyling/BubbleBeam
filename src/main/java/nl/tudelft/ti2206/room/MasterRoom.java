package nl.tudelft.ti2206.room;

import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.util.Vector2f;

/**
 * TODO: Better description.. The MasterRoom is a Room who is responsible for
 * it's own bubble creation, unlike the SlaveRoom.
 * 
 * @author Sam Smulders
 */
public class MasterRoom extends Room {
	
	public MasterRoom(Point cannonPosition, Dimension dimension, BubbleMesh bubbleMesh) {
		super(cannonPosition, dimension, bubbleMesh);
	}
	
	// TODO: Can't be called in constructor because of the MultplayerRoom.
	public void setup() {
		addBubble();
		addBubble();
		correctBubblePositions();
	}
	
	@Override
	public void shootBubble(final Vector2f direction) {
		super.shootBubble(direction);
		addBubble();
		correctBubblePositions();
	}
	
	protected void addBubble() {
		bubbleQueue.add(new ColouredBubble(bubbleMesh.getRandomRemainingColor()));
	}
	
}
