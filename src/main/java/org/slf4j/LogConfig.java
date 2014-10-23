package org.slf4j;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Global configuration for the {@link Logger} instances
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public final class LogConfig {
	
	/**
	 * Bind the {@code LogAppender}s here
	 */
	public static final List<LogAppender> LOG_APPENDERS = Lists
			.newArrayList(ConsoleAppender.getInstance());
	
	/**
	 * Add a {@code LogAppender} to the configuration
	 * @param appender {@code LogAppender} to use
	 */
	public static void addLogAppender(LogAppender appender) {
		LOG_APPENDERS.add(appender);
	}
	
}
