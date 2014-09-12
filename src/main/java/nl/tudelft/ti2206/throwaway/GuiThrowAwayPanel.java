package nl.tudelft.ti2206.throwaway;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.cannon.Cannon;
import nl.tudelft.ti2206.exception.GameOver;
import nl.tudelft.util.ObservableObject;

public class GuiThrowAwayPanel extends JPanel {
	
	public final static int WIDTH = AbstractBubble.WIDTH * 10 + AbstractBubble.WIDTH / 2 + 4;
	public final static int HEIGHT = 400;

	private final BubbleMesh bubbleMesh;
	private final Cannon cannon;
	private final Dimension size = new Dimension(WIDTH, HEIGHT);
	
	private ObservableObject<Long> score = new ObservableObject<Long>(0l);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 525456508008501827L;

	public GuiThrowAwayPanel(final BubbleMesh bubbleMesh) {
		this.bubbleMesh = bubbleMesh;
		bubbleMesh.addScoreListener((amount) -> { setScore(getScore() + amount); });
		this.cannon = new Cannon(bubbleMesh, new Point(WIDTH / 2, 400),
				this.getPreferredSize(), this.getLocation());
		this.cannon.bindMouseListenerTo(this);
		
		bubbleMesh.calculatePositions();
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED) );
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
		cannon.render(graphics);
		for(Bubble bubble : bubbleMesh) {
			bubble.render(graphics);
		}
	}

	public void gameStep() throws GameOver {
		cannon.gameStep();
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
