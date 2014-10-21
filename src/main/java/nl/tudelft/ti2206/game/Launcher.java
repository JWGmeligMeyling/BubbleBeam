package nl.tudelft.ti2206.game;

import java.io.IOException;

import javax.swing.JFrame;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.backend.mode.GameMode;
import nl.tudelft.ti2206.game.backend.mode.PowerupGameMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

	private static final Logger log = LoggerFactory.getLogger(Launcher.class);
	protected static final String DEFAULT_BOARD_PATH = "/board.txt";

	public static void main(String[] args) throws IOException {
		// This main method is called when starting your game.
		log.info("Starting game...");
		
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		Class<? extends GameMode> gameMode = PowerupGameMode.class;
		BubbleMesh bubbleMesh = BubbleMesh.parse(SinglePlayerFrame.class.getResourceAsStream(DEFAULT_BOARD_PATH));
		GameModel gameModel = new GameModel(gameMode, bubbleMesh);
		
		JFrame frame = new SinglePlayerFrame(gameModel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
	
}
