package nl.tudelft.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public interface DefaultMouseMoveListener extends MouseMotionListener {
	
	@Override
	default void mouseDragged(MouseEvent e) {};
	
}
