package nl.tudelft.ti2206.game;


import nl.tudelft.ti2206.throwaway.GuiThrowAway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

	private static final Logger log = LoggerFactory.getLogger(Launcher.class);
	
	public static void main(String[] args) {
		// This main method is called when starting your game.
		log.info("Starting game...");
		new GuiThrowAway();
}
}