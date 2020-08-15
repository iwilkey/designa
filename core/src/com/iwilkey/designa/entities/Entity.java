package com.iwilkey.designa.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.statics.StaticEntity;

import java.awt.Rectangle;

public abstract class Entity {

    public static final int DEFAULT_HEALTH = 10;

    protected GameBuffer gb;
    public float x, y;
    protected int width, height;
    protected int health;
    protected boolean active;
    public Rectangle collider;

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

    public boolean checkEntityCollisions(float xOffset, float yOffset) {
        for (Entity e : gb.getWorld().getEntityHandler().getEntities()) {
            if(e.equals(this)) continue;
            if(e instanceof StaticEntity)
                if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset))) {
                    return true;
                }
        }

        return false;
    }

    public Rectangle getCollisionBounds(float xOff, float yOff) {
        return new Rectangle((int) (x + collider.x + xOff),
                (int) (y + collider.y + yOff), collider.width, collider.height);
    }

    protected void renderCollider(Batch b) {
        b.draw(Assets.selector, getCollisionBounds(0f,0f).x, getCollisionBounds(0f,0f).y, collider.width, collider.height);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getHealth() { return health; }
    public boolean isActive() { return active; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

}
