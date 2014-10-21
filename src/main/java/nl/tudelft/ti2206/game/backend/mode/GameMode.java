package nl.tudelft.ti2206.game.backend.mode;

import java.io.Serializable;

import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;
import nl.tudelft.ti2206.game.backend.Tickable;
import nl.tudelft.ti2206.highscore.Highscore;

public interface GameMode extends Tickable, Serializable {
	
	default void missed() {};
	
	@Override default void gameTick() {}
	
	default String getModeName() {
		return this.getClass().getAnnotation(ModeName.class).value();
	}
	
	default Highscore getHighscore() {
		try {
			return this.getClass().getAnnotation(ModeHighscore.class).value()
					.newInstance();
		}
		catch (InstantiationException  | IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	default BubbleFactory getBubbleFactory() {
		try {
			return this.getClass().getAnnotation(ModeBubbleFactory.class)
					.value().newInstance();
		}
		catch (InstantiationException  | IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}
