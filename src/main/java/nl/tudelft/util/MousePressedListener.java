package nl.tudelft.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * {@link MouseListener} for mouse pressed events
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@FunctionalInterface
public interface MousePressedListener extends MouseListener {

	@Override
	default void mouseClicked(MouseEvent e) {};

	@Override
	default void mouseReleased(MouseEvent e) {};

	@Override
	default void mouseEntered(MouseEvent e) {};

	@Override
	default void mouseExited(MouseEvent e) {};
	 
}
