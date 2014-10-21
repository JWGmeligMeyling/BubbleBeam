package nl.tudelft.ti2206.game.event;

import java.util.EventListener;
import java.util.EventObject;

import nl.tudelft.ti2206.game.backend.GameController;

public interface GameListener extends EventListener, BubbleMeshListener, CannonListener {
	
	abstract class GameEvent extends EventObject {

		private static final long serialVersionUID = 816160582740508860L;
		
		protected final transient GameController gameController;

		protected GameEvent(GameController gameController) {
			super(gameController);
			this.gameController = gameController;
		}

		@Override
		public GameController getSource() {
			return gameController;
		}
	}
	
	class GameOverEvent extends GameEvent {

		private static final long serialVersionUID = 8044024662518190266L;

		public GameOverEvent(GameController gameController) {
			super(gameController);
		}
		
	}
	
	void gameOver(GameOverEvent event);
	
	class ShotMissedEvent extends GameEvent {

		private static final long serialVersionUID = 5365912303101540291L;

		public ShotMissedEvent(GameController gameController) {
			super(gameController);
		}
		
	}
	
	void shotMissed(ShotMissedEvent event);
	
	@Override default void rowInsert(RowInsertEvent event) { }
	@Override default void pop(BubblePopEvent event) { }
	@Override default void score(ScoreEvent event) { }
	@Override default void shoot(CannonShootEvent event) { };
	
	@FunctionalInterface
	interface GameOverEventListener extends GameListener {
		@Override default void shotMissed(ShotMissedEvent event) { }
	}
	
	@FunctionalInterface
	interface ShotMissedListener extends GameListener {
		@Override default void gameOver(GameOverEvent event) { }
	}
}
