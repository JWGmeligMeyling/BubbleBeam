package org.slf4j;

/**
 * Logger interface
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface Logger {
	
	/**
	 * Log a message at DEBUG level
	 * @param message
	 */
	void debug(String message);
	
	/**
	 * Log a {@link Throwable} at DEBUG level
	 * @param message
	 * @param t
	 */
	void debug(String message, Throwable t);
	
	/**
	 * Log a message at DEBUG level
	 * @param message
	 * @param objects
	 */
	void debug(String message, Object...objects);

	/**
	 * Log a message at INFO level
	 * @param message
	 */
	void info(String message);
	
	/**
	 * Log a {@link Throwable} at INFO level
	 * @param message
	 * @param t
	 */
	void info(String message, Throwable t);
	
	/**
	 * Log a message at INFO level
	 * @param message
	 * @param objects
	 */
	void info(String message, Object...objects);

	/**
	 * Log a message at WARN level
	 * @param message
	 */
	void warn(String message);
	
	/**
	 * Log a {@link Throwable} at WARN level
	 * @param message
	 * @param t
	 */
	void warn(String message, Throwable t);
	
	/**
	 * Log a message at WARN level
	 * @param message
	 * @param objects
	 */
	void warn(String message, Object...objects);

	/**
	 * Log a message at ERROR level
	 * @param message
	 */
	void error(String message);
	
	/**
	 * Log a {@link Throwable} at ERROR level
	 * @param message
	 * @param t
	 */
	void error(String message, Throwable t);
	
	/**
	 * Log a message at ERROR level
	 * @param message
	 * @param objects
	 */
	void error(String message, Object...objects);
	
}
