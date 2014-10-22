package nl.tudelft.ti2206.game.event;

import java.util.EventListener;
import java.util.EventObject;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.exception.GameOver;
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

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GameEvent other = (GameEvent) obj;
			if (gameController == null) {
				if (other.gameController != null)
					return false;
			} else if (!gameController.equals(other.gameController))
				return false;
			return true;
		}
		
	}
	
	class GameOverEvent extends GameEvent {

		private static final long serialVersionUID = 8044024662518190266L;
		
		protected final GameOver gameOver;

		public GameOverEvent(GameController gameController, GameOver gameOver) {
			super(gameController);
			this.gameOver = gameOver;
		}
		
		public GameOver getGameOver() {
			return gameOver;
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
	
	class AmmoLoadEvent extends GameEvent {
		
		private static final long serialVersionUID = 6279153032481219593L;
		
		private final Bubble loadedBubble, nextBubble;

		public AmmoLoadEvent(GameController gameController, Bubble loadedBubble, Bubble nextBubble) {
			super(gameController);
			this.loadedBubble = loadedBubble;
			this.nextBubble = nextBubble;
		}

		public Bubble getLoadedBubble() {
			return loadedBubble;
		}

		public Bubble getNextBubble() {
			return nextBubble;
		}
		
	}
	
	void ammo(AmmoLoadEvent event);
	
	class ScoreEvent extends GameEvent {
		
		private static final long serialVersionUID = 7856300702718622571L;
		
		protected final int amountOfPoints;
		
		public ScoreEvent(GameController gameController, int amountOfPoints) {
			super(gameController);
			this.amountOfPoints = amountOfPoints;
		}

		/**
		 * @return the amountOfPoints
		 */
		public int getAmountOfPoints() {
			return amountOfPoints;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			ScoreEvent other = (ScoreEvent) obj;
			if (amountOfPoints != other.amountOfPoints)
				return false;
			return true;
		}
		
	}
	
	/**
	 * Invoked on this {@code BubbleMeshListener} when a {@link ScoreEvent} occurred
	 * @param event {@code ScoreEvent}
	 */
	void score(ScoreEvent event);
	
	@Override default void rowInsert(RowInsertEvent event) { }
	@Override default void pop(BubblePopEvent event) { }
	@Override default void shoot(CannonShootEvent event) { };
	@Override default void rotate(CannonRotateEvent event) {};
	
	/**
	 * {@code ScoreListener}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 * @author Liam Clark
	 *
	 */
	@FunctionalInterface
	interface ScoreListener extends GameListener {
		@Override default void shotMissed(ShotMissedEvent event) { }
		@Override default void ammo(AmmoLoadEvent event) {}
		@Override default void gameOver(GameOverEvent event) { }
	}
	
	@FunctionalInterface
	interface GameOverEventListener extends GameListener {
		@Override default void shotMissed(ShotMissedEvent event) { }
		@Override default void ammo(AmmoLoadEvent event) {}
		@Override default void score(ScoreEvent event) {}
	}
	
	@FunctionalInterface
	interface ShotMissedListener extends GameListener {
		@Override default void gameOver(GameOverEvent event) { }
		@Override default void ammo(AmmoLoadEvent event) {}
		@Override default void score(ScoreEvent event) {}
	}
	
	@FunctionalInterface
	interface AmmoListener extends GameListener {
		@Override default void gameOver(GameOverEvent event) { }
		@Override default void shotMissed(ShotMissedEvent event) { }
		@Override default void score(ScoreEvent event) {}
	}
}
