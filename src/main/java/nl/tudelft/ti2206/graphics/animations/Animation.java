package nl.tudelft.ti2206.graphics.animations;

import java.awt.Graphics;

/**
 * The animation interface
 * 
 * @author Sam Smulders
 */
public interface Animation {
	
	/**
	 * Render the animation for the current frame
	 * @param graphics {@link Graphics} to paint the {@link Animation} on
	 */
	public void render(Graphics graphics);
	
}
