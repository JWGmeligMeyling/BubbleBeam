package nl.tudelft.ti2206.room;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.cannon.MouseCannonController;

/**
 * TODO: comment
 * 
 * @author Sam Smulders
 */
public class SingleplayerRoom extends MasterRoom {
	
	private Component component;
	
	public SingleplayerRoom(Point cannonPosition, Dimension dimension, BubbleMesh bubbleMesh,
			Component component) {
		super(cannonPosition, dimension, bubbleMesh);
		cannonController = new MouseCannonController(this);
		cannonController.registerObserver(cannon);
		this.component = component;
		component.addMouseListener((MouseCannonController) cannonController);
		component.addMouseMotionListener((MouseCannonController) cannonController);
	}
	
	@Override
	public void deconstruct() {
		cannonController.removeObserver(cannon);
		component.removeMouseListener((MouseCannonController) cannonController);
		component.removeMouseMotionListener((MouseCannonController) cannonController);
	}
}
