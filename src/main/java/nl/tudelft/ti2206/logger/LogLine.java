package nl.tudelft.ti2206.logger;

/**
 * A {@code LogLine} acts as a wrapper for a line to be printed to the logger.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface LogLine {
	
	/**
	 * @return a timestamp for when the event was logged
	 */
	long getMillis();
	
	/**
	 * @return name of the current {@link Thread}
	 */
	String getThread();
	
	/**
	 * @return the {@link LogPriority} for this {@code LogLine}
	 */
	LogPriority getLogPriority();
	
	/**
	 * @return the name of the current {@link Logger} instance
	 */
	String getLoggerName();
	
	/**
	 * @return the number at which this log event was triggered
	 */
	int getLineNumber();
	
	/**
	 * @return the log message
	 */
	String getMessage();

}
