package nl.tudelft.ti2206.throwaway;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GuiThrowAwayPanel extends JPanel {
	
	private final BubbleMesh bubbleMesh;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 525456508008501827L;

	public GuiThrowAwayPanel() {
		this.bubbleMesh = BubbleMesh.parse(BubbleMesh.TEMPLATE_STRING);
		bubbleMesh.calculatePositions();
		this.setVisible(true);
	}	
	
	protected final static int WIDTH = 400;
	protected final static int HEIGHT = 400;
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	@Override
	public void paintComponent(final Graphics g){
		for(Bubble bubble : bubbleMesh) {
			bubble.render(g);
		}
	}
}
