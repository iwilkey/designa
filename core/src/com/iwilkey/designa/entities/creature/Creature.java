package com.iwilkey.designa.entities.creature;

import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

/**

 This is an abstract class that defines the common facilities of a Designa creature.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

// Note: This object is an Entity.
public abstract class Creature extends Entity {

    /**
     * Global vars
     */

    // Final vars
    public static final float DEFAULT_SPEED = 1.05f; // 1.05f orig
    public static final int DEFAULT_CREATURE_WIDTH = 22,
        DEFAULT_CREATURE_HEIGHT = 30;

    // Physical State
    protected boolean facingRight, facingLeft;
    protected boolean isMoving, gunWielding = false;

    // Mock physics
    protected float speed;
    protected float xMove, yMove;
    protected float gravity = -3.5f;
    protected boolean isGrounded;
    protected float timeInAir = 0.0f;
        // Jumping
        protected float jumpTimer = 0.0f;
        protected float jumpTime = 1.0f;
        protected boolean isJumping = false;

    // Rendering (Flashing)
    protected boolean isFlashing = false;
    protected float flashTime = 1.0f;
    protected float flashDurationTimer = 0;
    protected float flashIntervalTime = 0.08f;
    protected float flashInterval = 0;

    /**
     * The Creature constructor.
     * @param gb The Creature class needs the GameBuffer.
     * @param x The x spawn location of the Creature.
     * @param y The y spawn location of the Creature.
     * @param w The width of the Creature.
     * @param h The height of the Creature.
     */
    public Creature(GameBuffer gb, float x, float y, int w, int h) {
        // Invoke the Entity constructor.
        super(gb, x, y, w, h);

        // Set some physical initial values.
        width = w; height = h;
        speed = DEFAULT_SPEED;
        xMove = 0; yMove = 0;
    }

    /**
     * This method defines how a Creature will or can move.
     */
    public void move() {
        // If there is nothing there to stop it, move the creature along the xAxis (per user request)
        if(!checkEntityCollisions(xMove, 0f)) moveX();
        // Otherwise, the Creature is not moving.
        else isMoving = false;

        // If the Creature is not jumping and allowed to jump...
        if(isJumping && (jumpTimer <= jumpTime)) jump(); // Let it jump.
        // Otherwise...
        else {
            // Set jumping to false
            isJumping = false;
            // Reset the jumpTimer.
            jumpTimer = 0;
            // Enforce gravity if there is nothing under the Creature.
            if(!checkEntityCollisions(0f, yMove)) moveY();
            // Otherwise, the Creature is not moving.
            else isMoving = false;
        }

        // If the Creature is jumping and collides with an object above itself, don't continue to travel upwards.
        if(isJumping && checkEntityCollisions(0f, 0f)) y -= 8;

        // If the jumpTimer is up, reset timeInAir. This is used for the physics engine.
        if(jumpTimer > jumpTime - 1f && jumpTimer < jumpTime) timeInAir = 0;

        // If the Creature is not grounded, keep track of how long they've been in the air. This is used for the physics engine.
        if(!isGrounded) timeInAir += 0.1;

        // If the Creature is flashing, check if it needs to become visible or invisible yet.
        if(isFlashing) checkFlash();
    }

    /**
     * This method defines how a Creature can move along the x-axis.
     */
    private void moveX() {
        // If the Creature requests to move right...
        if (xMove > 0) {
            // Create a temporary x-value to see where the player will be if another tick passes...
            int tx = (int) (x + xMove + collider.x + collider.width) / Tile.TILE_SIZE;
            // If there isn't a collision between the player and a tile using the temporary x-value...
            if(!collisionWithTile(tx, (int)(y + collider.y) / Tile.TILE_SIZE) &&
                    !collisionWithTile(tx, (int)(y + collider.y + collider.height) / Tile.TILE_SIZE)) {
                // Increment the Creatures x-value by the value of xMove.
                x += xMove;
                // Change the direction in which the Creature faces.
                facingRight = true; facingLeft = false;
                // Set isMoving to true to reflect how the Creature is moving.
                isMoving = true;
            // Otherwise, there will be a collision, so don't allow the Creature to move.
            } else isMoving = false;
        // Otherwise, the Creature requests to move left...
        } else if (xMove < 0) {
            // Same algorithm as what was done above.
            int tx = (int) (x + xMove + collider.x) / Tile.TILE_SIZE;
            if(!collisionWithTile(tx, (int)(y + collider.y) / Tile.TILE_SIZE) &&
                    !collisionWithTile(tx, (int)(y + collider.y + collider.height) / Tile.TILE_SIZE)) {
                x += xMove;
                facingLeft = true; facingRight = false;
                isMoving = true;
            } else isMoving = false;
        // Otherwise xMove is zero, and the player is not requesting to move.
        } else isMoving = false;
    }

    /**
     * This method defines how a Creature will tend to move along the y-axis.
     * Note: Since Designa is a side-view 2D game, the y-movement will be reserved exclusively for the effect of gravity.
     */
    private void moveY() {
        // Create a temporary y-value to see where the player will be if another tick passes...
        int ty = (int) (y - gravity - collider.y - collider.height + 24) / Tile.TILE_SIZE;
        // If there is nothing to stop the Creature...
        if(!collisionWithTile((int) (x + collider.x) / Tile.TILE_SIZE, ty) &&
                !collisionWithTile((int) (x + collider.x + collider.width) / Tile.TILE_SIZE, ty)){
            // The Creature must not be on the ground.
            isGrounded = false;
            // Accelerate the Creature downward using the gravitational constant and the increasing timeInAir.
            y += gravity * timeInAir;
        // Otherwise, there is something to stop the player from falling.
        } else {
            // The Creature must be on the ground.
            isGrounded = true;
            // If the Player has been in the air longer than 2.1f units of time, invoke the fallDamage() method.
            if(timeInAir > 2.1f) fallDamage();
            // If the timeInAir is greater than 0... (Only reason is to keep this sound from continuing when the player is grounded)
            if(timeInAir > 0.1f) {

                /*
                    * DEVELOPMENT NOTE
                    * The idea here is to create the effect of farther sounds being silenced by a factor of distance.
                    * It doesn't work exactly right yet, but it will be implemented further down the road.

                float vol = 0;
                if(!(this instanceof Player)) {
                    float dist = com.iwilkey.designa.physics.MathUtils.distance(this, World.getEntityHandler().getPlayer());
                    vol = (1 / dist) * 20;
                }

                 */
                // Play a variant of the landing SFX.
                Assets.jumpLand[MathUtils.random(0,2)].play(0.5f);

            }

            // Reset the timeInAir var.
            timeInAir = 0;

            // Replace the y value to reflect the top of the tile fallen on.
            y = ty * Tile.TILE_SIZE + Tile.TILE_SIZE;
        }

    }

    /**
     * This method defines how a Creature can take fall damage. It is deferred by the amount of time in the air.
     */
    private void fallDamage() {
        // Hurt the player based on the time in air divided by a softening constant.
        hurt((int)(Math.floor(timeInAir) / 1.4f));

        // Reset the flash state. TODO: Make this happen every time the player is hurt.
        flashDurationTimer = 0; flashInterval = 0; isFlashing = true;
    }

    /**
     * This method defines how a Creature can check if it needs to be invisible or visible based on the flash timer.
     */
    public void checkFlash() {
        // Pretty straight forward algorithm. Might as well leave alone just in case.
        flashDurationTimer += 0.01f; flashInterval += 0.01f;
        if(flashDurationTimer >= flashTime) isFlashing = false;
        if(flashInterval >= flashIntervalTime * 2) flashInterval = 0;

    }

    /**
     * This method defines how a Creature can preform a jump.
     * Note: My algorithm is to split this motion up into two different motions: the up and down.
     * This method is the up motion (Initial impulse force), moveY() is the movement back down.
     */
    public void jump() {
        isJumping = true;
        // Increment the jumpTimer.
        jumpTimer += 0.1;

        // Create a temporary y-value to see where the player will be if another tick passes...
        int ty = (int) (y + yMove + collider.y + collider.height) / Tile.TILE_SIZE;
        // If there is no collision with tile detected...
        if(!collisionWithTile((int) (x + collider.x) / Tile.TILE_SIZE, ty) &&
                !collisionWithTile((int) (x + collider.x + collider.width) / Tile.TILE_SIZE, ty)) {
            // The Creature must not be grounded.
            isGrounded = false;
            // Init a delta-time float which starts at the timeInAir.
            float dt = timeInAir;
            // If the jump has just started, make the impulse force relatively strong.
            if(dt == 0) dt = 1000f;
            // Change y based off of the inverse of dt multiplied by the gravitational constant. (This is the up motion)
            y -= gravity * 2 * ((1 / (dt * 24)));
        }
    }

    /**
     * This method will defer whether or not a tile is able to be passed through or not.
     * @param x The x location of the tile.
     * @param y The y location of the tile.
     * @return If the tile is solid at that location or not.
     */
    public boolean collisionWithTile(int x, int y) {
        return World.getTile(x, y).isSolid();
    }

    /**
     * This method defines how to check if itself is stuck inside of a tile.
     * Note: This was a problem when developing the Npcs as they tended to get stuck in tiles often. This is by
     * no means the best way to solve the issue, but it works crudely and that's good enough for pre-alpha XD.
     */
    protected void checkStuck() {
        // Pretty straight forward algorithm. Might as well leave alone just in case.
        if(collisionWithTile((int) (x + 16) / Tile.TILE_SIZE, (int) y / Tile.TILE_SIZE)) {
            jump();
            y = (LightManager.highestTile[(int) x / Tile.TILE_SIZE] * Tile.TILE_SIZE) + Tile.TILE_SIZE;
        }
    }

    /*
     * GETTERS AND SETTERS
     */

    /**
     * Speed getter.
     * @return The speed.
     */
    public float getSpeed() { return speed; }

    /**
     * Facing left getter.
     * @return If the player is currently facing left.
     */
    public boolean facingLeft() { return facingLeft; }

    /**
     * Is jumping getter.
     * @return If the player is currently jumping.
     */
    public boolean isJumping() { return isJumping; }

    /**
     * Is grounded getter.
     * @return If the player is currently on the ground.
     */
    public boolean isGrounded() { return isGrounded; }

}
