package com.iwilkey.designa.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.util.ArrayList;

public class LightManager {

    private GameBuffer gb;
    private World world;
    private ArrayList<Light> lights = new ArrayList<Light>();

    public LightManager(GameBuffer gb, World world) {
        this.gb = gb;
        this.world = world;
    }

    public void renderLight(Batch b, int x, int y) {
        int xx = x * Tile.TILE_SIZE;
        int yy = y * Tile.TILE_SIZE;

        TextureRegion shade;

        switch (world.getLightMap()[x][(gb.getWorld().h - y) - 1]) {
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
        for(int i = 0; i < lights.size(); i++) {
            if(lights.get(i).x == x && lights.get(i).y == y) return;
        }

        lights.add(new Light(x, y, strength));

        bakeLighting();

        System.out.println("Light added at " + x + " " + y);
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
            else world.setLightMap(world.getLightMap());
        }
    }

    public void bakeLighting() {
        int ww = world.w;
        int hh = world.h;

        int oldLm[][] = new int[ww][hh];
        for(int y = 0; y < hh; y++) {
            for(int x = 0; x < ww; x++) {
                // TODO: This also could be the source of light rendering trouble.
                oldLm[x][(hh - y) - 1] = world.getLightMap()[x][(hh - y) - 1];
            }
        }

        int[][] newLm = new int[ww][hh];
        for (int i = 0; i < lights.size(); i++) {
            if(i > 0) newLm = lights.get(i).buildLightMap(newLm, ww, hh);
            else newLm = lights.get(i).buildLightMap(oldLm, ww, hh);

        }
        world.setLightMap(newLm);

        System.out.println("Light has been baked!");
    }

}
