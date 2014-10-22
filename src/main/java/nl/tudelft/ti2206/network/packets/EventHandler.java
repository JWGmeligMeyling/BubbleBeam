package nl.tudelft.ti2206.network.packets;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventObject;

/**
 * Marks that a method in a {@link PacketListenerImpl} is a handler for a
 * specific {@link EventObject} type
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) 
public @interface EventHandler {
	
	/**
	 * {@link EventObject} type that this {@code EventHandler} handles
	 * @return type of the {@code EventObject}
	 */
	Class<? extends EventObject> value();
	
}
