package com.iwilkey.designa.entities.creature.violent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.building.NpcBuildingHandler;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.entities.creature.violent.Enemy;
import com.iwilkey.designa.gfx.Animation;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.awt.*;

/**

 This class defines the TerraBot. They are ruthless cyborgs that will terraform the land at any cost to get to you.
 Cons: Can't fly and they move relatively slow, and have no lasers.
 Pros: High stamina; they will hunt you relentlessly until you and your team are found and killed.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

// Note: This object is an Enemy.
public class TerraBot extends Enemy {

    /**
     * Global vars
     */

    // Final vars
    private final Animation[] animations;
    private final NpcBuildingHandler buildingHandler;

    // Integers
    int heartSpacing = 4;

    // Longs (timers)
    long timer = 0, checkLimit = 16;

    /**
     * TerraBot constructor.
     * @param gb The TerraBot needs the GameBuffer.
     * @param x The x spawn location of the bot.
     * @param y The y spawn location of the bot.
     */
    public TerraBot(GameBuffer gb, float x, float y) {
        // Invoke the Enemy constructor.
        super(gb, x, y, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT);

        // Give the bot the power to alter the world around them.
        buildingHandler = new NpcBuildingHandler(gb);

        // Set the face.
        facingLeft = true;

        damagePotential = 2;

        // Set the speed between the values below.
        speed = MathUtils.random(DEFAULT_SPEED - 0.9f, DEFAULT_SPEED - 0.8f);

        // Set the collider.
        collider.width = width - 12;
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;

        hitBox = new Rectangle(collider.x, collider.y, collider.width, collider.height);

        // Initialize animations.
        animations = new Animation[2];
        animations[0] = new Animation(100, Assets.groundBotRight);
        animations[1] = new Animation(100, Assets.groundBotLeft);
    }

    /**
     * TerraBot tick method. I like to think of it as the brain.
     */
    @Override
    public void tick() {
        // Tick the animations forward.
        for(Animation anim : animations) anim.tick();

        // Move.
        move();

        // Endlessly target the player.
        targetEntity(World.getEntityHandler().getPlayer());
    }

    /**
     * This method tells the bot how to target the entity passed in.
     * @param e The entity targeted.
     */
    private void targetEntity(Entity e) {
        // If the player is to the left of then, move left.
        if((x - e.getX()) > 0) {
            face("l");
            xMove = -speed;
        // Otherwise, move right.
        } else if (x - e.getX() < 0) {
            face("r");
            xMove = speed;
        // But if the bot is on top of the player, don't move at all.
        } else xMove = 0;

        // Increment the timer and once it hits the check limit, allow the bot to alter the world.
        // This just keeps it from happening too fast and making it unrealistic.
        timer++;
        if(timer > checkLimit) {
            // If the bot is stuck...
            if (checkObstacle(e)) actAI(e); // Allow it to act.
            timer = 0;
        }

        hitBox.x = (int)x; hitBox.y = (int)y;
    }

    /**
     * This method defines how a TerraBot is allowed to react to an obstacle.
     * @param e The entity targeted.
     */
    private void actAI(Entity e) {
        // If the bot is facing left...
        if (facingLeft) {
            // And there is a block in front of it...
            if(buildingHandler.damageBlock((int) ((x - 4) / Tile.TILE_SIZE), (int) ((y + 16) / Tile.TILE_SIZE)) == -1) {
                // Damage the block...
                buildingHandler.damageBlock((int) ((x - 4) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));
                // And, damage the block above it so that it doesn't get stuck when jumping.
                for(int i=0;i<30;i++) buildingHandler.damageBlock((int) ((x) / Tile.TILE_SIZE), (int) ((y + 48) / Tile.TILE_SIZE));
            }
        // Otherwise, the bot is facing right.
        } else  {
            // If there is a block in front of it...
            if(buildingHandler.damageBlock((int) ((x + 20) / Tile.TILE_SIZE), (int) ((y + 16) / Tile.TILE_SIZE)) == -1) {
                // Do the same thing that was done for the left side, but for the right.
                buildingHandler.damageBlock((int) ((x + 20) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));
                for(int i=0;i<30;i++) buildingHandler.damageBlock((int) ((x + 20) / Tile.TILE_SIZE), (int) ((y + 48) / Tile.TILE_SIZE));
            }
        }

        // Jump.
        if(!isJumping && isGrounded) jump();

        // If, while its jumping it sees a block above previously out of range...
        if(isJumping && collisionWithTile((int) (x + 16) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE)))
            // Damage it.
            buildingHandler.damageBlock((int) ((x + 16) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));

        // If the player is higher than it...
        if((y - e.getY()) < 0) {
            // Damage blocks below until they are found...
            if(isJumping && collisionWithTile((int) (x + 32) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE)))
                buildingHandler.damageBlock((int) ((x + 32) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));
            // Or...
            else {
                // Jump.
                if(!isJumping && isGrounded) jump();
                // Place a block underneath to build up to the player.
                buildingHandler.placeBlock(Assets.dirtTile.getID(), (int) ((x + 16) / Tile.TILE_SIZE), (int) y / Tile.TILE_SIZE);
            }
        // Otherwise, the player is under the bot.
        } else if ((y - e.getY()) > 0) {
            // So, dig down until found.
            if(isJumping && collisionWithTile((int) (x + 16) / Tile.TILE_SIZE, (int) ((y - 8) / Tile.TILE_SIZE)))
                buildingHandler.damageBlock((int) ((x + 16) / Tile.TILE_SIZE), (int) ((y - 8) / Tile.TILE_SIZE));
        }
    }

    /**
     * This method defines how a TerraBot can check for an obstruction in its path.
     * @param e The entity targeted.
     * @return Whether or not there is an obstruction.
     */
    private boolean checkObstacle(Entity e) {
        // Don't worry about all the specifics here, just trust it's checking in all directions. Feel free to change and experiment, though.
        if(facingLeft && collisionWithTile((int) (x - 3) / Tile.TILE_SIZE, (int) y / Tile.TILE_SIZE)) return true;
        if(facingLeft && collisionWithTile((int) (x - 3) / Tile.TILE_SIZE, (int) ((y + 16) / Tile.TILE_SIZE))) return true;
        if(isJumping && facingLeft && collisionWithTile((int) (x - 3) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE))) return true;
        if(facingRight && collisionWithTile((int) (x + 20) / Tile.TILE_SIZE, (int) y / Tile.TILE_SIZE)) return true;
        if(facingRight && collisionWithTile((int) (x + 20) / Tile.TILE_SIZE, (int) ((y + 16) / Tile.TILE_SIZE))) return true;
        if((y - e.getY()) < 0) if(isJumping && collisionWithTile((int) (x + 32) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE))) return true;
        if((y - e.getY()) > 0) if(isJumping && collisionWithTile((int) (x + 16) / Tile.TILE_SIZE, (int) ((y - 8) / Tile.TILE_SIZE))) return true;
        return isJumping && facingRight && collisionWithTile((int) (x + 20) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE));
    }

    /**
     * This method will take in an String value to defer what direction the TerraBot should face.
     * @param f The direction the player should face.
     */
    private void face(String f) {
        // "l" for left, anything else for right.
        if(f.equals("l")) {
            facingLeft = true; facingRight = false;
        } else {
            facingRight = true; facingLeft = false;
        }
    }

    /**
     * The TerraBot render method.
     * @param b Every render method needs the graphics batch.
     */
    @Override
    public void render(Batch b) {
        // Draw the correct sprite.
        b.draw(currentSprite(), x, y, width, height);

        // Draw the health.
        drawHealth(b);

        // Draw the name.
        Text.draw(b, "TerraBot", (int)(x + (width / 2)) - (("TerraBot".length() * 5) / 2), (int)y + 40, 4);
    }

    /**
     * This is a sub-render method for a TerraBot that will draw the health of itself.
     * @param b Ever render, or sub-render method needs the graphics batch.
     */
    private void drawHealth(Batch b) {
        // Simple algorithm for drawing the health of an entity.
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
        // If the bot is facing right...
        if(facingRight) return animations[1].getCurrentFrame();
        // Otherwise...
        else return animations[0].getCurrentFrame();
    }

    /**
     * This method defines what happens when a TerraBot dies.
     */
    @Override
    public void die() {
        int times = MathUtils.random(1, 14);
        for(int i = 0; i < times; i++)
            World.getItemHandler().addItem(Assets.carbonSampleResource.createNew(hitBox.x + 10 + MathUtils.random(-4, 4),
                    (LightManager.highestTile[hitBox.x / Tile.TILE_SIZE] * Tile.TILE_SIZE) + 12 + MathUtils.random(-4, 4)));
        World.particleHandler.startParticle("large-explosion", hitBox.x, hitBox.y);
        Assets.explosion[MathUtils.random(0,2)].play(0.75f);
    }
}
