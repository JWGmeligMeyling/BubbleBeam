package nl.tudelft.ti2206.cannon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observer;

import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.CannonRotate;
import nl.tudelft.util.Vector2f;
import nl.tudelft.ti2206.game.event.CannonListener.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Testcase for the {@link MouseCannonController}. This test extends
 * {@link AbstractCannonControllerTest} because all tests for
 * {@link AbstractCannonController} should pass for the
 * {@link MouseCannonController}.
 * 
 * @author Jan-Willem Gmelig meyling
 *
 */
public class MouseCannonControllerTest extends AbstractCannonControllerTest {
	
	protected Cannon cannon;
	protected MouseCannonController mouseCannonController;

	@Before
	public void setUp() throws Exception {
		cannonModel = spy(new CannonModel());
		cannonController = mouseCannonController = spy(new MouseCannonController(cannonModel));
		shootListener = mock(CannonShootListener.class);
		cannonModel.addEventListener(shootListener);
		cannon = spy(new Cannon(cannonController, new Point(50,50)));
		assertEquals(cannonModel, cannonController.getModel());
		resetMocks();
	}
	
	@Override
	protected void resetMocks() {
		super.resetMocks();
		reset(mouseCannonController);
		reset(cannon);
	}

	@Test
	public void testMouseMotionListenerBinding() {
		Component component = mock(Component.class);
		mouseCannonController.bindListenersTo(component, cannon);

		ArgumentCaptor<MouseMotionListener> mouseMotionListener = ArgumentCaptor
				.forClass(MouseMotionListener.class);
		
		verify(component, times(1)).addMouseMotionListener(mouseMotionListener.capture());
		
		Point mousePosition = new Point(0,0);
		MouseEvent mouseEvent = mock(MouseEvent.class);
		when(mouseEvent.getPoint()).thenReturn(mousePosition);
		mouseMotionListener.getValue().mouseMoved(mouseEvent);
		
		Vector2f expected = (new Vector2f(mousePosition)
			.subtract(new Vector2f(cannon.getPosition())))
			.normalize();

		verify(mouseCannonController, times(1)).setAngle(expected);
	}
	
	@Test
	public void testMouseListenerBinding() {
		Component component = mock(Component.class);
		mouseCannonController.bindListenersTo(component, cannon);

		ArgumentCaptor<MouseListener> mouseListener = ArgumentCaptor
				.forClass(MouseListener.class);
		verify(component, times(1)).addMouseListener(mouseListener.capture());
		
		MouseEvent mouseEvent = mock(MouseEvent.class);
		when(mouseEvent.getButton()).thenReturn(MouseEvent.BUTTON1);
		mouseListener.getValue().mousePressed(mouseEvent);
		verify(mouseCannonController, times(1)).shoot();
	}
	
	
	@Test
	public void testMouseListenerBindingWrongButton() {
		Component component = mock(Component.class);
		mouseCannonController.bindListenersTo(component, cannon);

		ArgumentCaptor<MouseListener> mouseListener = ArgumentCaptor
				.forClass(MouseListener.class);
		verify(component, times(1)).addMouseListener(mouseListener.capture());
		
		MouseEvent mouseEvent = mock(MouseEvent.class);
		when(mouseEvent.getButton()).thenReturn(MouseEvent.BUTTON2);
		mouseListener.getValue().mousePressed(mouseEvent);
		verify(mouseCannonController, never()).shoot();
	}
	
	@Test
	public void testConnectorBinding() {
		Connector connector = mock(Connector.class);
		mouseCannonController.bindConnectorAsMaster(connector);
		verify(cannonModel, times(1)).addObserver(any(Observer.class));
		
		mouseCannonController.setAngle( new Vector2f(0, 1));
		double angle = cannonModel.getAngle(); 

		ArgumentCaptor<CannonRotate> cannonRotate = ArgumentCaptor
				.forClass(CannonRotate.class);
		
		verify(connector, times(1)).sendPacket(cannonRotate.capture());
		assertEquals(angle, cannonRotate.getValue().rotation, 1e-4);
	}

}
