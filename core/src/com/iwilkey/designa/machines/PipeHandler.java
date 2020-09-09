package com.iwilkey.designa.machines;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

public class PipeHandler {

    public PipeHandler() {}

    public void tick() {}

    // TODO: This needs to be optimized.
    public static void render(Batch b, int x, int y, int bl, int id) {
        int xStart = (int) Math.max(0, ((-Camera.position.x / Camera.scale.x) / Tile.TILE_SIZE) - 1);
        int xEnd = (int) Math.min(World.w, ((((-Camera.position.x + Game.w) / Camera.scale.x) / Tile.TILE_SIZE) + 4));
        int yStart = (int) Math.max(0, ((-Camera.position.y / Camera.scale.y) / Tile.TILE_SIZE) - 1);
        int yEnd = (int) Math.min(World.h, ((((-Camera.position.y + Game.h) / Camera.scale.y) / Tile.TILE_SIZE) + 4));
        for(int yy = yStart; yy < yEnd; yy++) {
            for(int xx = xStart; xx < xEnd; xx++) {
                /*
                switch(World.pipeMap[xx][yy]) {
                    case -1:
                        break;
                    case 0:
                        // Right
                        b.draw(Tile.Pipe.StonePipe.animations[0].getCurrentFrame(), xx * Tile.TILE_SIZE, yy * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                        Tile.renderBreakLevel(b, x, y, bl, id);
                        break;
                    case 2:
                        // Left
                        b.draw(Tile.Pipe.StonePipe.animations[2].getCurrentFrame(), xx * Tile.TILE_SIZE, yy * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                        Tile.renderBreakLevel(b, x, y, bl, id);
                        break;
                    case 1:
                        // Down
                        b.draw(Tile.Pipe.StonePipe.animations[1].getCurrentFrame(), xx * Tile.TILE_SIZE, yy * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                        Tile.renderBreakLevel(b, x, y, bl, id);
                        break;
                    case 3:
                        // Up
                        b.draw(Tile.Pipe.StonePipe.animations[3].getCurrentFrame(), xx * Tile.TILE_SIZE, yy * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                        Tile.renderBreakLevel(b, x, y, bl, id);
                        break;
                }

                 */
            }
        }
    }
}
