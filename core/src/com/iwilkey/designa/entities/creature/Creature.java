package com.iwilkey.designa.entities.creature;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.tiles.Tile;

public abstract class Creature extends Entity {

    public static final float DEFAULT_SPEED = 1.05f; // 1.05f orig
    public static final int DEFAULT_CREATURE_WIDTH = 22,
        DEFAULT_CREATURE_HEIGHT = 30;

    // State
    protected boolean facingRight, facingLeft;
    protected boolean isMoving;

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
        moveX();
        if(isJumping && (jumpTimer <= jumpTime)) jump();
        else {
            isJumping = false;
            jumpTimer = 0;
            moveY();
        }

        if(jumpTimer > jumpTime - 1f && jumpTimer < jumpTime) timeInAir = 0;

        if(!isGrounded) {
            timeInAir += 0.1;
        }

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
            } else {
                isMoving = false;
            }

        } else if (xMove < 0) { // Moving left

            int tx = (int) (x + xMove + collider.x) / Tile.TILE_SIZE;
            if(!collisionWithTile(tx, (int)(y + collider.y) / Tile.TILE_SIZE) &&
                    !collisionWithTile(tx, (int)(y + collider.y + collider.height) / Tile.TILE_SIZE)) {
                x += xMove;
                facingLeft = true;
                facingRight = false;
                isMoving = true;
            } else {
                isMoving = false;
            }
        } else {
            isMoving = false;
        }

    }

    private void moveY() {

        int ty = (int) (y - gravity - collider.y - collider.height + 24) / Tile.TILE_SIZE;
        if(!collisionWithTile((int) (x + collider.x) / Tile.TILE_SIZE, ty) &&
                !collisionWithTile((int) (x + collider.x + collider.width) / Tile.TILE_SIZE, ty)){
            isGrounded = false;
            y += gravity * timeInAir;
        } else {
            isGrounded = true;

            if(timeInAir > 2.1f) {
                fallDamage();
            }

            timeInAir = 0;
            y = ty * Tile.TILE_SIZE + Tile.TILE_SIZE;
        }

    }

    public void checkFlash() {
        flashDurationTimer += 0.01f;
        flashInterval += 0.01f;

        if(flashDurationTimer >= flashTime) {
            isFlashing = false;
        }
        if(flashInterval >= flashIntervalTime * 2) {
            flashInterval = 0;
        }
    }

    private void fallDamage() {
        hurt((int)(Math.floor(timeInAir) / 1.4f)); // Fall damage

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

    protected boolean collisionWithTile(int x, int y) { return gb.getWorld().getTile(x, y).isSolid(); }

    public float getSpeed() { return speed; }
    public boolean facingLeft() { return facingLeft; }

}
