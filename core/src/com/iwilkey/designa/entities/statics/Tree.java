package com.iwilkey.designa.entities.statics;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

public class Tree extends StaticEntity {

    TextureRegion texture;

    // Mock physics
    float gravity = -3.5f;
    float timeInAir = 0f;

    public Tree(GameBuffer gb, float x, float y) {
        super(gb, x, y, Tile.TILE_SIZE * 4, Tile.TILE_SIZE * 16);

        this.x -= (Tile.TILE_SIZE * 2) - (Tile.TILE_SIZE / 2f);

        int i = MathUtils.random(0, 2);
        texture = Assets.trees[i];

        collider.width = width - (int)(width / 1.2f);
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;

        health = MathUtils.random(35, 40);
    }

    @Override
    public void tick() {
        if(!active) {
            yMove();
        }
    }

    private void yMove() {
        timeInAir += 0.02f;
        y = y + gravity * timeInAir;
    }

    @Override
    public void render(Batch b) {
        b.draw(texture, x, y, width, height);
        // renderCollider(b);
    }

    @Override
    public void hurt(int amt) {
        if(!Inventory.active) {
            health -= amt;

            if (MathUtils.random(0, 2) == 2) {
                int yy = MathUtils.random(50, 200);
                int xx = MathUtils.random(-4, 26);
                World.getItemHandler().addItem(Assets.barkResource.createNew((int) x + 16 + xx, (int) y + yy));
            }

            Assets.treeHit[MathUtils.random(0, 2)].play();

            if (health <= 0) {
                active = false;
                die();
            }
        }

    }

    @Override
    public void die() {
        if(health > -1) {
            Assets.treeFall[MathUtils.random(0,2)].play();
            int spawnAmount = MathUtils.random(2, 8);
            for(int i = 0; i < spawnAmount; i++) {
                int yy = MathUtils.random(50, 200);
                int xx = MathUtils.random(-4, 26);
                World.getItemHandler().addItem(Assets.barkResource.createNew((int)x + 16 + xx, (int)y + yy));
            }
        }
    }
}
