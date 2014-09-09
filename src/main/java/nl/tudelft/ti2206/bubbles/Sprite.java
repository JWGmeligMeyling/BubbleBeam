package nl.tudelft.ti2206.bubbles;

import java.awt.Graphics;
import java.awt.Point;

public interface Sprite {
	
	void render(Graphics graphics);	
	void setPosition(Point position);
	Point getPosition();
	int getX();
	int getY();
	
}
