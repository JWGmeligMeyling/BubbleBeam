package nl.tudelft.ti2206.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import nl.tudelft.ti2206.throwaway.Bubble;
import nl.tudelft.ti2206.throwaway.GuiThrowAway;

public class Cannon {
	public final int x, y;
	public double angle = 0;
	public Bubble nextBubble, loadedBubble;
	public final boolean mouseControl;
	
	public Cannon(int x, int y) {
		this.x = x;
		this.y = y;
		mouseControl = true;
	}
	
	public Cannon(int x, int y, boolean mouseControl) {
		this.x = x;
		this.y = y;
		this.mouseControl = mouseControl;
	}
	
	public void update() {
		if (mouseControl) {
			Point mousePosition = GuiThrowAway.instance.getMousePosition();
			if (mousePosition != null) {
				angle = Math.atan2(y - mousePosition.y, mousePosition.x - x);
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.drawRect(x - 8, y - 8, 16, 16);
		g.setColor(Color.green);
		g.drawLine(x, y, (int) (x + Math.cos(angle) * 50), (int) (y - Math.sin(angle) * 50));
	}
}
