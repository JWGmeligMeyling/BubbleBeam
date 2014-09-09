package nl.tudelft.ti2206.throwaway;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.BubbleMesh;

public class GuiThrowAwayPanel extends JPanel {
	
	private final BubbleMesh bubbleMesh;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 525456508008501827L;

	public GuiThrowAwayPanel() {
		try {
			this.bubbleMesh = BubbleMesh.parse(new File("src/main/resources/board.txt"));
		} catch (Exception e) {
			throw new RuntimeException("User too stupid, {put a username here}", e);
		}
		
		bubbleMesh.calculatePositions();
		
		bubbleMesh.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				GuiThrowAwayPanel.this.updateUI();
			}
			
		});
		
		this.setVisible(true);
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				bubbleMesh.insertRow();
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
