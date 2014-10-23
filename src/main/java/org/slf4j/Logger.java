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
	 * 
	 * @param message
	 *            {@code String} to log
	 */
	void debug(String message);
	
	/**
	 * Log a {@link Throwable} at DEBUG level
	 * 
	 * @param message
	 *            {@code String} to log
	 * @param t
	 *            {@code Throwable} to print stacktrace of
	 */
	void debug(String message, Throwable t);
	
	/**
	 * Log a message at DEBUG level
	 * 
	 * @param message
	 *            {@code String} to log
	 * @param objects
	 *            Objects to insert into format string
	 */
	void debug(String message, Object...objects);

	/**
	 * Log a message at INFO level
	 * 
	 * @param message
	 *            {@code String} to log
	 */
	void info(String message);
	
	/**
	 * Log a {@link Throwable} at INFO level
	 * 
	 * @param message
	 *            {@code String} to log
	 * @param t
	 *            {@code Throwable} to print stacktrace of
	 */
	void info(String message, Throwable t);
	
	/**
	 * Log a message at INFO level
	 * 
	 * @param message
	 *            {@code String} to log
	 * @param objects
	 *            Objects to insert into format string
	 */
	void info(String message, Object...objects);

	/**
	 * Log a message at WARN level
	 * 
	 * @param message
	 *            {@code String} to log
	 */
	void warn(String message);
	
	/**
	 * Log a {@link Throwable} at WARN level
	 * 
	 * @param message
	 *            {@code String} to log
	 * @param t
	 *            {@code Throwable} to print stacktrace of
	 */
	void warn(String message, Throwable t);
	
	/**
	 * Log a message at WARN level
	 * 
	 * @param message
	 *            {@code String} to log
	 * @param objects
	 *            Objects to insert into format string
	 */
	void warn(String message, Object...objects);

	/**
	 * Log a message at ERROR level
	 * 
	 * @param message
	 *            {@code String} to log
	 */
	void error(String message);
	
	/**
	 * Log a {@link Throwable} at ERROR level
	 * 
	 * @param message
	 *            {@code String} to log
	 * @param t
	 *            {@code Throwable} to print stacktrace of
	 */
	void error(String message, Throwable t);
	
	/**
	 * Log a message at ERROR level
	 * 
	 * @param message
	 *            {@code String} to log
	 * @param objects
	 *            Objects to insert into format string
	 */
	void error(String message, Object...objects);
	
}
