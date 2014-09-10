package nl.tudelft.ti2206.throwaway;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nl.tudelft.ti2206.bubbles.AbstractBubble;
import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.game.Cannon;

public class GuiThrowAwayPanel extends JPanel {
	
	private final BubbleMesh bubbleMesh;
	private final Cannon cannon;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 525456508008501827L;

	public GuiThrowAwayPanel() {
		try {
			this.bubbleMesh = BubbleMesh.parse(new File("src/main/resources/anotherboard"));
		} catch (Exception e) {
			throw new RuntimeException("User too stupid, {put a username here}", e);
		}
		
		cannon = new Cannon(new Point(WIDTH/2, 400));
		cannon.bindMouseListenerTo(this);
		
		bubbleMesh.calculatePositions();
		
		bubbleMesh.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				GuiThrowAwayPanel.this.repaint();
			}
			
		});
		
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED) );
		this.setVisible(true);

		cannon.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				GuiThrowAwayPanel.this.repaint();
			}
			
		});
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//bubbleMesh.insertRow();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}	
	
	public final static int WIDTH = AbstractBubble.WIDTH * 10 + AbstractBubble.WIDTH / 2 + 4;
	public final static int HEIGHT = 400;
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	@Override
	public void paintComponent(final Graphics graphics){
		super.paintComponent(graphics);
		cannon.render(graphics);
		for(Bubble bubble : bubbleMesh) {
			bubble.render(graphics);
		}
	}

	public void gameStep() {
		if(cannon!=null){
			cannon.gameStep();
		}
	}

}
