package com.iwilkey.designa.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;

import java.awt.Rectangle;

public abstract class Entity {

    public static final int DEFAULT_HEALTH = 10;

    protected GameBuffer gb;
    protected float x, y;
    protected int width, height;
    protected int health;
    protected boolean active;
    protected Rectangle collider;

    public Entity(GameBuffer gb, float x, float y, int w, int h) {
        this.gb = gb;
        this.x = x; this.y = y;
        this.width = w; this.height = h;
        active = true;
        health = DEFAULT_HEALTH;
        collider = new Rectangle((int)x, (int)y, w, h);
    }

    public abstract void tick();
    public abstract void render(Batch b);
    public abstract void die();

    public void hurt(int amt) {
        health -= amt;
        if(health <= 0) {
            active = false;
            die();
        }
    }

    public Rectangle getCollisionBounds(float xOff, float yOff) {
        return new Rectangle((int) (x + collider.x + xOff),
                (int) (y + collider.y + yOff), collider.width, collider.height);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getHealth() { return health; }
    public boolean isActive() { return active; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setHealth(int h) { this.health = h; }
    public void setActive(boolean a) { this.active = a; }


}
