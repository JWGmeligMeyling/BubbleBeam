package nl.tudelft.ti2206.game.backend;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import nl.tudelft.ti2206.logger.Logger;
import nl.tudelft.ti2206.logger.LoggerFactory;

/**
 * GameTick notifies its observers every framePeriod milliseconds
 * 
 * @author Sam Smulders
 * @author Jan-Willem Gmelig Meyling
 * @author Liam Clark
 */
public class GameTickImpl implements GameTick {

	private static final Logger log = LoggerFactory.getLogger(GameTickImpl.class);
	
	private final ScheduledExecutorService scheduler;
	
	private ArrayList<Tickable> gameTickObservers = new ArrayList<Tickable>();

	private final ScheduledFuture<?> feature;
	
	private boolean shutdownExecutor = false;
	
	/**
	 * Construct a new {@link GameTickImpl}
	 * 
	 * @param framePeriod
	 *            FramePeriod to use
	 */
	public GameTickImpl(final long framePeriod) {
		this(framePeriod, Executors.newScheduledThreadPool(2));
		shutdownExecutor = true;
	}
	
	/**
	 * Construct a new {@link GameTickImpl}
	 * 
	 * @param framePeriod
	 *            frame period to use
	 * @param scheduler
	 *            {@link ScheduledExecutorService} to use
	 */
	public GameTickImpl(final long framePeriod, final ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
		this.feature = scheduler.scheduleAtFixedRate(() -> {
			synchronized(gameTickObservers) {
				gameTickObservers.forEach(listener -> {
					try {
						listener.gameTick();
					}
					catch (Throwable e) {
						log.error(e.getMessage(), e);
					}
				});
			}
		}, 0l, framePeriod, TimeUnit.MILLISECONDS);
		
	}
	

	@Override
	public void registerObserver(Tickable observer) {
		synchronized(gameTickObservers) {
			gameTickObservers.add(observer);
		}
	}
	
	@Override
	public void removeObserver(Tickable observer) {
		synchronized(gameTickObservers) {
			gameTickObservers.remove(observer);
		}
	}
	
	@Override
	public void shutdown() {
		feature.cancel(true);
		if(shutdownExecutor)
			scheduler.shutdownNow();
	}
	
}
