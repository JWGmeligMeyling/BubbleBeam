package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Observable;
import java.util.Set;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.game.GamePanel;
import nl.tudelft.ti2206.game.backend.mode.GameMode;
import nl.tudelft.util.AbstractEventTarget;
import nl.tudelft.util.EventTarget;
import nl.tudelft.ti2206.game.event.GameListener;

/**
 * The {@link GameModel} is the model for the {@link GameController} and {@link GamePanel}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class GameModel extends Observable implements EventTarget<GameListener>, Serializable {
	
	private static final long serialVersionUID = 7372457527680874284L;

	private static final Logger log = LoggerFactory.getLogger(GameModel.class);
	
	private transient AbstractEventTarget<GameListener> eventTarget = new AbstractEventTarget<GameListener>();
	
	private Set<Color> remainingColors;
	
	private final Class<? extends GameMode> gameMode;
	
	private BubbleMesh bubbleMesh;
	
	private transient MovingBubble shotBubble;
	
	private Bubble loadedBubble, nextBubble;
	
	private long score = 0;
	
	private GameState state = GameState.PLAYING;
	
	private Dimension screenSize;
	
	public GameModel(final Class<? extends GameMode> gameMode) {
		log.info("Constructed {} with {}", this, gameMode);
		this.gameMode = gameMode;
	}
	
	public void setBubbleMesh(BubbleMesh bubbleMesh) {
		this.bubbleMesh = bubbleMesh;
		this.remainingColors = Sets.newHashSet(bubbleMesh.getRemainingColours());
	}
	
	public void retainRemainingColors(final Collection<Color> colours) {
		this.remainingColors.retainAll(colours);
		if(loadedBubble.hasColor())
			this.remainingColors.add(loadedBubble.getColor());
		if(nextBubble.hasColor())
			this.remainingColors.add(nextBubble.getColor());
	}
	
	public BubbleMesh getBubbleMesh() {
		return bubbleMesh;
	}
		
	public MovingBubble getShotBubble() {
		return shotBubble;
	}
	
	public void setShotBubble(final MovingBubble shotBubble) {
		this.shotBubble = shotBubble;
		this.setChanged();
	}

	public Bubble getLoadedBubble() {
		return loadedBubble;
	}

	public void setLoadedBubble(Bubble loadedBubble) {
		this.loadedBubble = loadedBubble;
		this.setChanged();
	}

	public Bubble getNextBubble() {
		return nextBubble;
	}

	public void setNextBubble(Bubble nextBubble) {
		this.nextBubble = nextBubble;
		this.setChanged();
	}

	public long getScore() {
		return score;
	}

	public void incrementScore(long amount) {
		score += amount;
		setChanged();
	}

	public Set<Color> getRemainingColors() {
		return remainingColors;
	}
	
	public void setGameState(GameState state) {
		this.state = state;

		this.setChanged();
	}
	
	public GameState getGameState() {
		return state;
	}

	public Dimension getScreenSize() {
		return screenSize;
	}
	
	public void setScreenSize(final Dimension dimension) {
		this.screenSize = dimension;
		this.setChanged();
	}

	@Override
	public void addEventListener(GameListener listener) {
		eventTarget.addEventListener(listener);
	}

	@Override
	public void removeEventListener(GameListener listener) {
		eventTarget.removeEventListener(listener);
	}

	@Override
	public void trigger(Consumer<GameListener> action) {
		eventTarget.trigger(action);
	}

	public Class<? extends GameMode> getGameMode() {
		return gameMode;
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        eventTarget = new AbstractEventTarget<GameListener>();
    }

}
