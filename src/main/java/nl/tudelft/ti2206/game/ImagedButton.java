package nl.tudelft.ti2206.game;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImagedButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4429892434267508033L;
	private final int BUTTON_WIDTH = 353/2;
	private final int BUTTON_HEIGHT = 91/2;
	
	protected static final Insets NO_PADDING = new Insets(0, 0, 0, 0);
	
	public ImagedButton(String url, Action action, Color background){
		super(action);
		setBackground(background);
		setIcon(getButtonImage(url));
		setIconTextGap(0);
		setBorder(null);
		//this.setMaximumSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
		setMargin(NO_PADDING);
	}
	
	private ImageIcon getButtonImage(String url) {
		try {
			BufferedImage scale = ImageIO.read(ImagedButton.class.getResourceAsStream(url));
			Image test = scale.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_SMOOTH);
			ImageIcon out = new ImageIcon(test);
			return out;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
