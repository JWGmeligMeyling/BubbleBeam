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
	
	protected static final int WIDTH = 48;
	protected static final int HEIGHT = 48;
<<<<<<< HEAD
	protected static final float MIN_ANGLE = (float) (Math.PI/10);
=======
	protected static final int Y_TRANSLATION = 32;
>>>>>>> d1997eb0ec252d6285cdcdf44741f3a749cf184a
	protected final Point LOADED_BUBBLE_POSITION;
	protected final Point NEXT_BUBBLE_POSITION;
	
	protected Point position;
	protected double angle = 0;
	protected Vector2f direction = new Vector2f(0f, 0f);
	protected Bubble nextBubble, loadedBubble;
	protected MovingBubble shotBubble;
<<<<<<< HEAD
	protected int chances = 5;
	
=======

>>>>>>> d1997eb0ec252d6285cdcdf44741f3a749cf184a
	protected final boolean mouseControl;
	protected Dimension screenSize;
	protected Point screenLocation;
	
	protected static File ROOT_FOLDER = new File("src/main/resources");
	
	protected static File CANNON = new File(ROOT_FOLDER, "cannon.png");
	protected static BufferedImage CANNON_IMAGE = _getCannonImage();
	
	public BufferedImage getCannonImage() {
		return CANNON_IMAGE;
	}
	
	protected static BufferedImage _getCannonImage() {
		try {
			BufferedImage scale = ImageIO.read(CANNON);
			scale.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
			return scale;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Cannon(Point position, Dimension dimension, Point screenLocation) {
		this.position = position;
		LOADED_BUBBLE_POSITION = new Point(position.x
				- (ColouredBubble.RADIUS + ColouredBubble.SPACING), position.y
				- (ColouredBubble.RADIUS + ColouredBubble.SPACING));
		NEXT_BUBBLE_POSITION = new Point(position.x + 60
				- (ColouredBubble.RADIUS + ColouredBubble.SPACING), position.y
				- (ColouredBubble.RADIUS + ColouredBubble.SPACING));
		
		mouseControl = true;
		
		this.screenSize = dimension;
		this.screenLocation = screenLocation;
		
		fillBubbleSlots();
	}
	
	protected void fillBubbleSlots() {
		nextBubble = new ColouredBubble();
		loadedBubble = new ColouredBubble();
		
		correctBubblePositions();
	}
	
	public void bindMouseListenerTo(final Component component) {
		component.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				angle = Math.atan2(position.y - e.getY(), e.getX() - position.x);
				if(angle < -Math.PI/2){
					angle = Math.PI - MIN_ANGLE;
				} else if(angle < MIN_ANGLE) {
					angle = MIN_ANGLE;
				} else if(angle > Math.PI - MIN_ANGLE){
					angle = Math.PI - MIN_ANGLE;
				}
				direction = new Vector2f((float) Math.cos(angle), (float) -Math.sin(angle));
				
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
			
			public void shootBubble() {
<<<<<<< HEAD
				Point bubbleStartPosition = new Point((position.x - AbstractBubble.WIDTH / 2)
						+ (int) (48 * direction.x), position.y - AbstractBubble.HEIGHT / 2
						+ (int) (48 * direction.y));
				shotBubble = new MovingBubble(bubbleStartPosition, loadedBubble, direction,
						screenSize, screenLocation);
=======
				Point bubbleStartPosition = new Point(
						(position.x - AbstractBubble.WIDTH / 2)
								+ (int) (WIDTH * direction.x), position.y
								- AbstractBubble.HEIGHT / 2
								+ (int) (HEIGHT * direction.y));
				shotBubble = new MovingBubble(bubbleStartPosition,
						loadedBubble, direction, screenSize, screenLocation);
>>>>>>> d1997eb0ec252d6285cdcdf44741f3a749cf184a
				loadedBubble = nextBubble;
				nextBubble = new ColouredBubble();
				correctBubblePositions();
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
	
	public void correctBubblePositions() {
		loadedBubble.setPosition(LOADED_BUBBLE_POSITION);
		nextBubble.setPosition(NEXT_BUBBLE_POSITION);
	}
	
	public void render(final Graphics g) {
		if (shotBubble != null) {
			drawBullet(g);
		}
		drawCannon(g);
		drawQueue(g);
	}
	
	protected void drawQueue(final Graphics g) {
		loadedBubble.render(g);
		nextBubble.render(g);
	}
	
	protected void drawCannon(final Graphics g) {
		((Graphics2D) g).rotate(-angle + Math.PI / 2, position.x, position.y);
<<<<<<< HEAD
		g.drawImage(getCannonImage(), position.x - WIDTH / 2, position.y - HEIGHT / 2 - 32,
				position.x + WIDTH / 2, position.y + HEIGHT / 2 - 32, 0, 0, 120, 108, null);
=======
		g.drawImage(getCannonImage(), position.x - WIDTH / 2, position.y
				- HEIGHT / 2 - Y_TRANSLATION, position.x + WIDTH / 2, position.y + HEIGHT
				/ 2 - Y_TRANSLATION, 0, 0, getCannonImage().getWidth(), getCannonImage().getHeight(), null);
>>>>>>> d1997eb0ec252d6285cdcdf44741f3a749cf184a
		((Graphics2D) g).rotate(angle - Math.PI / 2, position.x, position.y);
	}
	
	protected void drawBullet(final Graphics g) {
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
