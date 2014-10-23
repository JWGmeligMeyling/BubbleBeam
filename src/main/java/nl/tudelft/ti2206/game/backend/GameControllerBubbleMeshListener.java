package nl.tudelft.ti2206.game.backend;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.game.event.BubbleMeshListener;
import nl.tudelft.ti2206.game.event.GameListener.GameEvent;

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
	 *            {@code GameModel} for this
	 *            {@code GameControllerBubbleMeshListener}
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
		gameModel.retainRemainingColors(event.getSource().getRemainingColours());
	}
	
}