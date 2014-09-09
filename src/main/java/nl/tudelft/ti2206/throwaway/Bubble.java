package nl.tudelft.ti2206.throwaway;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bubble {

	private static File ROOT_FOLDER = new File("src/main/resources");
	
	enum Color {
		RED, GREEN, BLUE;

		private static File SHINEY_BUBBLE = new File(ROOT_FOLDER, "bubble.png");
		private static BufferedImage BUBBLE_IMAGE = _getBubbleImage();
		public BufferedImage getBuggleImage() {
			return BUBBLE_IMAGE;
		}

		private static BufferedImage _getBubbleImage() {
			try {
			   BufferedImage scale=ImageIO.read(SHINEY_BUBBLE);
			   scale.getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
				return scale;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	};


	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private static final Point ORIGIN = new Point(200, 50);

	private Color color = Color.RED;
	private Point position = ORIGIN;

	public void setPosition(final Point position) {
		this.position = position;
	}

	public Point getPosition() {
		return position;
	}

	
	public void render(final Graphics graphics) {
		final BufferedImage image = color.getBuggleImage();
		graphics.drawImage(image, position.x, position.y, WIDTH, HEIGHT, null);
	}
	
}
