package com.iwilkey.designa.building;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

public class NpcBuildingHandler {

    private final GameBuffer gb;
    private Entity entity;

    public NpcBuildingHandler(GameBuffer gb, Entity e) {
        this.gb = gb;
        this.entity = e;
    }

    public void placeBlock(int id, int x, int y) {
        if (gb.getWorld().getTile(x, y) instanceof Tile.AirTile) {
            World.tiles[x][World.h - y - 1] = id;
            gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
        }
    }

    public int damageBlock(int x, int y) {
        if (!(gb.getWorld().getTile(x, y) instanceof Tile.AirTile)) {
            gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] -= 1;
        } else {
            return -1;
        }
        checkBreak(x, y);
        return 1;
    }

    private void checkBreak(int x, int y) {
        if (gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] <= 0) {
            World.tiles[x][(World.h - y) - 1] = 0;
            gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(0);
            gb.getWorld().getLightManager().bakeLighting();
        }
    }

}
