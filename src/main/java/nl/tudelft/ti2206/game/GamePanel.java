package nl.tudelft.ti2206.game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.Observer;


import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.cannon.Cannon;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.backend.MasterGameController;
import nl.tudelft.ti2206.game.backend.SlaveGameController;
import nl.tudelft.ti2206.util.mvc.View;
import nl.tudelft.util.ObservableObject;

public final class GamePanel extends JPanel implements View<GameController, GameModel> {

	private static final Logger log = LoggerFactory.getLogger(GamePanel.class);
	private static final long serialVersionUID = 2416543550015136242L;
	protected final int BUBBLE_QUEUE_SPACING = 60;
	
	protected final static int WIDTH = 325;
	protected final static int HEIGHT = 400;
	
	private final Dimension size = new Dimension(WIDTH, HEIGHT);
	
	private final GameController gameController;
	private final Point cannonPosition;
	private final Cannon cannon;
	
	protected ObservableObject<Long> score = new ObservableObject<Long>(0l);
	
	public GamePanel(final MasterGameController gameController) {
		this((GameController) gameController);
		gameController.getCannonController().bindListenersTo(this, cannon);
	}
	
	public GamePanel(final SlaveGameController gameController) {
		this((GameController) gameController);
		this.setBackground(new Color(225,225,225));
	}
	
	public GamePanel(final GameController gameController) {
		GameModel gameModel = gameController.getModel();
		gameModel.setScreenSize(this.getPreferredSize());
		
		this.gameController = gameController;
		this.cannonPosition = new Point(getWidth() / 2, getHeight());
		this.cannon = new Cannon(gameController.getCannonController(), cannonPosition);
		
		positionAmmoBubbles();
		
		gameController.getModel().getBubbleMesh().addScoreListener((amount) -> {
			log.info("Awarded {} points", amount);
			setScore(getScore() + amount);
		});
		
		gameController.getModel().getBubbleMesh().addScoreListener((amount) -> {
			Applet.newAudioClip(GamePanel.class.getResource("/bubble_pop.wav")).play();
		});
		
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setVisible(true);
	}

	protected void positionAmmoBubbles() {
		GameModel gameModel = gameController.getModel();
		ColouredBubble loadedBubble = gameModel.getLoadedBubble();
		ColouredBubble nextBubble = gameModel.getNextBubble();
		loadedBubble.setCenter(new Point(cannonPosition.x, cannonPosition.y));
		nextBubble.setCenter(new Point(cannonPosition.x + BUBBLE_QUEUE_SPACING, cannonPosition.y));
	}
	
	private final static Image BACKGROUND = getBackgroundImage();
	
	private static Image getBackgroundImage() {
		try {
			return ImageIO.read(GamePanel.class.getResourceAsStream("/sb_bubbles.jpg"));
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
			return null;
		}
	}
	
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		
		if(BACKGROUND != null)
			graphics.drawImage(BACKGROUND, 0, 0, WIDTH, HEIGHT, null);
		
		GameModel model = gameController.getModel();
		
		gameController.getModel().getBubbleMesh()
				.forEach(bubble -> bubble.render(graphics));
		cannon.render(graphics);
		
		if(model.isShooting()) {
			model.getShotBubble().render(graphics);
		}
		
		model.getLoadedBubble().render(graphics);
		model.getNextBubble().render(graphics);
	}

	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return size;
	}
	
	@Override
	public Dimension getMaximumSize() {
		return size;
	}
	
	@Override
	public int getWidth() {
		return size.width;
	}

	@Override
	public int getHeight() {
		return size.height;
	}

	public long getScore() {
		return score.getValue();
	}
	
	public void setScore(final long value) {
		score.setValue(value);
	}
	
	public void observeScore(final Observer o) {
		score.addObserver(o);
	}

	@Override
	public GameController getController() {
		return gameController;
	}
	
}
