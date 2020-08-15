package com.iwilkey.designa.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.EntityHandler;
import com.iwilkey.designa.entities.statics.Tree;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.utils.PerlinNoise;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WorldGeneration {

    private static int[][] tiles;

    private static final int sampleDistance = 32;
    private static final PerlinNoise perlinNoise = new PerlinNoise(MathUtils.random(1000000, 10000000));

    public static String GenerateTerrain(String name, int width, int height) {

        // TODO: This method causes the desktop version not to run after being built by gradle.

        String path = "worlds/" + name + ".txt";
        FileHandle world = Gdx.files.internal(path);

        try {
            if(world.exists()) {
                return world.path();
            }

            // Terrain Generation
            tiles = new int[width][height];

            for(int x = 0; x < width; x++) {
                int columnHeight = perlinNoise.getNoise(x, height, sampleDistance);
                for(int y = 0; y < columnHeight; y++) {
                    int id;
                    if(y == columnHeight - 1) id = 1;
                    else if (y < columnHeight - 1 && y
                            >= columnHeight - MathUtils.random(4, 10)) id = 2;
                    else id = 5;

                    tiles[x][y] = id;
                }
            }
            // End Terrain Generation

            FileWriter worldWriter = new FileWriter(world.path());
            worldWriter.write(width + " " + height + "\n");

            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    int num = tiles[x][height - y - 1];
                    worldWriter.write(num + " ");
                }
                worldWriter.write("\n");
            }

            worldWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return world.path();

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

    }
}
