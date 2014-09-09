package nl.tudelft.ti2206.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.Sprite;

public class Cannon implements Sprite {
	public int x, y;
	public double angle = 0;
	public Bubble nextBubble, loadedBubble;
	public final boolean mouseControl;
	
	public Cannon(int x, int y) {
		this.x = x;
		this.y = y;
		mouseControl = true;
	}
	
	public Cannon(int x, int y, boolean mouseControl) {
		this.x = x;
		this.y = y;
		this.mouseControl = mouseControl;
	}
	
	public void bindMouseListenerTo(final Component component) {
		component.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				angle = Math.atan2(y - e.getY(), e.getX() - x);
				component.repaint();
			}
			
		});
	}
	
	public void render(final Graphics g) {
		g.setColor(Color.blue);
		g.drawRect(x - 8, y - 8, 16, 16);
		g.setColor(Color.green);
		g.drawLine(x, y, (int) (x + Math.cos(angle) * 50), (int) (y - Math.sin(angle) * 50));
	}

	@Override
	public void setPosition(final Point position) {
		this.x = position.x;
		this.y = position.y;
	}

	@Override
	public Point getPosition() {
		return new Point(x,y);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
}
