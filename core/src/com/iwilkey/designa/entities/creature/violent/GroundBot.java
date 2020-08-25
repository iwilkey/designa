package com.iwilkey.designa.entities.creature.violent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.building.NpcBuildingHandler;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.entities.creature.Creature;
import com.iwilkey.designa.entities.creature.Enemy;
import com.iwilkey.designa.gfx.Animation;
import com.iwilkey.designa.tiles.Tile;

public class GroundBot extends Enemy {

    private final Animation[] animations;
    private final NpcBuildingHandler buildingHandler;

    public GroundBot(GameBuffer gb, float x, float y) {
        super(gb, x, y, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT);
        facingLeft = true;
        speed = MathUtils.random(DEFAULT_SPEED - 0.5f, DEFAULT_SPEED - 0.4f);

        collider.width = width - 9;
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;

        animations = new Animation[2];
        animations[0] = new Animation(100, Assets.groundBotRight);
        animations[1] = new Animation(100, Assets.groundBotLeft);

        buildingHandler = new NpcBuildingHandler(gb, this);
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
            if (checkObstacle()) {
                blockedDestroyMethod(e);
            }
            timer = 0;
        }
    }

    private void blockedPlaceMethod(Entity e) {
        if(!isJumping && isGrounded) jump();
        if (facingLeft) buildingHandler.placeBlock(Tile.dirtTile.getID(), (int) ((x) / Tile.TILE_SIZE), (int) y / Tile.TILE_SIZE);
        else buildingHandler.placeBlock(Tile.dirtTile.getID(), (int) ((x + 16) / Tile.TILE_SIZE), (int) y / Tile.TILE_SIZE);
        if(x - e.getX() > -8f && x - e.getX() < 8f) ((Creature)(e)).jump();
    }

    private void blockedDestroyMethod(Entity e) {
        if (facingLeft) {
            if(buildingHandler.damageBlock((int) ((x - 4) / Tile.TILE_SIZE), (int) ((y + 16) / Tile.TILE_SIZE)) == -1) {
                buildingHandler.damageBlock((int) ((x - 4) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));
                for(int i=0;i <30;i++) buildingHandler.damageBlock((int) ((x) / Tile.TILE_SIZE), (int) ((y + 48) / Tile.TILE_SIZE));
            }
        }
        else  {
            if(buildingHandler.damageBlock((int) ((x + 20) / Tile.TILE_SIZE), (int) ((y + 16) / Tile.TILE_SIZE)) == -1) {
                buildingHandler.damageBlock((int) ((x + 20) / Tile.TILE_SIZE), (int) ((y + 32) / Tile.TILE_SIZE));
                for(int i=0;i <30;i++) buildingHandler.damageBlock((int) ((x + 20) / Tile.TILE_SIZE), (int) ((y + 48) / Tile.TILE_SIZE));
            }
        }
        if(!isJumping && isGrounded) jump();
    }

    private boolean checkObstacle() {
        if(facingLeft && collisionWithTile((int) (x - 3) / Tile.TILE_SIZE, (int) y / Tile.TILE_SIZE)) return true;
        if(facingLeft && collisionWithTile((int) (x - 3) / Tile.TILE_SIZE, (int) ((y + 16) / Tile.TILE_SIZE))) return true;
        if(isJumping && facingLeft && collisionWithTile((int) (x - 3) / Tile.TILE_SIZE, (int) ((y + 32) / Tile.TILE_SIZE))) return true;

        if(facingRight && collisionWithTile((int) (x + 20) / Tile.TILE_SIZE, (int) y / Tile.TILE_SIZE)) return true;
        if(facingRight && collisionWithTile((int) (x + 20) / Tile.TILE_SIZE, (int) ((y + 16) / Tile.TILE_SIZE))) return true;
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
