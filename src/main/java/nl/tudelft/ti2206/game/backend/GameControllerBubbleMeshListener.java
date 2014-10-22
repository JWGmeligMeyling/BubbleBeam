package nl.tudelft.ti2206.game.backend;

import nl.tudelft.ti2206.game.event.BubbleMeshListener;

/**
 * The {@code GameControllerBubbleMeshListener} listens for events on the
 * {@link BubbleMesh}, delegates the event as {@link GameEvent} on the
 * {@link GameModel} and invokes the right actions on the {@link GameController}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class GameControllerBubbleMeshListener implements BubbleMeshListener {
	
	protected final GameModel gameModel;
	
	/**
	 * Construct a new {@link GameControllerBubbleMeshListener}
	 * 
	 * The {@code GameControllerBubbleMeshListener} listens for events on the
	 * {@link BubbleMesh}, delegates the event as {@link GameEvent} on the
	 * {@link GameModel} and invokes the right actions on the
	 * {@link GameController}
	 * 
	 * @param gameModel
	 */
	public GameControllerBubbleMeshListener(GameModel gameModel) {
		this.gameModel = gameModel;
	}
	
	@Override
	public void rowInsert(RowInsertEvent event) {
		gameModel.trigger(listener -> listener.rowInsert(event));
	}
	
	@Override
	public void pop(BubblePopEvent event) {
		gameModel.trigger(listener -> listener.pop(event));
	}
	
	@Override
	public void score(ScoreEvent event) {
		int amount = event.getAmountOfPoints();
		gameModel.incrementScore(amount);
		gameModel.retainRemainingColors(event.getSource().getRemainingColours());
		gameModel.notifyObservers();
		gameModel.trigger(listener -> listener.score(event));
	}
	
}