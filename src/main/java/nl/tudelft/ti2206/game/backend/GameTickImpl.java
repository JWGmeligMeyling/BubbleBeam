package nl.tudelft.ti2206.game.backend;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import nl.tudelft.ti2206.exception.GameOver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GameTick notifies its observers every {@link framePeriod} milliseconds.
 * 
 * @author Sam Smulders
 */
public class GameTickImpl implements GameTick {

	private static final Logger log = LoggerFactory.getLogger(GameTickImpl.class);
	
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> scheduler.shutdownNow()));
	}

	private ArrayList<Tickable> gameTickObservers = new ArrayList<Tickable>();

	private final ScheduledFuture<?> feature;
	
	public GameTickImpl(final int framePeriod) {
		this.feature = scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				gameTickObservers.forEach(listener -> {
					try {
						listener.gameTick();
					} catch (GameOver e) {
						log.error(e.getMessage(), e);
					} catch (Throwable e) {
						log.error(e.getMessage(), e);
					}
				});
			}
		}, 0l, framePeriod, TimeUnit.MILLISECONDS);
	}

	@Override
	public void registerObserver(Tickable observer) {
		gameTickObservers.add(observer);
	}
	
	@Override
	public void removeObserver(Tickable observer) {
		gameTickObservers.remove(observer);
	}
	
	@Override
	public void shutdown() {
		feature.cancel(true);
	}
}
