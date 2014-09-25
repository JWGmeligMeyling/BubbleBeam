package nl.tudelft.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class ChainedFuture<T, A extends Future<B>, B> implements Future<T> {

	private final Future<B> future;
	
	public ChainedFuture(final Future<B> future) {
		this.future = future;
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return future.isDone();
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		return get(future.get());
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return get(future.get(timeout, unit));
	}
	
	public abstract T get(B arg) throws InterruptedException, ExecutionException;

}
