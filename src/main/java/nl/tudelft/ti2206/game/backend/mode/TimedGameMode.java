package nl.tudelft.ti2206.game.backend.mode;

import javax.inject.Inject;

import nl.tudelft.ti2206.bubbles.factory.PowerUpBubbleFactory;
import nl.tudelft.ti2206.game.backend.GameController;

/**
 * In the {@code TimedGameMode}, the {@link PowerUpBubbleFactory} is used to
 * create new {@code Bubbles}. It uses both {@link ColouredBubbles} and power-up
 * bubbles, some with the Drunk Bubble effect. Every tick all bubbles move down
 * a bit, and the goal is to pop all bubbles before they collide with the bottom.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
@ModeName("Timed Mode")
@ModeBubbleFactory(PowerUpBubbleFactory.class)
@ModeMusic("/time_music.wav")
public class TimedGameMode extends ClassicGameMode {

	private static final long serialVersionUID = 2150478663941653803L;	
	
	protected final static int TICK_AMOUNT = 30;
	
	protected int ticks = 0;

	/**
	 * In the {@code TimedGameMode}, the {@link PowerUpBubbleFactory} is used to
	 * create new {@code Bubbles}. It uses both {@link ColouredBubbles} and
	 * power-up bubbles, some with the Drunk Bubble effect. Every tick all
	 * bubbles move down a bit, and the goal is to pop all bubbles before they
	 * collide with the bottom.
	 * 
	 * @param gameController {@link GameController} for this {@code GameMode}
	 */
	@Inject
	public TimedGameMode(GameController gameController) {
		super(gameController);
	}

	@Override
	public void gameTick() {
		if((ticks = ++ticks % TICK_AMOUNT) == 0) {
			gameModel.getBubbleMesh().translate(0, 1);
		}
	}
	
	@Override
	public void shotMissed(ShotMissedEvent event) {
		// don't insert new row
	}
	
}
