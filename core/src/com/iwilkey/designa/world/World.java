package com.iwilkey.designa.world;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.EntityHandler;
import com.iwilkey.designa.entities.creature.Player;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.utils.Utils;

public class World {

    private GameBuffer gb;
    public static int w, h;

    // Rendering
    public int[][] tiles;

    // Entities
    private EntityHandler entityHandler;

    public World(GameBuffer gb, String path) {
        this.gb = gb;
        entityHandler = new EntityHandler(gb, new Player(gb, 100, 500));

        loadWorld(path);
    }

    public void tick() {
        entityHandler.tick();
    }

    public void render(Batch b) {

        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                int xx = x * Tile.TILE_SIZE;
                int yy = y * Tile.TILE_SIZE;
                getTile(x, y).render(b, xx, yy);
            }
        }

        entityHandler.render(b);

    }

    public Tile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= w || y >= h) return Tile.airTile;

        Tile t = Tile.tiles[tiles[x][Math.abs(h - y) - 1]];

        if(t == null) {
            return Tile.airTile;
        }

        return t;
    }

    private void loadWorld(String path) {
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");

        w = Utils.parseInt(tokens[0]);
        h = Utils.parseInt(tokens[1]);

        tiles = new int[w][h];

        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                int id = Utils.parseInt(tokens[x + y * w + 2]);
                tiles[x][y] = id;
            }
        }
    }

    public GameBuffer getGameBuffer() {
        return gb;
    }

}
