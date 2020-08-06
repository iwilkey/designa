package com.iwilkey.designa.entities.statics;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

public class OakTree extends StaticEntity {

    public OakTree(GameBuffer gb, float x, float y) {
        super(gb, x, y, Tile.TILE_SIZE * 5, Tile.TILE_SIZE * 7);

        width = 5;
        height = 7;
        model = new int[width][height];

        // Model data
        model[2][0] = 4;
        model[1][1] = 4; model[2][1] = 4; model[3][1] = 4;
        model[0][2] = 4; model[1][2] = 4; model[2][2] = 4; model[3][2] = 4; model[4][2] = 4;
        model[0][3] = 4; model[1][3] = 4; model[2][3] = 4; model[3][3] = 4; model[4][3] = 4;
        model[2][4] = 3;
        model[2][5] = 3;
        model[2][6] = 3;

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Batch b) {
        int off = Math.round(width / 2f);
        for(int rely = 0; rely < height; rely++) {
            for(int relx = -off; relx < width - off; relx++) {
                if(model[relx + off][rely] == 0) b.draw(Assets.air, (x - (relx - off)) * Tile.TILE_SIZE,
                        (y - rely) * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                if(model[relx + off][rely] == 3) b.draw(Assets.oakWood, (x - (relx - off)) * Tile.TILE_SIZE,
                        (y - rely) * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                if(model[relx + off][rely] == 4) b.draw(Assets.leaf, (x - (relx - off)) * Tile.TILE_SIZE,
                        (y - rely) * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
    }

    @Override
    public void die() {

    }

}
