package com.iwilkey.designa.world;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.EntityHandler;
import com.iwilkey.designa.entities.creature.Player;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.items.ItemHandler;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.utils.Utils;

public class World {

    private final GameBuffer gb;
    public static int w, h;

    // Rendering
    public static int[][] tiles;
    public int[][] tileBreakLevel;
    public static int[][] lightMap;
    public int[][] origLightMap;
    public static int[] origHighTiles;

    // Entities
    private final EntityHandler entityHandler;

    // Items
    private static ItemHandler itemHandler;

    // Light
    private final LightManager lightManager;

    // Environment
    private final AmbientCycle ambientCycle;

    public World(GameBuffer gb, String path) {
        this.gb = gb;
        lightManager = new LightManager(gb, this);
        ambientCycle = new AmbientCycle(this, gb);
        entityHandler = new EntityHandler(new Player(gb, 0,
                0));
        itemHandler = new ItemHandler(gb);

        loadWorld(path);
    }

    public void tick() {
        ambientCycle.tick();
        itemHandler.tick();
        entityHandler.tick();
    }

    public void render(Batch b) {

        int xStart = (int) Math.max(0, ((-Camera.position.x / Camera.scale.x) / Tile.TILE_SIZE) - 1);
        int xEnd = (int) Math.min(w, ((((-Camera.position.x + Game.w) / Camera.scale.x) / Tile.TILE_SIZE) + 4));
        int yStart = (int) Math.max(0, ((-Camera.position.y / Camera.scale.y) / Tile.TILE_SIZE) - 1);
        int yEnd = (int) Math.min(h, ((((-Camera.position.y + Game.h) / Camera.scale.y) / Tile.TILE_SIZE) + 4));

        for(int y = yStart; y < yEnd; y++) {
            for(int x = xStart; x < xEnd; x++) {
                int xx = x * Tile.TILE_SIZE;
                int yy = y * Tile.TILE_SIZE;

                ambientCycle.render(b, xx, yy);
                if(yy < (origHighTiles[x]) * Tile.TILE_SIZE) b.draw(Assets.backDirt, xx, yy, 16, 16);
                getTile(x, y).render(b, xx, yy, tileBreakLevel[x][(h - y) - 1], getTile(x, y).getID());
            }
        }

        entityHandler.render(b);

        for(int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                lightManager.renderLight(b, x, y);
            }
        }

        itemHandler.render(b);

        entityHandler.getPlayer().getBuildingHandler().render(b);

    }

    public Tile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= w || y >= h) return Tile.airTile;
        Tile t = Tile.tiles[tiles[x][Math.abs(h - y) - 1]];
        if(t == null) return Tile.airTile;
        return t;
    }

    private void loadWorld(String path) {
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");

        w = Utils.parseInt(tokens[0]);
        h = Utils.parseInt(tokens[1]);

        tiles = new int[w][h];
        tileBreakLevel = new int[w][h];
        lightMap = new int[w][h];
        origLightMap = new int[w][h];
        origHighTiles = new int[w];

        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                int id = Utils.parseInt(tokens[x + y * w + 2]);
                tiles[x][y] = id;
                tileBreakLevel[x][y] = Tile.getStrength(id);
                lightMap[x][h - y - 1] = 0;
                origLightMap[x][h - y - 1] = 0;
            }
        }

        origHighTiles = LightManager.findHighestTiles();
        // WorldGeneration.GenerateEnvironment(gb, entityHandler, LightManager.highestTile, w, h);
        lightMap = lightManager.buildAmbientLight(lightMap);

        entityHandler.getPlayer().setX((w / 2f) * Tile.TILE_SIZE);
        entityHandler.getPlayer().setY((LightManager.highestTile[(w / 2)]) * Tile.TILE_SIZE);
    }

    public static void bake(int[][] lm) { lightMap = lm; }
    public GameBuffer getGameBuffer() { return gb; }
    public static ItemHandler getItemHandler() { return itemHandler; }
    public EntityHandler getEntityHandler() { return entityHandler; }
    public int[][] getOrigLightMap() { return this.origLightMap; }
    public LightManager getLightManager() { return lightManager; }
    public AmbientCycle getAmbientCycle() { return ambientCycle; }

}
