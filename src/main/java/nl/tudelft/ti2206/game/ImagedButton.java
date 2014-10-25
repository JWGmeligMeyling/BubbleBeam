package nl.tudelft.ti2206.game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import nl.tudelft.ti2206.cannon.Cannon;

public class ImagedButton extends JButton {

	private final int BUTTON_WIDTH = 353/2;
	private final int BUTTON_HEIGHT = 91/2;
	
	public ImagedButton(String url, Action action){
		super(action);
		//setBackground(mainBackgroundColor);
		setIcon(getButtonImage("/exit_button.png"));
		setIconTextGap(0);
		setBorder(null);
		//setMargin(NO_PADDING);
	}
	
	private ImageIcon getButtonImage(String url) {
		try {
			BufferedImage scale = ImageIO.read(Cannon.class.getResourceAsStream(url));
			scale.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_SMOOTH);
			ImageIcon out = new ImageIcon(scale);
			return out;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
