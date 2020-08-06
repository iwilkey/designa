package com.iwilkey.designa.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.util.ArrayList;

public class LightManager {

    private final GameBuffer gb;
    private final World world;
    private final ArrayList<Light> lights = new ArrayList<Light>();

    public static int[] highestTile; // Highest tile (at that x value)
    private static final int ambientLightSourceBlockLimit = 25;

    public LightManager(GameBuffer gb, World world) {
        this.gb = gb;
        this.world = world;
    }

    public void renderLight(Batch b, int x, int y) {
        int xx = x * Tile.TILE_SIZE;
        int yy = y * Tile.TILE_SIZE;

        TextureRegion shade;

        switch (World.lightMap[x][y]) {
            case 6:
                shade = Assets.light_colors[6];
                break;
            case 5:
                shade = Assets.light_colors[5];
                break;
            case 4:
                shade = Assets.light_colors[4];
                break;
            case 3:
                shade = Assets.light_colors[3];
                break;
            case 2:
                shade = Assets.light_colors[2];
                break;
            case 1:
                shade = Assets.light_colors[1];
                break;
            default:
                shade = Assets.light_colors[0];
        }

        b.draw(shade, xx, yy, Tile.TILE_SIZE, Tile.TILE_SIZE);

    }

    public void addLight(int x, int y, int strength) {

        for(Light l : lights) {
            if(l.x == x && l.y == y) return;
        }

        lights.add(new Light(x, y, strength));

        bakeLighting();

    }

    public void removeLight(int x, int y) {

        boolean found = false;
        for(int i = 0; i < lights.size(); i++) {
            if(lights.get(i).x == x && lights.get(i).y == y) {
                lights.remove(i);
                found = true;
            }
        }

        if(found) {
            if(lights.size() >= 1) bakeLighting();
            else World.lightMap = world.getOrigLightMap();
        }

    }

    public static void findHighestTiles() {
        highestTile = new int[World.w];
        for(int x = 0; x < World.w; x++) {
            for(int y = 0; y < World.h; y++) {
                if(World.tiles[x][y] != 0) {
                    int c = 0;

                    // TODO: Find a more steadfast algorithm for this.
                    for (int i = 0; i < ambientLightSourceBlockLimit; i++) {
                        try {
                            if (World.tiles[x][y - i - 1] == 0) c++;
                        } catch (IndexOutOfBoundsException ignored) {}
                    }

                    if(c == ambientLightSourceBlockLimit) {
                        highestTile[x] = y;
                    }
                }
            }
        }
    }

    public int[][] buildAmbientLight(int[][] darkLm) {
        int[][] newLm = darkLm;

        int hh = World.h;

        for(int y = 0; y < hh; y++) {
            for (int x = 0; x < World.w; x++) {
                if(World.tiles[x][y] == 0) newLm[x][hh - y - 1] = 6;
            }
        }

        float percentOfDay = world.getAmbientCycle().getPercentOfDay();
        int intensityLevel = (int) (percentOfDay / (100.0f / 6));

        for(int x = 0; x < World.w; x++) {
            for(int y = 0; y < hh; y++) {
                if(World.tiles[x][y] != 0) {
                    int c = 0;
                    for (int i = 0; i < ambientLightSourceBlockLimit; i++) {
                        try {
                            if (World.tiles[x][y - i - 1] == 0) c++;
                        } catch (IndexOutOfBoundsException ignored) {}
                    }

                    if(c == ambientLightSourceBlockLimit) {
                        for (int yy = y; yy < hh; yy++) {
                            if((World.tiles[x][y] == 2 || World.tiles[x][y] == 1))
                                newLm[x][hh - yy - 1] = intensityLevel - Math.abs(y - yy) + 1;
                        }
                    }
                }
            }
        }

        for(int x = 0; x < World.w; x++) {
            for(int y = 0; y < hh; y++) {
                if(World.tiles[x][y] != 0) {
                    if(hh - y < hh - highestTile[x] - 6) {
                        newLm[x][hh - y - 1] -= Math.abs(hh - y - (hh - highestTile[x] - 3));
                    }
                }
            }
        }

        return newLm;
    }

    public void bakeLighting() {

        int ww = World.w;
        int hh = World.h;

        int[][] oldLm = world.origLightMap;
        oldLm = buildAmbientLight(oldLm);

        int[][] newLm = new int[ww][hh];

        if(lights.size() != 0) {
            for (int i = 0; i < lights.size(); i++) {
                if (i > 0) newLm = lights.get(i).buildLightMap(newLm, ww, hh);
                else newLm = lights.get(i).buildLightMap(oldLm, ww, hh);
            }
        } else {
           newLm = oldLm;
        }

        World.bake(newLm);
    }

}
