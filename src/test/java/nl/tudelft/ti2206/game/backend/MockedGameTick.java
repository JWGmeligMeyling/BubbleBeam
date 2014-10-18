package nl.tudelft.ti2206.game.backend;

import java.util.Set;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

public class MockedGameTick implements GameTick {

	private Set<Tickable> observers = Sets.newHashSet();

	@Override
	public void registerObserver(Tickable observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Tickable observer) {
		observers.remove(observer);
	}

	@Override
	public void shutdown() {
		observers.clear();
	}

	@VisibleForTesting
	public void tick() {
		observers.forEach(observer -> {
			observer.gameTick();
		});
	}

}
