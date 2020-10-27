package dev.iwilkey.designa.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.world.World;

import java.awt.Rectangle;

public abstract  class Entity {

    public static int DEFAULT_HEALTH = 10;

    public World world;
    public boolean active;
    public float x, y;
    public int width, height, health;
    public Rectangle collider;

    public Entity(World world, float x, float y, int width, int height) {
        this.world = world;
        this.x = x; this.y = y; this.width = width; this.height = height;
        health = DEFAULT_HEALTH;
        collider = new Rectangle((int)x, (int)y, width, height);
        active = true;
    }

    public abstract void tick();
    public abstract void render(Batch b);
    public void hurt(int amt) {
        health -= amt;
        if(health <= 0) {
            active = false;
            die();
        }
    }
    public abstract void die();

}
