package nl.tudelft.ti2206.game.backend.mode;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Annotation that names a {@link GameMode}.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) 
public @interface ModeName {
	
	/**
	 * Get the name for this {@code GameMode}
	 * @return the name
	 */
	String value();
	
}
