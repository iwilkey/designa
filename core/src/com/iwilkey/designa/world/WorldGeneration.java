package com.iwilkey.designa.world;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.utils.PerlinNoise;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WorldGeneration {

    private static int[][] tiles;
    private static final int sampleDistance = 32;
    private static final PerlinNoise perlinNoise = new PerlinNoise(MathUtils.random(1000000, 10000000));

    public static String GenerateWorld(String name, int width, int height) {

        String path = "worlds/" + name + ".txt";
        File world = new File(path);

        try {
            if(!world.createNewFile()) {
                return path;
            }

            tiles = new int[width][height];

            for(int x = 0; x < width; x++) {
                int columnHeight = perlinNoise.getNoise(x, height, sampleDistance);
                for(int y = 0; y < columnHeight; y++) {
                    int id = (y == 0 + columnHeight - 1) ? 1 : 2;
                    tiles[x][y] = id;
                }
            }

            FileWriter worldWriter = new FileWriter(path);
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

        return path;

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
