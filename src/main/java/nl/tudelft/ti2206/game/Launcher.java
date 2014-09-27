package nl.tudelft.ti2206.game;

import java.io.IOException;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

	private static final Logger log = LoggerFactory.getLogger(Launcher.class);

	public static void main(String[] args) throws IOException {
		// This main method is called when starting your game.
		log.info("Starting game...");
		JFrame frame = new SinglePlayerFrame();
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
	
}
