package org.slf4j;

import static org.slf4j.LogConfig.*;

import java.util.regex.Matcher;

/**
 * Default implementation for {@link Logger}
 * 
 * @author Jan-Willem Gmelig Meyling
 */
public class LoggerImpl implements Logger {

	private final String name;
	
	/**
	 * Construct a new {@code LoggerImpl}
	 * @param name
	 */
	public LoggerImpl(String name) {
		super();
		this.name = name;
	}

	@Override
	public void debug(String message) {
		write(LogPriority.DEBUG, message);
	}

	@Override
	public void debug(String message, Throwable t) {
		write(LogPriority.DEBUG, message);
		write(LogPriority.DEBUG, t);
	}

	@Override
	public void debug(String message, Object... objects) {
		write(LogPriority.DEBUG, replaceCurlyBraces(message, objects));
	}

	@Override
	public void info(String message) {
		write(LogPriority.INFO, message);
	}

	@Override
	public void info(String message, Throwable t) {
		write(LogPriority.INFO, message);
		write(LogPriority.INFO, t);
	}

	@Override
	public void info(String message, Object... objects) {
		write(LogPriority.INFO, replaceCurlyBraces(message, objects));
	}

	@Override
	public void warn(String message) {
		write(LogPriority.WARN, message);
	}

	@Override
	public void warn(String message, Throwable t) {
		write(LogPriority.WARN, message);
		write(LogPriority.WARN, t);
	}

	@Override
	public void warn(String message, Object... objects) {
		write(LogPriority.WARN, replaceCurlyBraces(message, objects));
	}

	@Override
	public void error(String message) {
		write(LogPriority.ERROR, message);
	}

	@Override
	public void error(String message, Throwable t) {
		write(LogPriority.ERROR, message);
		write(LogPriority.ERROR, t);

	}

	@Override
	public void error(String message, Object... objects) {
		write(LogPriority.ERROR, replaceCurlyBraces(message, objects));
	}

	protected String replaceCurlyBraces(String message, Object... objects) {
		final String braces = "\\{\\}";
		for(Object object : objects) {
			message = message.replaceFirst(braces, Matcher.quoteReplacement(object.toString()));
		}
		return message;
	}
	
	protected void write(LogPriority priority, String message) {
		final LogLine logLine = new LogLineImpl(priority, name, message);
		LOG_APPENDERS.forEach(appender -> {
			appender.append(logLine);
		});
	}
	
	protected void write(final LogPriority priority, final Throwable t) {
		LOG_APPENDERS.forEach(appender -> {
			appender.append(priority, t);
		});
	}
	
}
