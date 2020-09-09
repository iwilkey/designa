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
    public static int[] highestBackTile;
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

        if(World.lightMap[x][y] > 6) shade = Assets.light_colors[6];

        b.draw(shade, xx, yy, Tile.TILE_SIZE, Tile.TILE_SIZE);

    }

    public void addLight(int x, int y, int strength) {

        for(Light l : lights) {
            if(l.x == x && l.y == y) return;
        }

        lights.add(new Light(x, y, strength));

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

    public static int[] findHighestTiles() {
        int[] oht = new int[World.w];
        highestTile = new int[World.w];
        for(int x = 0; x < World.w; x++) {
            for(int y = 0; y < World.h; y++) {
                for(int yy = World.h - 1; yy > 0; yy--) {
                    if(World.tiles[x][World.h - yy] != 0) {
                        highestTile[x] = yy;
                        oht[x] = yy;
                        break;
                    }
                }
            }
        }

        return oht;
    }

    public static int[] findHighestBackTiles() {
        int[] oht = new int[World.w];
        highestBackTile = new int[World.w];
        for(int x = 0; x < World.w; x++) {
            for(int y = 0; y < World.h; y++) {
                for(int yy = World.h - 1; yy > 0; yy--) {
                    if(World.backTiles[x][World.h - yy] != 0) {
                        highestBackTile[x] = yy;
                        oht[x] = yy;
                        break;
                    }
                }
            }
        }

        return oht;
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

                for(int yy = hh - 1; yy > 0; yy--) {
                    if(World.tiles[x][hh - yy] != 0) {
                        if(highestTile[x] < yy) highestTile[x] = yy;
                        else if(highestTile[x] >= yy) {
                            if(yy == World.origHighTiles[x]) highestTile[x] = World.origHighTiles[x];
                            else if (yy > World.origHighTiles[x]) highestTile[x] = yy;
                            else break;
                        }
                        break;
                    }
                }

                if(hh - y == highestTile[x]) {
                    for (int yy = y; yy < hh; yy++) {
                        newLm[x][hh - yy - 1] = intensityLevel - Math.abs(y - yy) + 1;
                    }
                } else if (hh - y < highestTile[x]) {
                    newLm[x][hh - y - 1] = intensityLevel - Math.abs(hh - y - highestTile[x]);
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
