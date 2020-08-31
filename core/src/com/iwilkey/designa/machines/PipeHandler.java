package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

public class PipeHandler {

    public PipeHandler() {}

    public void tick() {

    }

    // TODO: This needs to be optimized.
    public static void render(Batch b, int x, int y, int bl, int id) {
        for(int yy = 0; yy < World.h; yy++) {
            for(int xx = 0; xx < World.w; xx++) {
                int direction = World.pipeMap[xx][yy];

                if(direction == 0 || direction == 2) {
                    b.draw(Assets.stonePipe[1], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    Tile.renderBreakLevel(b, x, y, bl, id);
                }

                if(direction == 1 || direction == 3) {
                    b.draw(Assets.stonePipe[0], x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
                    Tile.renderBreakLevel(b, x, y, bl, id);
                }
            }
        }
    }
}
