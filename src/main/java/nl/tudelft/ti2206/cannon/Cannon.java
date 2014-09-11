package nl.tudelft.ti2206.cannon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.BubblePlaceholder;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.Sprite;
import nl.tudelft.util.AbstractMouseListener;
import nl.tudelft.util.AbstractMouseMotionListener;
import nl.tudelft.util.Vector2f;

/**
 * @author Sam Smulders
 * @author Luka Bavdaz
 */
public class Cannon extends Observable implements Sprite {
	
	protected static final int WIDTH = 48;
	protected static final int HEIGHT = 48;
	protected static final float MIN_ANGLE = (float) (Math.PI/10);
	protected final Point LOADED_BUBBLE_POSITION;
	protected final Point NEXT_BUBBLE_POSITION;
	
	protected Point position;
	protected double angle = 0.0d;
	protected Vector2f direction = new Vector2f(0f, 0f);
	protected ColouredBubble nextBubble, loadedBubble;
	protected MovingBubble shotBubble;
	protected int chances = 5;
	
	protected final boolean mouseControl;
	protected final BubbleMesh bubbleMesh;
	protected final Point screenLocation;
	protected final Dimension screenSize;
	
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
	
	public Cannon(BubbleMesh bubbleMesh, final Point position,
			final Dimension dimension, final Point screenLocation) {
		this.position = position;
		this.bubbleMesh = bubbleMesh;
		this.position = position;
		LOADED_BUBBLE_POSITION = new Point(position.x
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING), position.y
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING));
		NEXT_BUBBLE_POSITION = new Point(position.x + 60
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING), position.y
				- (AbstractBubble.RADIUS + AbstractBubble.SPACING));

		mouseControl = true;

		this.screenSize = dimension;
		this.screenLocation = screenLocation;
		
		fillBubbleSlots();
	}
	
	protected void fillBubbleSlots() {
		nextBubble = new ColouredBubble(bubbleMesh.getRandomRemainingColor());
		loadedBubble = new ColouredBubble(bubbleMesh.getRandomRemainingColor());

		correctBubblePositions();
	}
	
	public void bindMouseListenerTo(final Component component) {
		component.addMouseMotionListener(new AbstractMouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				double newAngle = Math.atan2(position.y - e.getY(), e.getX() - position.x);
				
				if(MIN_ANGLE < newAngle && newAngle < Math.PI - MIN_ANGLE) {
					angle = newAngle;
					direction = new Vector2f(e.getPoint())
							.subtract(new Vector2f(position)).normalize();
				}
			}
			
		});
		
		component.addMouseListener(new AbstractMouseListener() {
		
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (shotBubble == null) {
						shootBubble();
					}
				}
			}

		});
	}
	
	public void shootBubble() {
		Point bubbleStartPosition = new Point((position.x - AbstractBubble.WIDTH / 2)
				+ (int) (WIDTH * direction.x), position.y - AbstractBubble.HEIGHT / 2
				+ (int) (WIDTH * direction.y));
		shotBubble = new MovingBubble(bubbleStartPosition, direction,
				screenSize, screenLocation, loadedBubble.getColor());
		loadedBubble = nextBubble;
		nextBubble = new ColouredBubble(bubbleMesh.getRandomRemainingColor());
		correctBubblePositions();
	}
	
	public void correctBubblePositions() {
		loadedBubble.setPosition(LOADED_BUBBLE_POSITION);
		nextBubble.setPosition(NEXT_BUBBLE_POSITION);
	}
	
	@Override
	public void render(final Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		if (shotBubble != null) {
			drawBullet(graphics);
		}
		drawCannon(graphics);
		drawQueue(graphics);
	}
	
	protected void drawQueue(final Graphics graphics) {
		loadedBubble.render(graphics);
		nextBubble.render(graphics);
	}
	
	protected void drawCannon(final Graphics2D graphics) {
		graphics.rotate(-angle + Math.PI / 2, position.x, position.y);
		graphics.drawImage(getCannonImage(), position.x - WIDTH / 2, position.y
				- HEIGHT / 2 - 32, position.x + WIDTH / 2, position.y + HEIGHT
				/ 2 - 32, 0, 0, 120, 108, null);
		//		^ MAGIC NUMBERS?!
		graphics.rotate(angle - Math.PI / 2, position.x, position.y);
	}

	protected void drawBullet(final Graphics graphics) {
		shotBubble.render(graphics);
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

	public void gameStep(BubbleMesh b) {
		if (shotBubble != null) {
			Bubble hit=null;
			shotBubble.gameStep();
			for(Bubble bubble : b ){
				if(bubble.intersect(shotBubble)){
					hit=bubble;
					BubblePlaceholder[] p = hit.getPlaceHolderNeighbours();
					shotBubble.makeConnections( p[0]);
					shotBubble.calculatePosition();
					shotBubble=null;
					break;
				}
			}
		}
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}
	
}
