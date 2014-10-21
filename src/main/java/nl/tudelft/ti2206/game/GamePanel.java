package nl.tudelft.ti2206.game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.decorators.BombBubble;
import nl.tudelft.ti2206.bubbles.decorators.PopSoundBubble;
import nl.tudelft.ti2206.cannon.Cannon;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.graphics.animations.FallAnimation;
import nl.tudelft.ti2206.graphics.animations.FiniteAnimation;
import nl.tudelft.ti2206.util.mvc.View;
import nl.tudelft.util.ObservableObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public final class GamePanel extends JPanel implements View<GameController, GameModel> {
	
	private static final Logger log = LoggerFactory.getLogger(GamePanel.class);
	private static final long serialVersionUID = 2416543550015136242L;
	
	protected final static int WIDTH = 325;
	protected final static int HEIGHT = 400;
	protected final static int BUBBLE_QUEUE_SPACING = 60;
	protected final static Point CANNONPOSITION = new Point(WIDTH / 2, HEIGHT);
	protected final static Point AMMO_POSITION = CANNONPOSITION;
	
	protected ArrayList<FiniteAnimation> animationList = new ArrayList<FiniteAnimation>();
	
	protected final static Point AMMO_NEXT_POSITION = new Point(CANNONPOSITION.x + BUBBLE_QUEUE_SPACING,CANNONPOSITION.y);
	protected final static AudioClip POPSOUND =  Applet.newAudioClip(PopSoundBubble.class.getResource("/bubble_pop.wav"));
	
	protected final GameController gameController;
	protected final Cannon cannon;
	protected final Dimension size = new Dimension(WIDTH, HEIGHT);
	protected ObservableObject<Long> score = new ObservableObject<Long>(0l);
	
	protected transient static BufferedImage gameOver =	getGameOverImage();
	protected transient static Image gameWon =	getGameWonImage();
	
	public GamePanel(final GameController gameController) {
		GameModel gameModel = gameController.getModel();
		gameModel.setScreenSize(this.getPreferredSize());
		
		this.gameController = gameController;
		this.cannon = new Cannon(gameController.getCannonController(), CANNONPOSITION);
		
		positionAmmoBubbles();
		
		gameController.getModel().getBubbleMesh().getEventTarget().addScoreListener((scoreEvent) -> {
			int amount = scoreEvent.getAmountOfPoints();
			log.info("Awarded {} points", amount);
			setScore(getScore() + amount);
			if(POPSOUND != null) POPSOUND.play();
		});
		
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setVisible(true);
		
		this.gameController.getModel().getBubbleMesh().getEventTarget()
				.addPopListener((popEvent) -> {
					popEvent.getPoppedBubbles().forEach(bubble -> {
						animationList.add(new FallAnimation(bubble));
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
		
		if (model.isShooting()) {
			model.getShotBubble().render(graphics);
		}
		
		
		model.getLoadedBubble().setCenter(AMMO_POSITION);
		model.getLoadedBubble().render(graphics);
		model.getNextBubble().setCenter(AMMO_NEXT_POSITION);
		model.getNextBubble().render(graphics);
		
		Lists.newArrayList(animationList).forEach(animation -> {
			animation.render(graphics);
			if (animation.isDone()) {
				animationList.remove(animation); 
			}
		});
		
		if(model.isGameOver()&& !model.isWon()){
			graphics.drawImage(gameOver,0,HEIGHT/2-100,null);
		}
		
		if(model.isWon()){
			graphics.drawImage(gameWon, WIDTH/2-100, HEIGHT/2-100, null);
		}
	}
	
	protected static BufferedImage getGameOverImage() {
		try {
			BufferedImage scaledImage = ImageIO.read(BombBubble.class.getResourceAsStream("/gameover.jpg"));
			scaledImage.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
			return scaledImage;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static Image getGameWonImage() {
		try {
			BufferedImage scaledImage = ImageIO.read(BombBubble.class.getResourceAsStream("/gamewon.jpg"));
			return scaledImage.getScaledInstance(WIDTH/2, HEIGHT/2, Image.SCALE_SMOOTH);
			
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
	
	public Cannon getCannon() {
		return cannon;
	}
	
}
