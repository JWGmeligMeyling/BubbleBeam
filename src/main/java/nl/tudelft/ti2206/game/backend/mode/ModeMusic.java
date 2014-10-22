package nl.tudelft.ti2206.game.backend.mode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that binds a music URL to a {@link GameMode}.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) 
public @interface ModeMusic {
	
	/**
	 * Get the URL for the music
	 * @return the URL for the music
	 */
	String value();
}
