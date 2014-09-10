package nl.tudelft.ti2206.cannon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.Sprite;
import nl.tudelft.util.Vector2f;

/**
 * @author Sam Smulders
 * @author Luka Bavdaz
 */
public class Cannon extends Observable implements Sprite {
	private static final Vector2f DEFAULT_VELOCITY = new Vector2f(0f, 0f);
	
	private static final int WIDTH = 48;
	private static final int HEIGHT = 48;
	
	public Point position;
	public double angle = 0;
	public Vector2f velocity = DEFAULT_VELOCITY;
	public Bubble nextBubble, loadedBubble;
	public MovingBubble shotBubble;
	
	private static File ROOT_FOLDER = new File("src/main/resources");
	
	private static File CANNON = new File(ROOT_FOLDER, "cannon.png");
	private static BufferedImage CANNON_IMAGE = _getCannonImage();
	
	public BufferedImage getCannonImage() {
		return CANNON_IMAGE;
	}
	
	private static BufferedImage _getCannonImage() {
		try {
			BufferedImage scale = ImageIO.read(CANNON);
			scale.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
			return scale;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public final boolean mouseControl;
	
	protected Dimension screenSize;
	protected Point screenLocation;
	
	public Cannon(Point position, Dimension dimension, Point screenLocation) {
		this.position = position;
		mouseControl = true;
		
		this.screenSize = dimension;
		this.screenLocation = screenLocation;
		
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
				Point bubbleStartPosition = new Point((position.x - AbstractBubble.WIDTH / 2)
						+ (int) (48 * velocity.x), position.y - AbstractBubble.HEIGHT / 2
						+ (int) (48 * velocity.y));
				shotBubble = new MovingBubble(bubbleStartPosition, loadedBubble, velocity,
						screenSize, screenLocation);
				loadedBubble = nextBubble;
				nextBubble = new ColouredBubble();
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
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
		});
	}
	
	public void render(final Graphics g) {
		if (shotBubble != null) {
			drawBullet(g);
		}
		drawCannon(g);
	}
	
	private void drawCannon(final Graphics g) {
		((Graphics2D) g).rotate(-angle + Math.PI / 2, position.x, position.y);
		g.drawImage(getCannonImage(), position.x - WIDTH / 2, position.y - HEIGHT / 2 - 32,
				position.x + WIDTH / 2, position.y + HEIGHT / 2 - 32, 0, 0, 120, 108, null);
		((Graphics2D) g).rotate(angle - Math.PI / 2, position.x, position.y);
		/*
		 * g.setColor(Color.blue); g.drawRect(position.x - 8, position.y - 8,
		 * 16, 16); g.setColor(Color.green); g.drawLine(position.x, position.y,
		 * (int) (position.x + Math.cos(angle) * 50), (int) (position.y -
		 * Math.sin(angle) * 50));
		 */
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
