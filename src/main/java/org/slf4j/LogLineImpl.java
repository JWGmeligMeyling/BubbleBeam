package org.slf4j;

/**
 * The {@code LogLineImpl} is the default implementation for {@link LogLine} and
 * acts as a wrapper for a line to be printed to the logger.
 * 
 * @author Jan-Willem Gmelig Meyling
 */
public class LogLineImpl implements LogLine {

	private final LogPriority priority;
	private final String loggerName;
	private final String message;
	
	/**
	 * Construct a new {@link LogLine}
	 * 
	 * @param priority
	 *            priority for the {@code LogLine}
	 * @param loggerName
	 *            name for the current {@link Logger} instance
	 * @param message
	 *            message to be logged
	 */
	public LogLineImpl(LogPriority priority, String loggerName, String message) {
		this.priority = priority;
		this.loggerName = loggerName;
		this.message = message;
	}

	@Override
	public long getMillis() {
		return System.currentTimeMillis();
	}

	@Override
	public String getThread() {
		return Thread.currentThread().getName();
	}

	@Override
	public LogPriority getLogPriority() {
		return priority;
	}

	@Override
	public String getLoggerName() {
		return loggerName;
	}

	@Override
	public int getLineNumber() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		StackTraceElement element = stackTraceElements[8];
		return element.getLineNumber();
	}

	@Override
	public String getMessage() {
		return message;
	}

}
