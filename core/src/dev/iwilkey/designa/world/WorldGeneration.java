package dev.iwilkey.designa.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.iwilkey.designa.math.Maths;
import dev.iwilkey.designa.math.PerlinNoise;
import dev.iwilkey.designa.tile.Tile;

import java.awt.*;
import java.util.ArrayList;

public class WorldGeneration {

    static int cH, pixW, pixH,
        sampleDistance;
    static byte ID;

    public static void GenerateWorld(int WIDTH, int HEIGHT, byte[][][] FRONT_TILES, byte[][][] BACK_TILES, ArrayList<World.Cloud> clouds) {
        sampleDistance = 2 * Tile.TILE_SIZE;

        PerlinNoise perlin = new PerlinNoise(MathUtils.random(1000000, 10000000));

        for(int x = 0; x < WIDTH; x++) {
            cH = perlin.getNoise(x, HEIGHT - 100, sampleDistance) + 20;
            for(int y = 0; y < cH; y++) {
                if(y == cH - 1) ID = (byte) Tile.GRASS.getTileID();
                else if (y < cH - 1 && y
                        >= cH - MathUtils.random(4, 10)) ID = (byte)Tile.DIRT.getTileID();
                else ID = (byte)Tile.STONE.getTileID();
                try {
                    FRONT_TILES[x][y][0] = ID;
                    FRONT_TILES[x][y][1] = (byte) Tile.getTileFromID(ID).getStrength();
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }

        PerlinNoise backPerlin = new PerlinNoise(MathUtils.random(10000000, 100000000));

        for(int x = 0; x < WIDTH; x++) {
            cH = backPerlin.getNoise(x, HEIGHT - 100, sampleDistance + 32) + 30;
            for(int y = 0; y < cH; y++) {
                if(y == cH - 1) ID = (byte)Tile.GRASS.getTileID();
                else if (y < cH - 1 && y
                        >= cH - MathUtils.random(4, 10)) ID = (byte)Tile.DIRT.getTileID();
                else ID = (byte)Tile.STONE.getTileID();
                try {
                    BACK_TILES[x][y][0] = ID;
                    BACK_TILES[x][y][1] = (byte) Tile.getTileFromID(ID).getStrength();
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }

        // Ambient Gen
        pixW = WIDTH * Tile.TILE_SIZE;
        pixH = HEIGHT * Tile.TILE_SIZE;

        int numClouds = pixW / World.Cloud.cloudW;
        for(int x = 0; x < numClouds + 1; x++) {
            int xx = x * World.Cloud.cloudW;
            float yy = MathUtils.random((HEIGHT * Tile.TILE_SIZE) - (200 * Tile.TILE_SIZE), (HEIGHT * Tile.TILE_SIZE) + (100 * Tile.TILE_SIZE));
            clouds.add(new World.Cloud(new Vector2(xx, yy), WIDTH));
        }

    }

    private static final byte AVERAGE_CAVE_WIDTH = 20,
        AVERAGE_CAVE_HEIGHT = 20,
        AVERAGE_CAVE_ORIGIN_Y = 35,
        AVERAGE_CAVE_ORIGIN_Y_VARIANCE = 10,
        AVERAGE_CAVE_SPACING = 16,
        CAVE_SAMPLE_DISTANCE = 3, // In Tiles.
        ORE_SPAWN_CHANCE = 10;

    static int y, ccH, dy;
    static byte id = 0;
    public static void GenerateCaves(int WIDTH, int HEIGHT, byte[][][] FRONT_TILES, byte[][][] BACK_TILES, byte[] HIGHEST_FRONT_TILES) {

        ArrayList<Point> caveOrigin = new ArrayList<>();
        while(caveOrigin.size() < WIDTH) {
            y = AVERAGE_CAVE_ORIGIN_Y + MathUtils.random(-AVERAGE_CAVE_ORIGIN_Y_VARIANCE, AVERAGE_CAVE_ORIGIN_Y_VARIANCE);
            if(caveOrigin.size() == 0) caveOrigin.add(new Point(8, y));
            else {
                try {
                    y = HIGHEST_FRONT_TILES[caveOrigin.get(caveOrigin.size() - 1).x] - AVERAGE_CAVE_ORIGIN_Y +
                            MathUtils.random(-AVERAGE_CAVE_ORIGIN_Y_VARIANCE, AVERAGE_CAVE_ORIGIN_Y_VARIANCE);
                    caveOrigin.add(new Point(caveOrigin.get(caveOrigin.size() - 1).x +
                            (AVERAGE_CAVE_WIDTH + MathUtils.random(-AVERAGE_CAVE_SPACING, AVERAGE_CAVE_SPACING)), y));
                } catch (ArrayIndexOutOfBoundsException ignored) { break; }
            }
        }

        ccH = 0;
        for(Point p : caveOrigin) {
            PerlinNoise caveNoise = new PerlinNoise(MathUtils.random(1000000, 10000000));
            for(int xx = 0; xx < AVERAGE_CAVE_WIDTH + MathUtils.random(0, 10); xx++) {
                ccH = caveNoise.getNoise(p.x + xx, AVERAGE_CAVE_HEIGHT, (CAVE_SAMPLE_DISTANCE) + MathUtils.random(-1, 1)) + MathUtils.random(-2, 2);
                for(int yy = -(ccH / 2); yy < (ccH / 2); yy++) {
                    dy = (yy < -(ccH / 2) + 1 || yy > (ccH / 2) - 1) ? MathUtils.random(-1, 1) : 0;
                    try {
                        FRONT_TILES[p.x + xx][p.y + yy + dy][0] = (byte) Tile.AIR.getTileID();
                        if (Maths.percentChance(ORE_SPAWN_CHANCE)) {
                            if (Maths.percentChance(1)) id = (byte) Tile.IRON_ORE.getTileID();
                            else if (Maths.percentChance(1)) id = (byte) Tile.SILVER_ORE.getTileID();
                            else if (Maths.percentChance(1)) id = (byte) Tile.COPPER_ORE.getTileID();
                            BACK_TILES[p.x + xx][p.y + yy + dy][0] = id;
                            BACK_TILES[p.x + xx][p.y + yy + dy][1] = (byte) Tile.getTileFromID(id).getStrength();
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                }
            }
        }

        for(int xx = 0; xx < WIDTH; xx++) {
            for(int yy = 0; yy < HEIGHT; yy++) {
                if(yy < HIGHEST_FRONT_TILES[xx] && BACK_TILES[xx][yy][0] == (byte) Tile.STONE.getTileID()) {
                    id = -1;
                    if(Maths.percentChance(1)) id = (byte) Tile.IRON_ORE.getTileID();
                    else if (Maths.percentChance(1)) id = (byte) Tile.SILVER_ORE.getTileID();
                    else if (Maths.percentChance(1)) id = (byte) Tile.COPPER_ORE.getTileID();
                    if(id != -1) {
                        BACK_TILES[xx][yy][0] = id;
                        BACK_TILES[xx][yy][1] = (byte) Tile.getTileFromID(id).getStrength();
                    }
                } else if (BACK_TILES[xx][yy][0] == (byte) Tile.STONE.getTileID()) {
                    if(Maths.percentChance(1)) {
                        BACK_TILES[xx][yy][0] = (byte) Tile.COPPER_ORE.getTileID();
                        BACK_TILES[xx][yy][1] = (byte) Tile.COPPER_ORE.getStrength();
                    }
                }
            }
        }
    }
}
