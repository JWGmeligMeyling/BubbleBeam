package nl.tudelft.ti2206.game.backend.mode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that binds maps to a {@link GameMode}.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) 
public @interface ModeMaps {

	/**
	 * @return the paths to the maps
	 */
	String[] value();
	
}
