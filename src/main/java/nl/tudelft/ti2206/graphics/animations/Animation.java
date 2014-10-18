package nl.tudelft.ti2206.graphics.animations;

import java.awt.Graphics;

public abstract class Animation {
	public abstract boolean isDone();

	public abstract void render(Graphics graphics);
}
