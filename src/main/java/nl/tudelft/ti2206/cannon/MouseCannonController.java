package nl.tudelft.ti2206.cannon;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import nl.tudelft.ti2206.room.Room;
import nl.tudelft.util.Vector2f;

/**
 * The {@code MouseCannonController} is a controller who's behaviour is
 * determined by behaviour of the mouse.
 * 
 * @author Sam Smulders
 */
public class MouseCannonController extends CannonController implements MouseMotionListener,
		MouseListener {
	
	protected Vector2f direction = new Vector2f(0, 1);
	protected final Room room;
	
	public MouseCannonController(Room room) {
		this.room = room;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Vector2f dir = (new Vector2f(e.getPoint()).subtract(new Vector2f(room.cannonPosition)))
				.normalize();
		if (dir.y > -Cannon.MIN_DIRECTION_Y) {
			dir.x = Cannon.MIN_DIRECTION_X * Math.signum(dir.x);
			dir.y = -Cannon.MIN_DIRECTION_Y;
		}
		this.notifyObserversRotate(Math.atan(dir.x / dir.y) + Math.PI / 2);
		this.direction = dir;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (room.canShoot()) {
				this.notifyObserversShoot(direction);
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
