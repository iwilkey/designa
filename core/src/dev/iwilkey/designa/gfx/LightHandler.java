package dev.iwilkey.designa.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.world.World;

import java.util.ArrayList;

public class LightHandler {

    final byte[][] darkMap;
    public byte[] highestFrontTile,
            originalHighestFrontTiles;
    final int LIGHT_LEVEL_MAX = 10,
        ww, hh;

    World world;
    Texture lightBlock;
    Color lightColor;
    ArrayList<Light> lights;

    public LightHandler(World world) {
        this.world = world;
        darkMap = new byte[world.WIDTH][world.HEIGHT];

        ww = world.WIDTH; hh = world.HEIGHT;

        for(int x = 0; x < world.WIDTH; x++)
            for(int y = 0; y < world.HEIGHT; y++)
                darkMap[x][y] = 0;
        world.LIGHT_MAP = darkMap;

        lights = new ArrayList<>();
        lightBlock = new Texture("textures/ambient/sky.jpg");
        lightColor = new Color(0,0,0,1);
    }

    int xx, yy, level;
    float intensity;
    public void renderLight(Batch b, int x, int y) {

        xx = x * Tile.TILE_SIZE;
        yy = y * Tile.TILE_SIZE;

        level = (world.LIGHT_MAP[x][y] <= 0) ? 0 :
                Math.min(world.LIGHT_MAP[x][y], LIGHT_LEVEL_MAX);
        lightColor.a = intensity = 1 - ((float)level / LIGHT_LEVEL_MAX);

        b.setColor(lightColor);
        b.draw(lightBlock, xx, yy, Tile.TILE_SIZE, Tile.TILE_SIZE);
        b.setColor(Color.WHITE);

    }

    public void addLight(int x, int y, int strength) {
        for(Light l : lights) if(l.x == x && l.y == y) return;
        lights.add(new Light(x, y, strength));
        bake();
    }

    boolean found = false;
    public void removeLight(int x, int y) {
        found = false;
        for(int i = 0; i < lights.size(); i++) {
            if(lights.get(i).x == x && lights.get(i).y == y) {
                lights.remove(i);
                found = true;
                break;
            }
        }
        if(found) {
            if(lights.size() >= 1) bake();
            else world.LIGHT_MAP = darkMap;
        }
    }

    byte run = 0;
    public void findHighestTiles() {
        if(run == 0) originalHighestFrontTiles = new byte[ww];
        highestFrontTile = new byte[ww];
        for(int x = 0; x < ww; x++)
            for(int y = 0; y < hh; y++)
                for(int yy = hh - 1; yy > 0; yy--)
                    if(world.FRONT_TILES[x][yy][0] != Tile.AIR.getTileID()) {
                        if(run == 0) originalHighestFrontTiles[x] = (byte)(yy + 1);
                        if(highestFrontTile[x] < yy) highestFrontTile[x] = (byte)(yy + 1);
                        else if(highestFrontTile[x] >= yy) {
                            if(yy == originalHighestFrontTiles[x]) highestFrontTile[x] = originalHighestFrontTiles[x];
                            else if (yy > originalHighestFrontTiles[x]) highestFrontTile[x] = (byte)(yy + 1);
                            else break;
                        }
                        break;
                    }
        if(run == 0) run++;
    }

    float percentOfDay;
    int intensityLevel;
    public byte[][] buildAmbientLight() {
        byte[][] returnedLightMap = darkMap;

        percentOfDay = (run == 0) ? 1 : world.ambientCycle.getPercentOfDay();
        intensityLevel = (int) Math.floor((percentOfDay / (100.0f / LIGHT_LEVEL_MAX)) * 100);

        findHighestTiles();

        for(int x = 0; x < ww; x++) {
            for(int y = 0; y < hh; y++) {
                if(hh - y == highestFrontTile[x])
                    for(int yy = y; yy < hh; yy++)
                        returnedLightMap[x][hh - yy - 1] = (byte)(intensityLevel - Math.abs(y - yy) + 1);
                else if(hh - y <= highestFrontTile[x])
                    returnedLightMap[x][hh - y - 1] = (byte)(intensityLevel - Math.abs(hh - y - highestFrontTile[x]));
            }
        }

        return returnedLightMap;
    }

    public void bake() {

        byte[][] builtAmbient = buildAmbientLight();
        byte[][] newLightMap = darkMap;

        // Build for lights
        if(lights.size() != 0) {
            for (int i = 0; i < lights.size(); i++) {
                if(i > 0) newLightMap = lights.get(i).buildLightMap(newLightMap, ww, hh);
                else newLightMap = lights.get(i).buildLightMap(builtAmbient, ww, hh);
            }
        } else newLightMap = builtAmbient;

        // Air is fully lit
        for(int x = 0; x < ww; x++)
            for(int y = 0; y < hh; y++)
                if(world.FRONT_TILES[x][y][0] == Tile.AIR.getTileID()) newLightMap[x][y] = LIGHT_LEVEL_MAX;

        world.LIGHT_MAP = newLightMap;
    }

}
