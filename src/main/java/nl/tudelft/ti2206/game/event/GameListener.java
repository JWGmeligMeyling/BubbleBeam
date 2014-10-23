package nl.tudelft.ti2206.game.event;

import java.util.EventListener;
import java.util.EventObject;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.Cannon;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameOver;
import nl.tudelft.ti2206.game.event.CannonListener.CannonEvent;

/**
 * A {@link GameListener} is an {@link EventListener} that listens to
 * {@link GameEvent GameEvents}, {@link CannonEvent CannonEvents} and
 * {@link BubbleMeshEvent BubbleMeshEvents}.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface GameListener extends EventListener, BubbleMeshListener, CannonListener {
	
	/**
	 * Abstract base class for {@link GameEvent GameEvents}. Extends
	 * {@link EventObject}, but forces to use a {@link GameController} as event
	 * source.
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	abstract class GameEvent extends EventObject {

		private static final long serialVersionUID = 816160582740508860L;
		
		protected final transient GameController gameController;
		
		/**
		 * Construct a new {@link GameEvent}
		 * 
		 * @param gameController
		 *            {@link GameController} to use as event source
		 */
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
	
	/**
	 * The {@link GameOverEvent} is triggered when a {@link GameOver} exception is thrown
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	class GameOverEvent extends GameEvent {

		private static final long serialVersionUID = 8044024662518190266L;
		
		protected final GameOver gameOver;
		
		/**
		 * Construct a new {@link GameOverEvent}
		 * 
		 * @param gameController
		 *            {@link GameController} for this {@link GameOverEvent}
		 * @param gameOver
		 *            {@link GameOver} exception
		 */
		public GameOverEvent(GameController gameController, GameOver gameOver) {
			super(gameController);
			this.gameOver = gameOver;
		}
		
		public GameOver getGameOver() {
			return gameOver;
		}
		
	}
	
	/**
	 * Invoked on the {@link GameListener GameListeners} when a
	 * {@link GameOverEvent} is triggered
	 * 
	 * @param event The {@link GameOverEvent}
	 */
	void gameOver(GameOverEvent event);
	
	/**
	 * The {@link ShotMissedEvent} is called when a shot bubble snapped to the
	 * {@link BubbleMesh}, but no bubbles were popped
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	class ShotMissedEvent extends GameEvent {

		private static final long serialVersionUID = 5365912303101540291L;
		
		/**
		 * Construct a new {@link ShotMissedEvent}
		 * @param gameController
		 *            {@link GameController} for this {@link GameOverEvent}
		 */
		public ShotMissedEvent(GameController gameController) {
			super(gameController);
		}
		
	}
	
	/**
	 * Invoked on the {@link GameListener GameListeners} when a
	 * {@link ShotMissedEvent} is triggered
	 * 
	 * @param event The {@link ShotMissedEvent}
	 */
	void shotMissed(ShotMissedEvent event);
	
	/**
	 * The {@link AmmoLoadEvent} is triggered when new ammunition is created for
	 * the {@link Cannon}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	class AmmoLoadEvent extends GameEvent {
		
		private static final long serialVersionUID = 6279153032481219593L;
		
		private final Bubble loadedBubble, nextBubble;
		
		/**
		 * Construct a new {@link AmmoLoadEvent}
		 * 
		 * @param gameController
		 *            {@link GameController} for this {@link GameOverEvent}
		 * @param loadedBubble
		 *            {@link Bubble} in the loaded ammo position
		 * @param nextBubble
		 *            {@link Bubble} in the next ammo position
		 */
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
	
	/**
	 * Invoked on {@link GameListener GameListeners} when a
	 * {@link AmmoLoadEvent} is triggered
	 * 
	 * @param event
	 *            the {@link AmmoLoadEvent}
	 */
	void ammo(AmmoLoadEvent event);
	
	/**
	 * The {@link ScoreEvent} is triggered when a player is awarded points
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	class ScoreEvent extends GameEvent {
		
		private static final long serialVersionUID = 7856300702718622571L;
		
		protected final int amountOfPoints;
		
		/**
		 * Construct a new {@link ScoreEvent}
		 * 
		 * @param gameController
		 *            {@link GameController} for this {@link GameOverEvent}
		 * @param amountOfPoints
		 *            the amount of points rewarded
		 */
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
	 * A {@link GameListener} that listens for {@link ScoreEvent ScoreEvents}
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
	
	/**
	 * A {@link GameListener} tat listens for {@link GameOverEvent
	 * GameOverEvents}
	 *
	 * @author Jan-Willem Gmelig Meyling
	 * @author Liam Clark
	 *
	 */
	@FunctionalInterface
	interface GameOverEventListener extends GameListener {
		@Override default void shotMissed(ShotMissedEvent event) { }
		@Override default void ammo(AmmoLoadEvent event) {}
		@Override default void score(ScoreEvent event) {}
	}
	
	/**
	 * A {@link GameListener} that listens for {@link ShotMissedEvent
	 * ShotMissedEvents}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 * @author Liam Clark
	 *
	 */
	@FunctionalInterface
	interface ShotMissedListener extends GameListener {
		@Override default void gameOver(GameOverEvent event) { }
		@Override default void ammo(AmmoLoadEvent event) {}
		@Override default void score(ScoreEvent event) {}
	}
	
	/**
	 * A {@link GameListener} that listens for {@link AmmoLoadEvent
	 * AmmoLoadEvents}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 * @author Liam Clark
	 *
	 */
	@FunctionalInterface
	interface AmmoListener extends GameListener {
		@Override default void gameOver(GameOverEvent event) { }
		@Override default void shotMissed(ShotMissedEvent event) { }
		@Override default void score(ScoreEvent event) {}
	}
}
