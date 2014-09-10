package nl.tudelft.ti2206.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Observable;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.Sprite;
import nl.tudelft.util.Vector2f;

public class Cannon extends Observable implements Sprite{
	private static final Vector2f DEFAULT_VELOCITY = new Vector2f(0f,0f);

	public Point position;
	public double angle = 0;
	public Vector2f velocity = DEFAULT_VELOCITY;
	public Bubble nextBubble, loadedBubble;
	public MovingBubble shotBubble;
	
	public final boolean mouseControl;
	
	public Cannon(Point position) {
		this.position = position;
		mouseControl = true;
		
		fillBubbleSlots();
	}
	
	private void fillBubbleSlots() {
		nextBubble = new ColouredBubble();
		loadedBubble = new ColouredBubble();
	}
	
	public Cannon(Point position, boolean mouseControl) {
		this.position = position;
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
				velocity = new Vector2f(e.getPoint()).subtract(new Vector2f(position)).normalize();
				angle = Math.atan2(position.y - e.getY(), e.getX() - position.x);
				component.repaint();
			}
			
		});
		component.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (shotBubble == null) {
						shootBubble();
					}
				}
			}
			
			private void shootBubble() {
				shotBubble = new MovingBubble(position, loadedBubble, velocity);
				loadedBubble = nextBubble;
				nextBubble = new ColouredBubble();
			}
		});
	}
	
	public void render(final Graphics g) {
		drawCannon(g);
		if (shotBubble != null) {
			drawBullet(g);
		}
	}
	
	private void drawCannon(final Graphics g) {
		g.setColor(Color.blue);
		g.drawRect(position.x - 8, position.y - 8, 16, 16);
		g.setColor(Color.green);
		g.drawLine(position.x, position.y, (int) (position.x + Math.cos(angle) * 50),
				(int) (position.y - Math.sin(angle) * 50));
	}
	
	private void drawBullet(final Graphics g) {
		shotBubble.bubble.render(g);
	}
	
	@Override
	public void setPosition(final Point position) {
		this.position = position;
	}
	
	@Override
	public Point getPosition() {
		return position;
	}
	
	@Override
	public int getX() {
		return position.x;
	}
	
	@Override
	public int getY() {
		return position.y;
	}
	
	public void gameStep() {
		if (shotBubble != null) {
			shotBubble.gameStep();
			this.setChanged();
			this.notifyObservers();
		}
	}
}
