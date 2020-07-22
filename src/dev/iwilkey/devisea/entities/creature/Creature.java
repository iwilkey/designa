package dev.iwilkey.devisea.entities.creature;

import dev.iwilkey.devisea.AppBuffer;
import dev.iwilkey.devisea.entities.Entity;
import dev.iwilkey.devisea.tiles.Tile;

public abstract class Creature extends Entity {
	
	// Set default speed and size of all creatures
	public static final float DEFAULT_SPEED = 1.05f;
	public static final int DEFAULT_CREATURE_WIDTH = 22,
			DEFAULT_CREATURE_HEIGHT = 30;
	
	protected float speed;
	protected float xMove, yMove;
	protected boolean facingRight, facingLeft;
	protected boolean isMoving;
	
	// Mock physics
	private float gravity = 3.5f;
	protected boolean isGrounded;
	protected float timeInAir = 0.0f;
	
	protected float jumpTimer = 0.0f;
	protected float jumpTime = 1.0f;
	protected boolean isJumping = false;
	
	public Creature(AppBuffer ab, float x, float y, int w, int h) {
		super(ab, x, y, w, h);
		
		width = w;
		height = h;
		
		speed = DEFAULT_SPEED;
		xMove = 0; yMove = 0; // yMove will be determined by physics
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
		
	}
	
	public void moveX() {
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
	
	public void moveY() {
		
		int ty = (int) (y + gravity + collider.y + collider.height) / Tile.TILE_SIZE;
		if(!collisionWithTile((int) (x + collider.x) / Tile.TILE_SIZE, ty) &&
			!collisionWithTile((int) (x + collider.x + collider.width) / Tile.TILE_SIZE, ty)){
				isGrounded = false;
				y += gravity * timeInAir;
		} else {
			isGrounded = true;
			timeInAir = 0;
			y = ty * Tile.TILE_SIZE - collider.y - collider.height - 1;
		}
	}
	
	public void jump() {
		isJumping = true;
		jumpTimer += 0.1;
		
		int ty = (int) (y + yMove + collider.y) / Tile.TILE_SIZE;
		if(!collisionWithTile((int) (x + collider.x) / Tile.TILE_SIZE, ty) &&
				!collisionWithTile((int) (x + collider.x + collider.width) / Tile.TILE_SIZE, ty)) {
			isGrounded = false;
			float dt = timeInAir;
			if(dt == 0) dt = 1000f;
			y -= gravity * 2 * ((1 / (dt * 24)));
		}

	}
	
	protected boolean collisionWithTile(int x, int y) {
		return ab.getWorld().getTile(x, y).isSolid();
	}

	// Getters and setters
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getxMove() {
		return xMove;
	}

	public void setxMove(float xMove) {
		this.xMove = xMove;
	}

	public float getyMove() {
		return yMove;
	}

	public void setyMove(float yMove) {
		this.yMove = yMove;
	}

}
