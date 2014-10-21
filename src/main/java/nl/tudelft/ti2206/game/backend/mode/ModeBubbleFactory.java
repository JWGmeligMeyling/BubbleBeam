package nl.tudelft.ti2206.game.backend.mode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.tudelft.ti2206.bubbles.factory.BubbleFactory;

/**
 * Annotation that binds a {@link BubbleFactory} type to a {@link GameMode}.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) 
public @interface ModeBubbleFactory {
	
	/**
	 * Get the {@link BubbleFactory} for the {@code GameMode}
	 * @return a type of {@code BubbleFactory}
	 */
	Class<? extends BubbleFactory> value();
	
}
