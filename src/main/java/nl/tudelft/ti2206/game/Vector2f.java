package nl.tudelft.ti2206.game;

public class Vector2f {
	public float x;
	public float y;
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector2f velocity) {
		this.x+=velocity.x;
		this.y+=velocity.y;
	}
}
