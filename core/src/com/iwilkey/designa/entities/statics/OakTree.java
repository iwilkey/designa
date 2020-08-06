package com.iwilkey.designa.entities.statics;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

public class OakTree extends StaticEntity {

    // I'll need to describe a tree based out of tiles.

    private int trunkHeight = 4;

    public OakTree(GameBuffer gb, float x, float y) {
        super(gb, x, y, Tile.TILE_SIZE * 6, Tile.TILE_SIZE * 6);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Batch b) {

    }

    @Override
    public void die() {

    }

}
