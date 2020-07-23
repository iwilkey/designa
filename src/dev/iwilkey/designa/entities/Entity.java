package dev.iwilkey.designa.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import dev.iwilkey.designa.AppBuffer;

public abstract class Entity {
	
	public static final int DEFAULT_HEALTH = 10;
	
	protected AppBuffer ab;
	protected float x, y;
	protected int width, height;
	protected int health;
	protected boolean active = true;
	protected Rectangle collider;
	
	public Entity(AppBuffer ab, float x, float y, int w, int h) {
		this.ab = ab;
		this.x = x; this.y = y;
		this.width = w; this.height = h;
		health = DEFAULT_HEALTH;
		collider = new Rectangle((int)x, (int)y, w, h);
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract void die();
	
	public void hurt(int amt) {
		health -= amt;
		if(health <= 0) {
			active = false;
			die();
		}
	}
	
	public Rectangle getCollisionBounds(float xOffset, float yOffset) {
		return new Rectangle((int) (x + collider.x + xOffset), 
				(int) (y + collider.y + yOffset), collider.width, collider.height);
	}
	
	protected void drawCollider(Graphics g) {
		g.setColor(Color.red);
		g.drawRect((int)x + collider.x, (int)y + collider.y, collider.width, collider.height);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
