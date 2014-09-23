package nl.tudelft.ti2206.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.ti2206.room.Room;
import nl.tudelft.util.ObservableObject;

public abstract class GamePanel extends JPanel {

	private static final Logger log = LoggerFactory.getLogger(GamePanel.class);
	private static final long serialVersionUID = 2416543550015136242L;
	
	protected final static int WIDTH = 340;
	protected final static int HEIGHT = 400;
	
	private final Dimension size = new Dimension(WIDTH, HEIGHT);
	
	public static Point cannonPosition;
	protected Room room;	//initialized in subclasses
	protected GameTick gameTick;
	
	protected ObservableObject<Long> score = new ObservableObject<Long>(0l);
	
	public GamePanel(final BubbleMesh bubbleMesh) {
		cannonPosition = new Point(WIDTH / 2, HEIGHT);
		gameTick = new GameTick(33);
		gameTick.start();
		bubbleMesh.addScoreListener((amount) -> {
			log.info("Awarded {} points", amount);
			setScore(getScore() + amount);
		});
		
		bubbleMesh.calculatePositions();
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setVisible(true);
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
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		room.render(graphics);
	}
	
	public void gameStep() throws GameOver {
		this.repaint();
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
	
}
