package nl.tudelft.ti2206.game;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.MouseCannonController;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.backend.mode.GameMode;
import nl.tudelft.ti2206.game.backend.mode.PowerupGameMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

	private static final Logger log = LoggerFactory.getLogger(Launcher.class);

	public static void main(String[] args) throws IOException {
		// This main method is called when starting your game.
		log.info("Starting game...");
		
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		Class<? extends GameMode> gameMode = PowerupGameMode.class;
		
		String path = GameMode.getMapsFor(gameMode)[0];
		InputStream inputstream = Launcher.class.getResourceAsStream(path);
		BubbleMesh bubbleMesh = BubbleMesh.parse(inputstream);

		GameModel gameModel = new GameModel(gameMode, bubbleMesh);
		gameModel.setMapPath(path);
		
		MouseCannonController masterCannonController = new MouseCannonController();
		GameController gameController = new GameController(gameModel, masterCannonController);
		
		JFrame frame = new SinglePlayerFrame(masterCannonController, gameController);
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
	
}
