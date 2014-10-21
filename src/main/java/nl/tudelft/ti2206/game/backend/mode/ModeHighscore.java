package nl.tudelft.ti2206.game.backend.mode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.tudelft.ti2206.highscore.Highscore;

/**
 * Annotation that binds a {@link Highscore} type to a {@link GameMode}.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) 
public @interface ModeHighscore {
	
	/**
	 * Get the {@link Highscore} for the {@code GameMode}
	 * @return a type of {@code Highscore}
	 */
	Class<? extends Highscore> value();
	
}
