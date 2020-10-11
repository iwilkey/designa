package com.iwilkey.designa.wave;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.entities.creature.violent.Enemy;
import com.iwilkey.designa.entities.creature.violent.TerraBot;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

public class Wave {
    public static GameBuffer gb;

    public static boolean active = false;
    public static int WAVE_NUM = 0, ENEMIES_ALIVE = 0;
    public int WAVES_SURVIVED;
    public int newEntityCount = 0;

    public Wave(GameBuffer gb){
        Wave.gb = gb;
    }

    public static void startWave() {
        active = true;
        WAVE_NUM++;

        int playerTileX = (int)World.getEntityHandler().getPlayer().getX() / Tile.TILE_SIZE;

        // Rule is wave num squared
        for(int i = 0; i < WAVE_NUM * WAVE_NUM; i++) {
            // Half spawn to left, half spawn to right
            if(i < (WAVE_NUM * WAVE_NUM) / 2) {
                // Left (at least 50 tiles)
                int lx = (playerTileX - MathUtils.random(50, 100)) * Tile.TILE_SIZE;
                World.getEntityHandler().addEntity(new TerraBot(gb, lx,
                        LightManager.highestTile[lx / Tile.TILE_SIZE] * Tile.TILE_SIZE));
                ENEMIES_ALIVE++;
            } else {
                // Right (at least 50 tiles)
                int rx = (playerTileX + MathUtils.random(50, 100)) * Tile.TILE_SIZE;
                World.getEntityHandler().addEntity(new TerraBot(gb, rx,
                        LightManager.highestTile[rx / Tile.TILE_SIZE] * Tile.TILE_SIZE));
                ENEMIES_ALIVE++;
            }
        }
    }

    public void tick() {
        if(active) {
            newEntityCount = 0;
            for (Entity e : World.getEntityHandler().getEntities())
                if (e instanceof Enemy) newEntityCount++;
            if (ENEMIES_ALIVE < newEntityCount) ENEMIES_ALIVE = newEntityCount;
            if (newEntityCount == 0) {
                ENEMIES_ALIVE = 0;
                WAVES_SURVIVED++;
                active = false;
            }
        }
    }
}
