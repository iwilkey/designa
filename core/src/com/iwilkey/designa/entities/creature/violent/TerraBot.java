package com.iwilkey.designa.entities.creature.violent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.building.NpcBuildingHandler;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.entities.creature.Enemy;
import com.iwilkey.designa.gfx.Animation;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.tiles.Tile;

// TerraBots are ruthless cyborgs that will terraform the land at any cost to get to you.
// Cons: Can't fly and they move relatively slow. They also have no lasers.
// Pros: High stamina; they will hunt you relentlessly until you are found and killed

public class TerraBot extends Enemy {

    private final Animation[] animations;
    private final NpcBuildingHandler buildingHandler;

    int heartSpacing = 4;

    public TerraBot(GameBuffer gb, float x, float y) {
        super(gb, x, y, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT);

        facingLeft = true;
        speed = MathUtils.random(DEFAULT_SPEED - 0.9f, DEFAULT_SPEED - 0.8f);

        collider.width = width - 12;
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;

        animations = new Animation[2];
        animations[0] = new Animation(100, Assets.groundBotRight);
        animations[1] = new Animation(100, Assets.groundBotLeft);

        buildingHandler = new NpcBuildingHandler(gb);
    }

    @Override
    public void tick() {
        for(Animation anim : animations) anim.tick();
        move();
        targetEntity(gb.getWorld().getEntityHandler().getPlayer());
    }

    long timer = 0, checkLimit = 16;
    private void targetEntity(Entity e) {
        if((x - e.getX()) > 0) {
            face("l");
            xMove = -speed;
        } else if (x - e.getX() < 0) {
            face("r");
            xMove = speed;
        } else xMove = 0;

        timer++;
        if(timer > checkLimit) {
            if (checkObstacle(e)) {
                actAI(e);
            }
            timer = 0;
        }
    }

    private void actAI(Entity e) {
        if (facingLeft) {
            if(buildingHandler.damageBlock((int) ((x - 4) / Tile.TILE_SIZE), (int) ((y + 16) / Tile.TILE_SIZE)) == -1) {
                buildingHandler.damageBlock((int) ((x - 4) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));
                for(int i=0;i<30;i++) buildingHandler.damageBlock((int) ((x) / Tile.TILE_SIZE), (int) ((y + 48) / Tile.TILE_SIZE));
            }
        }
        else  {
            if(buildingHandler.damageBlock((int) ((x + 20) / Tile.TILE_SIZE), (int) ((y + 16) / Tile.TILE_SIZE)) == -1) {
                buildingHandler.damageBlock((int) ((x + 20) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));
                for(int i=0;i<30;i++) buildingHandler.damageBlock((int) ((x + 20) / Tile.TILE_SIZE), (int) ((y + 48) / Tile.TILE_SIZE));
            }
        }
        if(!isJumping && isGrounded) jump();
        if(isJumping && collisionWithTile((int) (x + 16) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE)))
            buildingHandler.damageBlock((int) ((x + 16) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));
        if((y - e.getY()) < 0) {
            if(isJumping && collisionWithTile((int) (x + 32) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE)))
                buildingHandler.damageBlock((int) ((x + 32) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));
            else {
                if(!isJumping && isGrounded) jump();
                buildingHandler.placeBlock(Tile.dirtTile.getID(), (int) ((x + 16) / Tile.TILE_SIZE), (int) y / Tile.TILE_SIZE);
            }
        } else if ((y - e.getY()) > 0) {
            if(isJumping && collisionWithTile((int) (x + 16) / Tile.TILE_SIZE, (int) ((y - 8) / Tile.TILE_SIZE)))
                buildingHandler.damageBlock((int) ((x + 16) / Tile.TILE_SIZE), (int) ((y - 8) / Tile.TILE_SIZE));
        }
    }

    private boolean checkObstacle(Entity e) {
        if(facingLeft && collisionWithTile((int) (x - 3) / Tile.TILE_SIZE, (int) y / Tile.TILE_SIZE)) return true;
        if(facingLeft && collisionWithTile((int) (x - 3) / Tile.TILE_SIZE, (int) ((y + 16) / Tile.TILE_SIZE))) return true;
        if(isJumping && facingLeft && collisionWithTile((int) (x - 3) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE))) return true;
        if(facingRight && collisionWithTile((int) (x + 20) / Tile.TILE_SIZE, (int) y / Tile.TILE_SIZE)) return true;
        if(facingRight && collisionWithTile((int) (x + 20) / Tile.TILE_SIZE, (int) ((y + 16) / Tile.TILE_SIZE))) return true;
        if((y - e.getY()) < 0) if(isJumping && collisionWithTile((int) (x + 32) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE))) return true;
        if((y - e.getY()) > 0) if(isJumping && collisionWithTile((int) (x + 16) / Tile.TILE_SIZE, (int) ((y - 8) / Tile.TILE_SIZE))) return true;
        return isJumping && facingRight && collisionWithTile((int) (x + 20) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE));
    }

    private void face(String f) {
        if(f.equals("l")) {
            facingLeft = true; facingRight = false;
        } else {
            facingRight = true; facingLeft = false;
        }
    }

    @Override
    public void render(Batch b) {
        b.draw(currentSprite(), x, y, width, height);

        for (int i = 0; i < 10; i++) {
            if (getHealth() >= i + 1) {
                // Down, to the left
                b.draw(Assets.heart[0], (x + (i * heartSpacing)) - 10,
                        y + 33, 4, 4);
            } else {
                b.draw(Assets.heart[1], (x + (i * heartSpacing)) - 10,
                        y + 33, 4, 4);
            }
        }

        Text.draw(b, "TerraBot", (int)(x + (width / 2)) - (("TerraBot".length() * 5) / 2), (int)y + 40, 4);
    }

    private TextureRegion currentSprite() {
        if(facingRight) return animations[1].getCurrentFrame();
        else return animations[0].getCurrentFrame();
    }

    @Override
    public void die() {
        // Drop carbon samples
        // Play particle effect
    }
}
