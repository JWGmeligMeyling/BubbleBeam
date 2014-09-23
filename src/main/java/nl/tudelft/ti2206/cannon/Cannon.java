package nl.tudelft.ti2206.cannon;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.bubbles.Sprite;
import nl.tudelft.util.Vector2f;

/**
 * The {@code Cannon} is responsible for drawing the cannon. It can listen to a
 * {@link CannonController} to execute the {@link CannonController}'s behaviour.
 * 
 * @author Sam Smulders
 */
public class Cannon implements Sprite, CannonControllerObserver {
	
	protected static final int WIDTH = 48;
	protected static final int HEIGHT = 48;
	protected static final float MIN_ANGLE = (float) (Math.PI / 10);
	protected static final float MIN_DIRECTION_Y = (float) Math.sin(Cannon.MIN_ANGLE);
	protected static final float MIN_DIRECTION_X = (float) Math.cos(Cannon.MIN_ANGLE);
	protected static final int ROTATE_TRANSLATION = 32;
	
	/*
	 * CANNON_OUTPUT is the distance from the center of the cannon, where the
	 * moving bubble should be created when there is shot.
	 */
	public static final float CANNON_OUTPUT = 60;
	
	protected Point position;
	protected double angle = Math.PI / 2;
	protected Vector2f direction = new Vector2f(0f, 0f);
	
	protected final Dimension screenSize;
	
	protected static BufferedImage CANNON_IMAGE = _getCannonImage();
	
	public Cannon(final Point position, final Dimension dimension) {
		this.position = position;
		this.position = position;
		
		this.screenSize = dimension;
	}
	
	protected static BufferedImage _getCannonImage() {
		try {
			BufferedImage scale = ImageIO.read(Cannon.class.getResourceAsStream("/cannon.png"));
			scale.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
			return scale;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void render(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		drawCannon(graphics);
	}
	
	protected void drawCannon(final Graphics2D graphics) {
		graphics.rotate(-angle + Math.PI / 2, position.x, position.y);
		graphics.drawImage(CANNON_IMAGE, position.x - WIDTH / 2, position.y - HEIGHT / 2
				- ROTATE_TRANSLATION, position.x + WIDTH / 2, position.y + HEIGHT / 2
				- ROTATE_TRANSLATION, 0, 0, CANNON_IMAGE.getWidth(), CANNON_IMAGE.getHeight(), null);
		graphics.rotate(angle - Math.PI / 2, position.x, position.y);
	}
	
	@Override
	public void cannonRotate(double direction) {
		this.angle = direction;
	}
	
	@Override
	public void cannonShoot(Vector2f direction) {
	}

	@Override
	public void setPosition(Point position) {
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

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}
	
}
