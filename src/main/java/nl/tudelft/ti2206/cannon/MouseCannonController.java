package nl.tudelft.ti2206.cannon;

import java.awt.Component;
import java.awt.event.MouseEvent;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.CannonRotate;
import nl.tudelft.ti2206.network.packets.Packet;
import nl.tudelft.util.DefaultMouseClickListener;
import nl.tudelft.util.DefaultMouseMoveListener;
import nl.tudelft.util.Vector2f;

/**
 * The {@code MouseCannonController} is a controller who's behaviour is
 * determined by behaviour of the mouse.
 * 
 * @author Sam Smulders
 */
public class MouseCannonController extends AbstractCannonController {
	
	public MouseCannonController() {
		super();
	}
	
	public MouseCannonController(final CannonModel cannonModel) {
		super(cannonModel);
	}
	
	public void bindListenersTo(final Component component, final Cannon cannon) {
		component.addMouseMotionListener(new DefaultMouseMoveListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Vector2f direction = (new Vector2f(e.getPoint())
						.subtract(new Vector2f(cannon.getPosition())))
						.normalize();
				MouseCannonController.this.setAngle(direction);
			}
			
		});
		
		component.addMouseListener(new DefaultMouseClickListener() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					MouseCannonController.this.shoot();
				}
			}
			
		});
	}
	
	public void bindConnectorAsMaster(final Connector connector) {
		model.addObserver((a,b) -> {
			Packet packet = new CannonRotate(model.getAngle());
			connector.sendPacket(packet);
		});
	}
	
}
