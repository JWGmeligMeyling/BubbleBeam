package nl.tudelft.ti2206.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.decorators.MovingBubble;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.Cannon;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.event.BubbleMeshListener;
import nl.tudelft.ti2206.graphics.animations.FiniteAnimation;
import nl.tudelft.ti2206.util.mvc.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The GamePanel is the panel which contains and paints the {@link Cannon} and
 * the {@link BubbleMesh}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public final class GamePanel extends JPanel implements View<GameController, GameModel> {
	
	private static final Logger log = LoggerFactory.getLogger(GamePanel.class);
	private static final long serialVersionUID = 2416543550015136242L;
	
	protected final static int WIDTH = 325;
	protected final static int HEIGHT = 400;
	protected final static int BUBBLE_QUEUE_SPACING = 60;
	protected final static Point CANNONPOSITION = new Point(WIDTH / 2, HEIGHT);
	protected final static Point AMMO_POSITION = CANNONPOSITION;
	
	protected ArrayList<FiniteAnimation> animationList = new ArrayList<FiniteAnimation>();
	
	protected final static Point AMMO_NEXT_POSITION = new Point(CANNONPOSITION.x
			+ BUBBLE_QUEUE_SPACING, CANNONPOSITION.y);
	
	
	protected final GameController gameController;
	protected final Cannon cannon;
	protected final Dimension size = new Dimension(WIDTH, HEIGHT);
	
	protected transient static BufferedImage gameOver = getGameOverImage();
	protected transient static Image gameWon = getGameWonImage();
	
	public GamePanel(final GameController gameController) {
		GameModel gameModel = gameController.getModel();
		gameModel.setScreenSize(this.getPreferredSize());
		
		this.gameController = gameController;
		this.cannon = new Cannon(gameController.getCannonController(), CANNONPOSITION);
		
		positionAmmoBubbles();
		
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setVisible(true);
		
		this.gameController
			.getModel()
			.getBubbleMesh()
			.addEventListener(BubbleMeshListener.PopListener.class, (popEvent) -> {
				popEvent.getPoppedBubbles().forEach(bubble -> {
					animationList.add(bubble.getAnimation());
				});
				
			});
	}
	
	protected void positionAmmoBubbles() {
		GameModel gameModel = gameController.getModel();
		Bubble loadedBubble = gameModel.getLoadedBubble();
		Bubble nextBubble = gameModel.getNextBubble();
		loadedBubble.setCenter(new Point(CANNONPOSITION.x, CANNONPOSITION.y));
		nextBubble.setCenter(new Point(CANNONPOSITION.x + BUBBLE_QUEUE_SPACING, CANNONPOSITION.y));
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
		
		if (BACKGROUND != null)
			graphics.drawImage(BACKGROUND, 0, 0, WIDTH, HEIGHT, null);
		
		GameModel model = gameController.getModel();
		
		gameController.getModel().getBubbleMesh().render(graphics);
		cannon.render(graphics);
		
		MovingBubble shotBubble = model.getShotBubble();
		if (shotBubble != null) {
			shotBubble.render(graphics);
		}
		
		model.getLoadedBubble().setCenter(AMMO_POSITION);
		model.getLoadedBubble().render(graphics);
		model.getNextBubble().setCenter(AMMO_NEXT_POSITION);
		model.getNextBubble().render(graphics);
		
		synchronized(animationList) {
			animationList.removeIf(FiniteAnimation::isDone);
			animationList.forEach(animation -> {
				animation.render(graphics);
				animation.addTime();
			});
		}
		
		switch(model.getGameState()) {
		case LOSE:
			graphics.drawImage(gameOver, 0, HEIGHT / 2 - 100, null);
			break;
		case WIN:
			graphics.drawImage(gameWon, 0, HEIGHT / 2 - 100, null);
			break;
		default:
			break;
		}
	}
	
	protected static BufferedImage getGameOverImage() {
		try {
			BufferedImage scaledImage = ImageIO.read(GamePanel.class
					.getResourceAsStream("/gamelost.png"));
			return scaledImage;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static BufferedImage getGameWonImage() {
		try {
			BufferedImage scaledImage = ImageIO.read(GamePanel.class
					.getResourceAsStream("/gamewon.png"));
			return scaledImage;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
	
	@Override
	public GameController getController() {
		return gameController;
	}
	
	public Cannon getCannon() {
		return cannon;
	}
	
}
