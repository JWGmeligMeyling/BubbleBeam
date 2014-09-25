package org.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerImpl;

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
	 * @param name
	 * @return a {@link Logger} with a given name passed as parameter
	 */
	static Logger getLogger(String name) {
		return new LoggerImpl(name);
	}
	

}
