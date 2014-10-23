package nl.tudelft.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * {@link MouseMotionListener} for mouse moves
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@FunctionalInterface
public interface DefaultMouseMoveListener extends MouseMotionListener {
	
	@Override
	default void mouseDragged(MouseEvent e) {};
	
}
