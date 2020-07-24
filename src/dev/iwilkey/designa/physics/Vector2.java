package dev.iwilkey.designa.physics;

public class Vector2 {
	
	public float x, y;
	
	public Vector2(int x, int y) {
		this.x = x; this.y = y;
	}
	
	public Vector2(float x, float y) {
		this.x = x; this.y = y;
	}
	
	public Vector2(double x, double y) {
		this.x = (float)x; this.y = (float)y;
	}
	
	public static Vector2 add(Vector2 a, Vector2 b) {
		return new Vector2((a.x + b.x), (a.y + b.y));
	}
	
	public static Vector2 subtract(Vector2 a, Vector2 b) {
		return new Vector2((a.x - b.x), (a.y - b.y));
	}
	
	public static float magnitude(Vector2 v) {
		return (float) Math.round(Math.sqrt((Math.pow(v.x, 2) + Math.pow(v.y, 2))));
	}
	
}
