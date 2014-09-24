package nl.tudelft.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class TimeoutFuture<T> implements Future<T> {
	
	private T value;
	private boolean running = true, cancelled = false, done = false;
	
	@Override
	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		if (!cancelled && !done && (mayInterruptIfRunning || !running)) {
			cancelled = true;
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean isCancelled() {
		return cancelled;
	}

	@Override
	public synchronized boolean isDone() {
		return done;
	}

	@Override
	public synchronized T get() throws InterruptedException, ExecutionException {
		this.start(this);
		this.wait();
		return value;
	}
	
	public synchronized void complete(T value) {
		this.value = value;
		this.notifyAll();
	}
	
	public abstract void start(TimeoutFuture<T> future);
	
	@Override
	public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

}
