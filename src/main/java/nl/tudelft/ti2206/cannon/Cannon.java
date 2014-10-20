 package nl.tudelft.ti2206.cannon;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.tudelft.ti2206.graphics.Sprite;
import nl.tudelft.ti2206.util.mvc.View;

/**
 * The {@code Cannon} is responsible for drawing the cannon. It can listen to a
 * {@link CannonController} to execute the {@link CannonController}'s behaviour.
 * 
 * @author Sam Smulders
 */
public class Cannon implements Sprite, View<CannonController, CannonModel> {
	
	protected static final int WIDTH = 48;
	protected static final int HEIGHT = 48;
	protected static final int ROTATE_TRANSLATION = 32;

	protected Point position;
	protected final CannonController controller;
	protected static BufferedImage CANNON_IMAGE = _getCannonImage();
	
	public Cannon(final CannonController controller, final Point position) {
		this.controller = controller;
		this.position = position;
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
		double angle = getModel().getAngle();
		graphics.rotate(-angle + Math.PI / 2, position.x, position.y);
		graphics.drawImage(CANNON_IMAGE, position.x - WIDTH / 2, position.y - HEIGHT / 2
				- ROTATE_TRANSLATION, position.x + WIDTH / 2, position.y + HEIGHT / 2
				- ROTATE_TRANSLATION, 0, 0, CANNON_IMAGE.getWidth(), CANNON_IMAGE.getHeight(), null);
		graphics.rotate(angle - Math.PI / 2, position.x, position.y);
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

	@Override
	public CannonController getController() {
		return controller;
	}

	@Override
	public void translate(int dx, int dy) {
		position.translate(dx, dy);
	}

}
