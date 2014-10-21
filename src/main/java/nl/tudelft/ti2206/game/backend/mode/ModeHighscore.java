package nl.tudelft.ti2206.game.backend.mode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.tudelft.ti2206.highscore.Highscore;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) 
public @interface ModeHighscore {
	
	Class<? extends Highscore> value();
	
}
