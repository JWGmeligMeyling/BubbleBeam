package nl.tudelft.ti2206.game.backend.mode;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.inject.Inject;

import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.factory.ClassicBubbleFactory;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;

/**
 * In the {@code ClassicGameMode}, the {@link ClassicBubbleFactory} is used to
 * create new {@code Bubbles}. It uses only {@link ColouredBubble
 * ColouredBubbles}, no power-up bubbles. After 5 misses, a new row is inserted
 * on top.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@ModeName("Classic Mode")
@ModeBubbleFactory(ClassicBubbleFactory.class)
@ModeMusic("/classic.wav")
@ModeMaps({"/board.txt"})
public class ClassicGameMode implements GameMode {

	private static final long serialVersionUID = -5322766009361290612L;

	private final static int MAX_MISSES = 5;
	
	private int misses = 0;
	
	protected final GameController gameController;
	protected final GameModel gameModel;
	protected final Iterator<String> mapIterator;
	
	/**
	 * In the {@code ClassicGameMode}, the {@link ClassicBubbleFactory} is used to
	 * create new {@code Bubbles}. It uses only {@link ColouredBubble ColouredBubbles}, no power-up
	 * bubbles. After 5 misses, a new row is inserted on top.
	 * 
	 * @param gameController {@link GameController} for this {@code GameMode}
	 */
	@Inject
	public ClassicGameMode(GameController gameController) {
		this.gameController = gameController;
		this.gameModel = gameController.getModel();
		this.mapIterator = Arrays.asList(getMaps()).iterator();
	}

	@Override
	public void shotMissed(ShotMissedEvent event) {
		if (++misses == MAX_MISSES) {
			misses = 0;
			gameController.insertRow();
		}
	}
	
	@Override
	public void pop(BubblePopEvent event) {
		int amount = event.amountOfPoppedBubbles() * event.amountOfPoppedBubbles() * 25;
		gameController.incrementScore(amount);
	}

	@Override
	public BubbleMesh nextMap() throws IOException {
		String path = mapIterator.next();
		return BubbleMesh.parse(ClassicGameMode.class.getResourceAsStream(path));
	}
	
	@Override
	public boolean hasNextMap() {
		return mapIterator.hasNext();
	}
	
}
