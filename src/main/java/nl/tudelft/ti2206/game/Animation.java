package nl.tudelft.ti2206.game;

import nl.tudelft.ti2206.game.tick.GameTick;
import nl.tudelft.ti2206.game.tick.Tickable;

public abstract class Animation<T> implements Tickable {
	
	private final T object;
	private final GameTick tick;
	
	public Animation(GameTick tick, T object) {
		this.object = object;
		this.tick = tick;
		tick.registerObserver(this);
	}

	@Override
	public void gameTick() {
		if(isComplete(object)) {
			finish();
		}
		else {
			animate(object);
		}
	}
	
	protected abstract void animate(T object);

	protected abstract boolean isComplete(T object);
	
	public void finish() {
		tick.removeObserver(this);
	}

}
