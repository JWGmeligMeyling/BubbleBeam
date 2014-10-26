package nl.tudelft.ti2206.logger;

/**
 * A log appender acts as a listener for log events
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface LogAppender {

	/**
	 * Append a {@link LogLine} to the log
	 * @param line {@code LogLine} to be logged
	 */
	void append(LogLine line);
	
	/**
	 * Append a {@link Throwable} to the log
	 * @param priority {@link LogPriority} for this event
	 * @param throwable {@link Throwable} to be logged
	 */
	void append(LogPriority priority, Throwable throwable);
	
}
