package org.slf4j;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * The console appender adds {@link LogLine LogLines} to the System streams
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class ConsoleAppender implements LogAppender {
	
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d MMM HH:mm:ss.SSS");
	
	private volatile static ConsoleAppender instance;
	
	private static final Map<LogPriority, PrintStream> DEFAULT_BINDINGS = ImmutableMap.of(
			LogPriority.DEBUG, System.out, LogPriority.INFO, System.out, LogPriority.WARN,
			System.err, LogPriority.ERROR, System.err);
	
	private Map<LogPriority, PrintStream> bindings = DEFAULT_BINDINGS;
	
	public static ConsoleAppender getInstance() {
		if (instance == null) {
			synchronized (ConsoleAppender.class) {
				instance = new ConsoleAppender();
			}
		}
		return instance;
	}
	
	private ConsoleAppender() {
	}
	
	@Override
	public void append(LogPriority priority, Throwable throwable) {
		PrintStream writer = bindings.get(priority);
		if (writer == null)
			return;
		throwable.printStackTrace(writer);
	}
	
	@Override
	public void append(LogLine line) {
		PrintStream writer = bindings.get(line.getLogPriority());
		if (writer == null)
			return;
		writer.append(DATE_FORMAT.format(new Date(line.getMillis()))).append(' ');
		writer.append('[').append(line.getThread()).append(']').append(' ');
		writer.append(line.getLogPriority().toString()).append(' ');
		writer.append(line.getLoggerName()).append(' ');
		writer.append('(').append(Integer.toString(line.getLineNumber())).append(')').append(' ');
		writer.append(" - ").append(line.getMessage());
		writer.println();
	}
	
	public Map<LogPriority, PrintStream> getBindings() {
		return bindings;
	}
	
	public void setBindings(Map<LogPriority, PrintStream> bindings) {
		Preconditions.checkNotNull(bindings);
		this.bindings = bindings;
	}
	
}
