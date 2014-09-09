package nl.tudelft.ti2206.game;

import java.awt.event.MouseEvent;

public class MouseListener implements java.awt.event.MouseListener {

	public static final MouseListener INSTANCE = new MouseListener();

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getX());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
