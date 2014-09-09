package nl.tudelft.ti2206.throwaway;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Bubble {

	private static File ROOT_FOLDER = new File("src/main/resources");
	
	private Bubble topLeft;
	private Bubble topRight;
	private Bubble left;
	private Bubble right;
	private Bubble bottomLeft;
	private Bubble bottomRight;
	
	public Bubble getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(Bubble topLeft) {
		this.topLeft = topLeft;
		if(topLeft != null) {
			topLeft.bottomRight = this;
		}
	}

	public Bubble getTopRight() {
		return topRight;
	}

	public void setTopRight(Bubble topRight) {
		this.topRight = topRight;
		if(topRight != null) {
			topRight.bottomLeft = this;
		}
	}

	public Bubble getLeft() {
		return left;
	}

	public void setLeft(Bubble left) {
		this.left = left;
		if(left != null) {
			left.right = this;
		}
	}

	public Bubble getRight() {
		return right;
	}

	public void setRight(Bubble right) {
		this.right = right;
		if(right != null) {
			right.left = this;
		}
	}

	public Bubble getBottomLeft() {
		return bottomLeft;
	}

	public void setBottomLeft(Bubble bottomLeft) {
		this.bottomLeft = bottomLeft;
		if(bottomLeft != null) {
			bottomLeft.topRight = this;
		}
	}

	public Bubble getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(Bubble bottomRight) {
		this.bottomRight = bottomRight;
		if(bottomRight != null) {
			bottomRight.topLeft = this;
		}
	}

	public Colour getColor() {
		return color;
	}

	public void setColor(Colour color) {
		this.color = color;
	}

	
	public interface Picture{
		public BufferedImage getImage();
	}
	
	enum Colour implements Picture {
		RED("redbubble.png"){
			public BufferedImage getImage() {
				return super.i;
			}
		},
	 GREEN("greenbubble.png"){
			public BufferedImage getImage() {
				return super.i;
			}
	 }, BLUE("bluebubble.png"){
			public BufferedImage getImage() {
				return super.i;
			}
			
			
	 };
	 private BufferedImage i;
	 
	 private Colour(String filename){
			i=getBubbleImage(new File(ROOT_FOLDER,filename));
		}

		private static BufferedImage getBubbleImage(File file) {
			try {
			   BufferedImage scale=ImageIO.read(file);
			   scale.getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
				return scale;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	};
	
	private static final Random r=new Random();
	private static final Colour[] colours = Colour.values();
	public Bubble(){
		color=colours[r.nextInt(colours.length)];
	}


	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private static final Point ORIGIN = new Point(0,0);

	private Colour color = Colour.RED;
	private Point position = ORIGIN;

	public void setPosition(final Point position) {
		this.position = position;
	}
	
	public Point calculatePosition() {
		if(this.topLeft != null) {
			return new Point(topLeft.position.x + WIDTH / 2, topLeft.position.y + HEIGHT);
		}
		else if(this.topRight != null) {
			return new Point(topRight.position.x - WIDTH / 2, topRight.position.y + HEIGHT);
		}
		else if(this.left != null) {
			return new Point(left.position.x + WIDTH, left.position.y);
		}
		else if(this.right != null) {
			return new Point(right.position.x - WIDTH, right.position.y);			
		}
		return ORIGIN;
	}

	public Point getPosition() {
		return position;
	}

	public void render(final Graphics graphics) {
		final BufferedImage image = color.getImage();
		graphics.drawImage(image, position.x, position.y, WIDTH, HEIGHT, null);
	}
	
}
