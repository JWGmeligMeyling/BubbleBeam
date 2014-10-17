package nl.tudelft.ti2206.game.backend;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Observable;
import java.util.Set;

import com.google.common.collect.Sets;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;

public class GameModel extends Observable {
	
	private final Set<Color> remainingColors;
	
	private BubbleMesh bubbleMesh;
	
	private MovingBubble shotBubble;
	
	private Bubble loadedBubble, nextBubble;
	
	private long score = 0;
	
	private boolean gameOver = false;
	
	private int misses = 0;
	
	private Dimension screenSize;
	
	public GameModel(final BubbleMesh bubbleMesh) {
		this.bubbleMesh = bubbleMesh;
		this.remainingColors = Sets.newHashSet(bubbleMesh.getRemainingColours());
	}
	
	public void retainRemainingColors(final Collection<Color> colours) {
		this.remainingColors.retainAll(colours);
		if(ColouredBubble.class.isInstance(loadedBubble))
			this.remainingColors.add(ColouredBubble.class.cast(loadedBubble).getColor());
		if(ColouredBubble.class.isInstance(nextBubble))
			this.remainingColors.add(ColouredBubble.class.cast(nextBubble).getColor());
	}
	
	public BubbleMesh getBubbleMesh() {
		return bubbleMesh;
	}
	
	public boolean isShooting() {
		return shotBubble != null;
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

	public void setScore(long score) {
		this.score = score;
		this.setChanged();
	}
	
	public void incrementScore(long amount) {
		setScore(score + amount);
	}

	public Set<Color> getRemainingColors() {
		return remainingColors;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
		this.setChanged();
	}

	public int getMisses() {
		return misses;
	}

	public void setMisses(int misses) {
		this.misses = misses;
		this.setChanged();
	}

	public Dimension getScreenSize() {
		assert this.screenSize != null : "Screen size should not be null";
		return screenSize;
	}
	
	public void setScreenSize(final Dimension dimension) {
		this.screenSize = dimension;
		this.setChanged();
	}

	public void setBubbleMesh(BubbleMesh bubbleMesh) {
		this.bubbleMesh.replace(bubbleMesh);
		this.setChanged();
	}
	
}
