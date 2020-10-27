package dev.iwilkey.designa.entity.creature;

import com.badlogic.gdx.Gdx;

import dev.iwilkey.designa.entity.Entity;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.world.World;

public abstract class Creature extends Entity {

    public static final float DEFAULT_SPEED = 1.15f;
    public static final int DEFAULT_CREATURE_WIDTH = 22,
        DEFAULT_CREATURE_HEIGHT = 30;

    public boolean facingRight,
        isMoving;

    // Physics
    public final float GRAVITY = -6.5f;
    public float speed;
    public float xMove, yMove,
        timeInAir;
    public boolean isGrounded;
        // Jumping
        protected float jumpTimer = 0.0f;
        protected float jumpTime = 1.0f;
        protected boolean isJumping = false;

    public Creature(World world, float x, float y) {
        super(world, x, y, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT);
        speed = DEFAULT_SPEED;
        xMove = 0; yMove = 0;
    }

    public void move() {
        if(xMove != 0) moveX();
        else isMoving = false;
        if(isJumping && (jumpTimer <= jumpTime)) jump();
        else {
            isJumping = false;
            jumpTimer = 0;
            moveY();
        }
        if(jumpTimer > jumpTime - 1f && jumpTimer < jumpTime) timeInAir = 0;
        if(!isGrounded) timeInAir += Gdx.graphics.getDeltaTime();
    }

    int tx;
    private void moveX() {
        if(xMove < 0) { // Move left
            tx = (int)(collider.x + xMove);
            if(tx <= 0) {
                isMoving = false;
                return;
            }
            if (!collisionWithTile(tx / Tile.TILE_SIZE, (int)y / Tile.TILE_SIZE) &&
                    !collisionWithTile(tx / Tile.TILE_SIZE, ((int)y + collider.height) / Tile.TILE_SIZE)) {
                x += xMove;
                isMoving = true;
                facingRight = false;
            } else isMoving = false;

        } else if (xMove > 0) {
            tx = (int)(x + xMove);
            if(tx >= (world.WIDTH * Tile.TILE_SIZE) - (5 * Tile.TILE_SIZE)) {
                isMoving = false;
                return;
            }
            if (!collisionWithTile((tx + collider.width + 8) / Tile.TILE_SIZE, (int)y / Tile.TILE_SIZE) &&
                    !collisionWithTile((tx + collider.width + 8) / Tile.TILE_SIZE, ((int)y + collider.height) / Tile.TILE_SIZE)) {
                x += xMove;
                isMoving = true;
                facingRight = true;
            } else isMoving = false;
        }
        collider.x = (int)x + 6;
    }

    int ty;
    private void moveY() {
        ty = (int)(collider.y + GRAVITY);
        if(ty <= -6) {
            isMoving = false;
            isGrounded = true;
            timeInAir = 0;
            return;
        }
        if(!collisionWithTile((int)(x + 6) / Tile.TILE_SIZE, ty / Tile.TILE_SIZE) &&
                !collisionWithTile((int)(x + collider.width + 6) / Tile.TILE_SIZE, ty / Tile.TILE_SIZE)) {
            isGrounded = false;
            y += GRAVITY * (timeInAir * 2);
        } else {
            isGrounded = true;
            timeInAir = 0;
            y = (((int)((y + 8) / Tile.TILE_SIZE)) * Tile.TILE_SIZE);
        }
        collider.y = (int)y;
    }

    float dt = 0;
    public void jump() {
        isJumping = true;
        jumpTimer += 0.15f;
        ty = (int)(y - GRAVITY);
        if(!collisionWithTile((int)(x + (collider.width / 2f) + 4) / Tile.TILE_SIZE, ty / Tile.TILE_SIZE) &&
            !collisionWithTile((int)(x + (collider.width / 2f) + 4) / Tile.TILE_SIZE, (ty + collider.height - 6) / Tile.TILE_SIZE)) {
            isGrounded = false;
            dt = timeInAir;
            if(dt == 0) dt = 1000f;
            y -= (GRAVITY / 2) * ((1 / (dt * 40)));
        } else {
            isJumping = false;
            collider.y = (int)y;
            return;
        }
        collider.y = (int)y;
    }

    public boolean collisionWithTile(int x, int y) { return world.getTile(x, y).isSolid(); }

}
