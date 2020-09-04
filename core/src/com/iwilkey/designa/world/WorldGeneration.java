package com.iwilkey.designa.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.EntityHandler;
import com.iwilkey.designa.entities.statics.Tree;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.physics.Vector2;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.utils.PerlinNoise;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class WorldGeneration {

    public static class Cloud {
        public Texture texture;
        public Vector2 position;
        private static final int cloudW = 100, cloudH = (int) (100 / 2.27f);
        public Cloud(Vector2 pos) {
            this.position = pos;
            texture = Assets.clouds[MathUtils.random(0,3)];
        }

        public void tick() {
            position.x %= World.w * Tile.TILE_SIZE;
            position.x += 0.04f;
        }

        public void render(Batch b) {
            b.draw(texture, position.x, position.y, cloudW, cloudH);
        }
    }

    public static class Mountain {
        public Texture texture;
        public Vector2 position;
        private static final int mountainW = 444, mountainH = (int) (444 / 2.33f);
        public Mountain(Vector2 pos) {
            this.position = pos;
            texture = Assets.mountains;
        }
        public void tick() {}

        public void render(Batch b) {
            b.draw(texture, position.x, position.y, mountainW, mountainH);
        }

    }

    public static int[][] tiles, backTiles;

    private static final int sampleDistance = 32;
    private static final PerlinNoise perlinNoise = new PerlinNoise(MathUtils.random(1000000, 10000000));
    private static final PerlinNoise backTilePerlin = new PerlinNoise(MathUtils.random(10000000, 10000000 + 10000));


    // Clouds & mountains
    public static ArrayList<Cloud> clouds = new ArrayList<>();
    public static ArrayList<Mountain> mountains = new ArrayList<>();

    public static String GenerateTerrain(String name, int width, int height) {

        // TODO: This method causes the desktop version not to run after being built by gradle.

        TEST_TOOLS_DELETE_ALL_WORLDS();

        String path = "worlds/" + name + ".dw";
        FileHandle world = Gdx.files.local(path);

        try {
            if(world.exists()) {
                return world.path();
            }

            // Terrain Generation
            tiles = new int[width][height];
            backTiles = new int[width][height];

            for(int x = 0; x < width; x++) {
                int columnHeight = perlinNoise.getNoise(x, height, sampleDistance);
                for(int y = 0; y < columnHeight; y++) {
                    int id;
                    if(y == columnHeight - 1) id = Tile.grassTile.getID();
                    else if (y < columnHeight - 1 && y
                            >= columnHeight - MathUtils.random(4, 10)) id = Tile.dirtTile.getID();
                    else id = Tile.stoneTile.getID();
                    tiles[x][y] = id;
                }

                int backTileColumnHeight = backTilePerlin.getNoise(x, height, sampleDistance);
                for(int yy = 0; yy < backTileColumnHeight; yy++){
                    int id;
                    if(yy == backTileColumnHeight - 1) id = Tile.grassTile.getID();
                    else if (yy < backTileColumnHeight - 1 && yy
                            >= backTileColumnHeight - MathUtils.random(4, 10)) id = Tile.dirtTile.getID();
                    else id = Tile.stoneTile.getID();
                    backTiles[x][yy] = id;
                }
            }

            // End Terrain Generation

            Writer w = world.writer(true);
            w.write(width + " " + height + "\n");

            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    int num = tiles[x][height - y - 1];
                    w.write(num + " ");
                }
                w.write("\n");
            }

            w.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return world.path();

    }

    private static void TEST_TOOLS_DELETE_ALL_WORLDS() {
        FileHandle[] allWorlds;
        allWorlds = Gdx.files.local("/worlds").list();
        for(FileHandle fh : allWorlds) fh.delete();
    }

    public static void EnvironmentGeneration(GameBuffer gb, EntityHandler e) {

        final int TREE_AMOUNT = World.w / 6;

        ArrayList<Integer> trees = new ArrayList<Integer>();
        for(int i = 0; i < TREE_AMOUNT; i++) {
            int tileX = MathUtils.random(0, World.w);
            if(tileX >= World.w / 2 - 30 && tileX <= World.w / 2 + 30) continue;
            trees.add(tileX);

        }

        for (int tree : trees) {
            try {
                int y = LightManager.highestTile[tree];
                e.addEntity(new Tree(gb, tree * Tile.TILE_SIZE, y * Tile.TILE_SIZE));
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }

        AmbientGeneration();

    }

    private static void AmbientGeneration() {
        int pixW = World.w * Tile.TILE_SIZE, pixH = World.h * Tile.TILE_SIZE;

        int numClouds = pixW / Cloud.cloudW;
        for(int x = 0; x < numClouds + 1; x++) {
            int xx = x * Cloud.cloudW;
            float yy = MathUtils.random((World.h * Tile.TILE_SIZE) - 1200, (World.h * Tile.TILE_SIZE) - 100);
            /*
            do {
                yy = MathUtils.random((World.h * Tile.TILE_SIZE) - 1200, (World.h * Tile.TIL
            } while (yy < LightManager.highestTile[xx / Tile.TILE_SIZE])
             */
            clouds.add(new Cloud(new Vector2(xx, yy)));
        }

        int numMountains = pixW / Mountain.mountainW;
        for(int x = 0; x < numMountains + 1; x++) {
            int xx = x * Mountain.mountainW;
            mountains.add(new Mountain(new Vector2(xx, (World.h * Tile.TILE_SIZE) - 1200)));
        }
    }

    public static void OreGeneration() {
        for(int x = 0; x < World.w; x++) {
            for(int yy = 0; yy < World.h; yy++) {
                if(yy > World.h - LightManager.highestTile[x] - 1) {
                    if(World.tiles[x][yy] == Tile.stoneTile.getID()) {
                        int id = Tile.stoneTile.getID();
                        if(percentChance(10)) id = Tile.copperOreTile.getID();
                        if(yy > World.h - LightManager.highestTile[x] + 10) {
                            if (percentChance(5)) id = Tile.silverOreTile.getID();
                            if(yy > World.h - LightManager.highestTile[x] + 16) {
                                if (percentChance(2)) id = Tile.ironOreTile.getID();
                            }
                        }
                        World.tiles[x][yy] = id;
                    }
                }
            }
        }
    }

    private static boolean percentChance(int percent) {
        if(percent > 100) return true;
        return MathUtils.random(0, 100) > 100 - percent;
    }

}
