package nl.tudelft.ti2206.game;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LayerUI;

/**
 * Do not consider this part of the project. This was not written by us, and was
 * added as suggestion for the game.
 * 
 * TODO: Add author
 */
@SuppressWarnings("unchecked")
public class EffectsLayer extends LayerUI<JComponent> {
	private static final long serialVersionUID = -3945974703178210267L;
	private boolean mActive, enabled = false;
	private int mX, mY;
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		JLayer<JComponent> jlayer = (JLayer<JComponent>) c;
		jlayer.setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		JLayer<JComponent> jlayer = (JLayer<JComponent>) c;
		jlayer.setLayerEventMask(0);
		super.uninstallUI(c);
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		// Paint the view.
		super.paint(g2, c);
		if (!enabled)
			return;
		
		float radius = 1; // no extra light when mouse is not in screen. Value
							// of 0 causes exceptions
		if (mActive) {
			
			radius = 72;
			
		}
		// Create a radial gradient, transparent in the middle.
		java.awt.geom.Point2D center = new java.awt.geom.Point2D.Float(mX, mY);
		float[] dist = { 0.0f, 1.0f };
		Color[] colors = { new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.BLACK };
		RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
		g2.setPaint(p);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
		g2.fillRect(0, 0, c.getWidth(), c.getHeight());
		
		g2.dispose();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void processMouseEvent(MouseEvent e, JLayer l) {
		if (e.getID() == MouseEvent.MOUSE_ENTERED)
			mActive = true;
		if (e.getID() == MouseEvent.MOUSE_EXITED)
			mActive = false;
		l.repaint();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void processMouseMotionEvent(MouseEvent e, JLayer l) {
		Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
		mX = p.x;
		mY = p.y;
		l.repaint();
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
