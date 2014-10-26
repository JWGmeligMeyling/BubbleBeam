package nl.tudelft.ti2206.logger;

import nl.tudelft.ti2206.logger.Logger;
import nl.tudelft.ti2206.logger.LoggerImpl;

/**
 * The {@code LoggerFactory} is a utility class producing {@link Logger Loggers}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface LoggerFactory {

	/**
	 * @param klasz Class name
	 * @return a {@link Logger} named corresponding to the class passed as parameter
	 */
	static Logger getLogger(Class<?> klasz) {
		return getLogger(klasz.getName());
	}

	/**
	 * @param name Name for this logger
	 * @return a {@link Logger} with a given name passed as parameter
	 */
	static Logger getLogger(String name) {
		return new LoggerImpl(name);
	}
	

}
