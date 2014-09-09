package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class ColouredBubble extends AbstractBubble {

	private static File ROOT_FOLDER = new File("src/main/resources");
	
	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private Colour color = Colour.pickRandom();

	@Override
	public void render(final Graphics graphics) {
		final BufferedImage image = color.getImage();
		graphics.drawImage(image, position.x, position.y, WIDTH, HEIGHT, null);
	}
	
	public Colour getColor() {
		return color;
	}

	public void setColor(Colour color) {
		this.color = color;
	}

	public enum Colour {
		
		RED("redbubble.png"),
		GREEN("greenbubble.png"),
		BLUE("bluebubble.png");

		private final BufferedImage image;

		private Colour(String filename) {
			this.image = getBubbleImage(new File(ROOT_FOLDER, filename));
		}

		public BufferedImage getImage() {
			return image;
		}

		private static BufferedImage getBubbleImage(File file) {
			try {
				BufferedImage scale = ImageIO.read(file);
				scale.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
				return scale;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private static final Random RANDOM_GENERATOR = new Random();
		
		public static Colour pickRandom() {
			Colour[] values = Colour.values();
			int index = RANDOM_GENERATOR.nextInt(values.length);
			return values[index];
		}
		
	}
}
