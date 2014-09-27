package nl.tudelft.ti2206.game.backend;

public interface GameTick {
	
	void registerObserver(Tickable observer);

	void removeObserver(Tickable observer);
	
	void shutdown();
	
}
