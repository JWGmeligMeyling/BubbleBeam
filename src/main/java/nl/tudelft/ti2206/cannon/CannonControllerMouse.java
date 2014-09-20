package nl.tudelft.ti2206.cannon;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import nl.tudelft.util.Vector2f;

public class CannonControllerMouse implements CannonController, MouseMotionListener, MouseListener {
	
	protected Cannon2 cannon;
	private Vector2f direction;
	private Room room;
	
	@Override
	public void setCannon(Cannon2 cannon) {
		this.cannon = cannon;
		this.room = cannon.room;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Vector2f dir = (new Vector2f(e.getPoint()).subtract(new Vector2f(cannon.position)))
				.normalize();
		dir.y = Math.abs(direction.y);
		if (dir.y < Cannon2.MIN_DIRECTION_Y) {
			dir.x = Cannon2.MIN_DIRECTION_X * Math.signum(direction.x);
			dir.y = Cannon2.MIN_DIRECTION_Y;
		}
		cannon.angle = Math.atan(dir.y / dir.x);
		direction = dir;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (room.canShoot()) {
				room.shootBubble(direction);
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
}
