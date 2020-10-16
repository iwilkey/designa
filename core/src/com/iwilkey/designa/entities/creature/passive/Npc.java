package com.iwilkey.designa.entities.creature.passive;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.entities.creature.Creature;
import com.iwilkey.designa.entities.creature.violent.Enemy;
import com.iwilkey.designa.gfx.Animation;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.awt.*;

/**

 This class defines a passive non-playable-character.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

// Note: This object is a Creature.
public class Npc extends Creature {

    /**
     * Global vars
     */

    // Final vars
    final Animation[] animations;

    // Timers
    long timer = 0; // Master timer
    long DECISION_TIME = 100; // Default decision time

    // Booleans
    boolean walkLeft = true;

    // Strings
    public String name;

    // Integers
    int heartSpacing = 4;

    public Rectangle hitBox;

    /**
     * Npc constructor.
     * @param gb An instance of this class needs the GameBuffer.
     * @param x The x spawn location of this Npc.
     * @param y The y spawn location of this Npc.
     */
    public Npc(GameBuffer gb, float x, float y) {
        // Invoke the Creature constructor.
        super(gb, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);

        // Give the Npc a random name.
        name = Assets.maleNames[MathUtils.random(0, Assets.maleNames.length - 1)];
        // Give the Npc a random speed between the range below.
        speed = MathUtils.random(DEFAULT_SPEED - 0.4f, DEFAULT_SPEED + 0.4f);

        // Init facing direction.
        facingLeft = true; facingRight = false;

        // Set the collider.
        collider.width = width - 9;
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;

        hitBox = new Rectangle(collider.x, collider.y, collider.width, collider.height);

        // Init Animations
        animations = new Animation[2];
        animations[0] = new Animation(MathUtils.random(80, 150), Assets.manWalkRight);
        animations[1] = new Animation(MathUtils.random(80, 150), Assets.manWalkLeft);
    }

    // TODO: Make an AI Behavior web that Npcs can choose actions on.
    // TODO: Then, apply perlin noise with time and see that happens.

    /**
     * The tick method. I like to think of this method like the brain.
     */
    @Override
    public void tick() {

        checkEnemyCollision();

        // Tick the animations forward.
        for(Animation anim : animations) anim.tick();
        // Check if it is stuck.
        checkStuck();

        flashCheck();

        // Increment the timer until next decision time.
        timer++;
        if(timer > DECISION_TIME) {
            // Make a random decision to either turn right or stay left.
            walkLeft = MathUtils.random(0, 1) != 1;
            // Apply it.
            facingLeft = walkLeft; facingRight = !facingLeft;
            // Jump.
            if(!isJumping && isGrounded) jump();
            // Set a new decision time.
            DECISION_TIME = MathUtils.random(1, 200);
            // Reset the timer.
            timer = 0;
        }

        // Move. This is defined in the Creature class.
        move();

        hitBox.x = (int)x; hitBox.y = (int)y;

        // Move the x value of the Npc based off of the decision it made and speed it can go.
        xMove = (walkLeft) ? (speed / 2) : -(speed / 2);
    }

    private void checkEnemyCollision() {
        for(Entity e : World.getEntityHandler().getEntities()) {
            if(e instanceof Enemy) {
                if(((Enemy) e).hitBox.intersects(hitBox)) {
                    hurt((((Enemy) e).damagePotential));
                    World.particleHandler.startParticle("large-explosion", hitBox.x, hitBox.y);
                    Assets.explosion[MathUtils.random(0,2)].play(1f);
                    World.getEntityHandler().getEntities().remove(e);
                    flashDurationTimer = 0; flashInterval = 0; isFlashing = true;
                }
            }
        }
    }

    /**
     * The render method.
     * @param b Every render method needs a passed in graphics batch.
     */
    @Override
    public void render(Batch b) {
        // Draw the sprite calculated by the current state its in.
        if(isFlashing && flashInterval >= flashIntervalTime) {
            b.draw(currentSprite(), x, y, width, height);
            // Draw the health above.
            renderHealth(b);
            // Draw the name above.
            Text.draw(b, name, (int)(x + (width / 2)) - ((name.length() * 5) / 2), (int)y + 40, 4);
        }
        else if (!isFlashing) {
            b.draw(currentSprite(), x, y, width, height);
            // Draw the health above.
            renderHealth(b);
            // Draw the name above.
            Text.draw(b, name, (int)(x + (width / 2)) - ((name.length() * 5) / 2), (int)y + 40, 4);
        }

    }

    /**
     * This method will render the health based off of the value.
     * @param b Every render, or sub-render method needs a passed in graphics batch.
     */
    private void renderHealth(Batch b) {
        // Simple algorithm to display the hearts properly.
        for (int i = 0; i < 10; i++) {
            if (getHealth() >= i + 1) b.draw(Assets.heart[0], (x + (i * heartSpacing)) - 10,
                    y + 33, 4, 4);
            else b.draw(Assets.heart[1], (x + (i * heartSpacing)) - 10,
                    y + 33, 4, 4);
        }
    }

    /**
     * This method defers the proper animation or sprite to return to the render method based on the state of itself.
     * @return A TextureRegion denoting the sprite that needs to be rendered.
     */
    private TextureRegion currentSprite() {
        // If the Npc is moving and on the ground...
        if(isMoving && isGrounded) {
            if(facingLeft) return animations[1].getCurrentFrame();
            else if (facingRight) return animations[0].getCurrentFrame();
        // Otherwise if they are off the ground...
        } else if (isMoving) {
            if(facingLeft) return Assets.manJump[0];
            else return Assets.manJump[1];
        // Otherwise, return the idle sprite.
        } else {
            if(facingLeft) return Assets.man[0];
            else return Assets.man[1];
        }
        return null;
    }

    /**
     * This method defines what happens when an Npc dies.
     */
    @Override
    public void die() {
        int times = MathUtils.random(1, 14);
        for(int i = 0; i < times; i++)
            World.getItemHandler().addItem(Assets.carbonSampleResource.createNew(hitBox.x + 10 + MathUtils.random(-4, 4),
                    (LightManager.highestTile[hitBox.x / Tile.TILE_SIZE] * Tile.TILE_SIZE) + 12 + MathUtils.random(-4, 4)));
        World.getEntityHandler().getPlayer().hurt(1);
    }
}
