package nl.tudelft.ti2206.game;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.tudelft.ti2206.exception.GameOver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GameTick notifies its observers every {@link framePeriod} milliseconds.
 * 
 * @author Sam Smulders
 */
public class GameTick {

	private static final Logger log = LoggerFactory.getLogger(GameTick.class);
	
	private ArrayList<Tickable> gameTickObservers = new ArrayList<Tickable>();
	
	public final void registerObserver(Tickable observer) {
		gameTickObservers.add(observer);
	}
	
	public final void removeObserver(Tickable observer) {
		gameTickObservers.remove(observer);
	}
	
	private final int framePeriod;
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> scheduler.shutdownNow()));
	}
	
	public GameTick(int framePeriod) {
		this.framePeriod = framePeriod;
	}
	
	public void start() {
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				gameTickObservers.forEach(listener -> {
					try {
						listener.gameTick();
					} catch (GameOver e) {
						log.error(e.getMessage(), e);
					}
				});
			}
		}, 0l, framePeriod, TimeUnit.MILLISECONDS);
	}
}
