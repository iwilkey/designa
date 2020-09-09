package com.iwilkey.designa.entities.creature;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.entities.creature.passive.Player;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.tiles.Tile;

public abstract class Creature extends Entity {

    public static final float DEFAULT_SPEED = 1.05f; // 1.05f orig
    public static final int DEFAULT_CREATURE_WIDTH = 22,
        DEFAULT_CREATURE_HEIGHT = 30;

    // State
    protected boolean facingRight, facingLeft;
    protected boolean isMoving, gunWielding = false;

    // Mock physics
    protected float speed;
    protected float xMove, yMove;
    protected float gravity = -3.5f;
    protected boolean isGrounded;
    protected float timeInAir = 0.0f;

    protected float jumpTimer = 0.0f;
    protected float jumpTime = 1.0f;
    protected boolean isJumping = false;

    // Rendering (Flashing)
    protected boolean isFlashing = false;
    protected float flashTime = 1.0f;
    protected float flashDurationTimer = 0;
    protected float flashIntervalTime = 0.08f;
    protected float flashInterval = 0;

    public Creature(GameBuffer gb, float x, float y, int w, int h) {
        super(gb, x, y, w, h);
        width = w;
        height = h;
        speed = DEFAULT_SPEED;
        xMove = 0; yMove = 0;
    }

    public void move() {
        if(!checkEntityCollisions(xMove, 0f)) moveX();
        else isMoving = false;
        if(isJumping && (jumpTimer <= jumpTime)) jump();
        else {
            isJumping = false;
            jumpTimer = 0;
            if(!checkEntityCollisions(0f, yMove)) moveY();
            else isMoving = false;
        }
        if(isJumping && checkEntityCollisions(0f, 0f)) y -= 8;
        if(jumpTimer > jumpTime - 1f && jumpTimer < jumpTime) timeInAir = 0;
        if(!isGrounded) timeInAir += 0.1;
        if(isFlashing) checkFlash();
    }

    private void moveX() {

        if (xMove > 0) { // Moving right

            int tx = (int) (x + xMove + collider.x + collider.width) / Tile.TILE_SIZE;
            if(!collisionWithTile(tx, (int)(y + collider.y) / Tile.TILE_SIZE) &&
                    !collisionWithTile(tx, (int)(y + collider.y + collider.height) / Tile.TILE_SIZE)) {
                x += xMove;
                facingRight = true;
                facingLeft = false;
                isMoving = true;
            } else isMoving = false;

        } else if (xMove < 0) { // Moving left
            int tx = (int) (x + xMove + collider.x) / Tile.TILE_SIZE;
            if(!collisionWithTile(tx, (int)(y + collider.y) / Tile.TILE_SIZE) &&
                    !collisionWithTile(tx, (int)(y + collider.y + collider.height) / Tile.TILE_SIZE)) {
                x += xMove;
                facingLeft = true;
                facingRight = false;
                isMoving = true;
            } else isMoving = false;
        } else isMoving = false;
    }

    private void moveY() {

        int ty = (int) (y - gravity - collider.y - collider.height + 24) / Tile.TILE_SIZE;
        if(!collisionWithTile((int) (x + collider.x) / Tile.TILE_SIZE, ty) &&
                !collisionWithTile((int) (x + collider.x + collider.width) / Tile.TILE_SIZE, ty)){
            isGrounded = false;
            y += gravity * timeInAir;
        } else {
            isGrounded = true;
            if(timeInAir > 2.1f) fallDamage();
            if(timeInAir > 0.1f) {

                // TODO: Make a sound class that does this for all sounds...
                float vol = 0;
                if(!(this instanceof Player)) {
                    float dist = com.iwilkey.designa.physics.MathUtils.distance(this, gb.getWorld().getEntityHandler().getPlayer());
                    vol = (1 / dist) * 20;
                }
                Assets.jumpLand[MathUtils.random(0,2)].play(Math.max(0.5f - vol, 0));

            }
            timeInAir = 0;
            y = ty * Tile.TILE_SIZE + Tile.TILE_SIZE;
        }

    }

    // TODO: Finish this
    private void moveUpLadder() {
        y += 0.1;
    }

    protected void checkStuck() {
        if(collisionWithTile((int) (x + 16) / Tile.TILE_SIZE, (int) y / Tile.TILE_SIZE)) {
            jump();
            y = (LightManager.highestTile[(int) x / Tile.TILE_SIZE] * Tile.TILE_SIZE) + Tile.TILE_SIZE;
        }
    }

    public void checkFlash() {
        flashDurationTimer += 0.01f;
        flashInterval += 0.01f;
        if(flashDurationTimer >= flashTime) isFlashing = false;
        if(flashInterval >= flashIntervalTime * 2) flashInterval = 0;

    }

    private void fallDamage() {
        hurt((int)(Math.floor(timeInAir) / 1.4f));
        flashDurationTimer = 0;
        flashInterval = 0;
        isFlashing = true;
    }

    public void jump() {
        isJumping = true;
        jumpTimer += 0.1;

        int ty = (int) (y + yMove + collider.y + collider.height) / Tile.TILE_SIZE;
        if(!collisionWithTile((int) (x + collider.x) / Tile.TILE_SIZE, ty) &&
                !collisionWithTile((int) (x + collider.x + collider.width) / Tile.TILE_SIZE, ty)) {
            isGrounded = false;
            float dt = timeInAir;
            if(dt == 0) dt = 1000f;
            y -= gravity * 2 * ((1 / (dt * 24)));
        }
    }

    public boolean collisionWithTile(int x, int y) { return gb.getWorld().getTile(x, y).isSolid(); }

    public float getSpeed() { return speed; }
    public boolean facingLeft() { return facingLeft; }
    public boolean isJumping() { return isJumping; }
    public boolean isGrounded() { return isGrounded; }

}
