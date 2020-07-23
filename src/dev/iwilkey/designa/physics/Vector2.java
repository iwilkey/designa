package dev.iwilkey.designa.physics;

public class Vector2 {
	
	public double x, y;
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	// Add two vectors.
	public static Vector2 add(Vector2 a, Vector2 b) {
		double xf = a.x + b.x;
		double yf = a.y + b.y;
		return new Vector2(xf, yf);
	}
	
	// Subtract two vectors.
	public static Vector2 subtract(Vector2 a, Vector2 b) {
		double xf = b.x - a.x;
		double yf = b.y - a.y;
		return new Vector2(xf, yf);
	}
	
	// The dot product tells you what amount of one vector goes in the direction of another.
	public static double dot(Vector2 a, Vector2 b) {
		double xDir = a.x * b.x;
		double yDir = a.y * b.y;
		return xDir + yDir;
	}
	
}
