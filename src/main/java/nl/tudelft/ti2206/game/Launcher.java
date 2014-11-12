package nl.tudelft.ti2206.game;

import java.io.IOException;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.cannon.MouseCannonController;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.backend.mode.ClassicGameMode;
import nl.tudelft.ti2206.game.backend.mode.GameMode;

public class Launcher {

	private static final Logger log = LoggerFactory.getLogger(Launcher.class);

	public static void main(String[] args) throws IOException {
		// This main method is called when starting your game.
		log.info("Starting game...");
		
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		Class<? extends GameMode> gameMode = ClassicGameMode.class;
		
		GameModel gameModel = new GameModel(gameMode);
		MouseCannonController masterCannonController = new MouseCannonController();
		GameController gameController = new GameController(gameModel, masterCannonController);
		
		JFrame frame = new SinglePlayerFrame(masterCannonController, gameController);
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
	
}
