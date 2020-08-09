package com.iwilkey.designa.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.utils.PerlinNoise;

import java.io.FileWriter;
import java.io.IOException;

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

    private static void writeTile(int id, int x, int y, int w, int h) {
        for(int yy = 0; yy < h; yy++) {
            for(int xx = 0; xx < w; xx++) {
                if(yy == y && xx == x) {
                    tiles[xx][yy] = id;
                }
            }
        }
    }
}
