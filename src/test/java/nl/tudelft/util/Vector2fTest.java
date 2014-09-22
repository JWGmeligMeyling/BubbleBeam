package nl.tudelft.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector2fTest {

	@Test
	public void additionTest() {
		Vector2f a = new Vector2f(1f,2f);
		Vector2f b = new Vector2f(3f,4f);
		Vector2f ans = new Vector2f(4f,6f);
		
		assertEquals(ans, a.add(b));
	}
	
	@Test
	public void subtractionTest() {
		Vector2f a = new Vector2f(1f,2f);
		Vector2f b = new Vector2f(3f,4f);
		Vector2f ans = new Vector2f(-2f,-2f);
		
		assertEquals(ans, a.subtract(b));
	}
	
	@Test
	public void multiplicationWithVectorTest() {
		Vector2f a = new Vector2f(1f,2f);
		Vector2f b = new Vector2f(3f,4f);
		Vector2f ans = new Vector2f(3f,8f);
		
		assertEquals(ans, a.multiply(b));
	}
	
	@Test
	public void multiplicationWithScalarTest() {
		Vector2f a = new Vector2f(1f,2f);
		float b = 2f;
		Vector2f ans = new Vector2f(2f,4f);
		
		assertEquals(ans, a.multiply(b));
	}
	
	@Test
	public void divideByVectorTest() {
		Vector2f a = new Vector2f(4f,9f);
		Vector2f b = new Vector2f(2f,3f);
		Vector2f ans = new Vector2f(2f,3f);
		
		assertEquals(ans, a.divide(b));
	}
	
	@Test
	public void divideByScalarTest() {
		Vector2f a = new Vector2f(2f,4f);
		float b = 2f;
		Vector2f ans = new Vector2f(1f,2f);
		
		assertEquals(ans, a.divide(b));
	}
	
	@Test
	public void normalizeTest() {
		Vector2f a = new Vector2f(3f,4f);
		Vector2f ans = new Vector2f(0.6f,0.8f);
		
		assertEquals(ans, a.normalize());
	}

}
