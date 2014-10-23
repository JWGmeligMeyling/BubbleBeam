package nl.tudelft.ti2206.network;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.tudelft.ti2206.game.backend.SlaveGamePacketListener;

/**
 * Marks that a method in a {@link SlaveGamePacketListener} is a handler for a
 * specific {@link Packet} type
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) 
public @interface PacketHandler {
	
	/**
	 * Type of the {@link Packet} that this handler handles
	 * 
	 * @return the Type of the {@link Packet} that this handler handles
	 */
	Class<? extends Packet> value() default Packet.class;
	
}
