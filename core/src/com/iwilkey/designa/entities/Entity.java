package com.iwilkey.designa.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.statics.StaticEntity;
import com.iwilkey.designa.world.World;

import java.awt.Rectangle;

/**

 This is an abstract class that defines the common facilities of a Designa entity.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

public abstract class Entity {

    /**
     * Global vars
     */

    // Final vars
    public static final int DEFAULT_HEALTH = 10;

    // Misc objects
    protected GameBuffer gb;
    public Rectangle collider;

    // Floats
    public float x, y;

    // Integers
    protected int width, height, health;

    // Booleans
    protected boolean active;

    /**
     * Entity constructor.
     * @param gb An Entity needs the GameBuffer.
     * @param x The x spawn location of the Entity.
     * @param y The y spawn location of the Entity.
     * @param w The width of the Entity.
     * @param h The height of the Entity.
     */
    public Entity(GameBuffer gb, float x, float y, int w, int h) {
        // Set the GameBuffer.
        this.gb = gb;

        // Set numerical values.
        this.x = x; this.y = y;
        this.width = w; this.height = h;

        // Set health to the default value.
        health = DEFAULT_HEALTH;

        // Set the collider.
        collider = new Rectangle((int)x, (int)y, w, h);

        // Make it active.
        active = true;
    }

    /**
     * The abstract tick method. This is called every frame.
     */
    public abstract void tick();

    /**
     * The abstract render method. This is called every frame and has the graphics batch to draw with.
     * @param b Every render method needs the graphics batch.
     */
    public abstract void render(Batch b);

    /**
     * The abstract die method. This is called when an entity dies.
     */
    public abstract void die();

    /**
     * This method defines how an entity is hurt.
     * @param amt The amount of damage dealt.
     */
    public void hurt(int amt) {
        // Check if the entity should be dead...
        if(health <= 0) {
            active = false;
            die();
            return;
        }

        // Decrement health by amount.
        health -= amt;
    }

    /**
     * This method defines how an entity can check collisions between itself and the rest of the entities in the world.
     * Note: I really am only utilizing this for collisions with StaticEntities.
     * @param xOffset The x-offset is used to predict collisions in the future so as to not cause any issues during runtime.
     * @param yOffset The y-offset is used to predict collisions in the future so as to not cause any issues during runtime.
     * @return If the entity is about to collide with another entity.
     */
    public boolean checkEntityCollisions(float xOffset, float yOffset) {
        // Loop through all active entities...
        for (Entity e : World.getEntityHandler().getEntities()) {
            if(e.equals(this)) continue; // Skip over this entity in the list...
            // If the entity is a StaticEntity...
            if(e instanceof StaticEntity)
                // If the two collide, return true.
                if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset))) return true;
        }
        // Otherwise, no collision was detected.
        return false;
    }

    /**
     * THis method will return a rectangle that outlines the collider space around an entity.
     * @param xOff The x can be moved a value of xOff if needed.
     * @param yOff The y can be moved a value of xOff if needed.
     * @return A rectangle that outlines the collider space around an entity.
     */
    public Rectangle getCollisionBounds(float xOff, float yOff) {
        return new Rectangle((int) (x + collider.x + xOff),
                (int) (y + collider.y + yOff), collider.width, collider.height);
    }

    /**
     * This method can be used in any polymorph to render the collider around it.
     * @param b Every render, or sub-render method needs the graphics batch.
     */
    protected void renderCollider(Batch b) {
        b.draw(Assets.selector, getCollisionBounds(0f,0f).x,
                getCollisionBounds(0f,0f).y, collider.width, collider.height);
    }

    /*
     * GETTERS AND SETTERS
     */

    /**
     * X location getter.
     * @return The x location of the Entity.
     */
    public float getX() { return x; }

    /**
     * Y location getter.
     * @return The y location of the Entity.
     */
    public float getY() { return y; }

    /**
     * Width getter.
     * @return The width of the Entity.
     */
    public int getWidth() { return width; }

    /**
     * Height getter.
     * @return The height of the Entity.
     */
    public int getHeight() { return height; }

    /**
     * Health getter.
     * @return The health of the Entity.
     */
    public int getHealth() { return health; }

    /**
     * Is active getter.
     * @return If the entity is active or not.
     */
    public boolean isActive() { return active; }

    /**
     * X location setter.
     * @param x The new x location.
     */
    public void setX(float x) { this.x = x; }

    /**
     * Y location setter.
     * @param y The new x location.
     */
    public void setY(float y) { this.y = y; }

}
