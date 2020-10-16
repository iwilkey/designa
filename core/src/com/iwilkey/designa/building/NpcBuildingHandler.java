package com.iwilkey.designa.building;

import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.defense.WeaponHandler;
import com.iwilkey.designa.defense.WeaponType;
import com.iwilkey.designa.inventory.crate.Crate;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.machines.MachineHandler;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.util.HashMap;
import java.util.Map;

/**

 The NpcBuildingHandler gives non-playable-characters (including enemies) the ability to alter the world around them.
 It has a couple functions to help them achieve this.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

public class NpcBuildingHandler {

    /**
     * Global vars
     */

    // Final vars
    private final GameBuffer gb;

    /**
     * The NpcBuildingHandler constructor.
     * @param gb An instance of this class needs the GameBuffer.
     */
    public NpcBuildingHandler(GameBuffer gb) { this.gb = gb; }

    /**
     * This method gives the entity the power to place a tile.
     * @param id The ID of the tile being placed.
     * @param x The x location (in tiles) of the tile being placed.
     * @param y The y location (in tiles) of the tile being placed.
     */
    public void placeBlock(int id, int x, int y) {
        // If the tile location is air...
        if (World.getTile(x, y) instanceof Tile.AirTile) {
            // Add the tile to the tile map, set the break level, and bake the lighting.
            World.tiles[x][World.h - y - 1] = id;
            World.tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
            gb.getWorld().getLightManager().bakeLighting();
            playSound(x, y);
        }
    }

    /**
     * This method gives the entity the power to damage a tile.
     * @param x The x location (in tiles) of the tile being damaged.
     * @param y The y location (in tiles) of the tile being damaged.
     * @return Either a 1 or a -1 (a boolean) denoting whether or not the action was able to occur properly.
     */
    public int damageBlock(int x, int y) {
        // If the location isn't an air tile...
        if (!(World.getTile(x, y) instanceof Tile.AirTile)) {
            // If it's a torch, remove the light.
            if(World.getTile(x, y).getID() == Assets.torchTile.getID()) gb.getWorld().getLightManager().removeLight(x, y);
            // Alter the tile break level by one.
            World.tileBreakLevel[x][(World.h - y) - 1] -= 1;
        // If it was an air tile, return -1 (false).
        } else return -1;

        // Check the tile at the location to see if it should be destroyed.
        checkBreak(x, y);

        // The the appropriate sound.
        playSound(x, y);

        return 1;
    }

    /**
     * This method will play the appropriate sound based on what tile was broken.
     * @param x The x location (in tiles) of the tile.
     * @param y The y location (in tiles) of the tile
     */
    private void playSound(int x, int y) {
        if (World.getTile(x, y) instanceof Tile.GrassTile ||
                World.getTile(x, y) instanceof Tile.DirtTile)
            Assets.dirtHit[MathUtils.random(0, 2)].play(0.3f);
        else Assets.stoneHit[MathUtils.random(0, 2)].play(0.5f);
    }

    /**
     * This method will compare the tileBreakLevel at that place to zero and remove it if necessary.
     * @param x The x location (in tiles) of the tile.
     * @param y The y location (in tiles) of the tile.
     */
    private void checkBreak(int x, int y) {
        // Should the tile be removed?
        if (World.tileBreakLevel[x][(World.h - y) - 1] <= 0) {
            World.tiles[x][(World.h - y) - 1] = 0;
            World.tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(0);
            gb.getWorld().getLightManager().bakeLighting();
            specialTilesRemove(World.getTile(x, y), x, y);
        }
    }

    /**
     * This method will deal with removing special tiles that have extraneous processes attached to their existence.
     * @param tile The tile in question.
     * @param x The x location of the tile.
     * @param y The y location of the tile.
     */
    private void specialTilesRemove(Tile tile, int x, int y) {
        // Define an offset for debugging purposes.
        int off = 4;

        // If the tile is a torch, remove the light there.
        if (tile == Assets.torchTile) {
            gb.getWorld().getLightManager().removeLight(x, y);
            return;
        }

        // If the tile is a crate, destroy the crate and spawn into the game world all the items that were in the crate.
        if (tile == Assets.crateTile) {
            HashMap<Item, Integer> items = new HashMap<>();
            for (Crate crate : World.getEntityHandler().getPlayer().crates)
                if (crate.x == x && crate.y == y) items = crate.destroy();

            for (Map.Entry<Item, Integer> item : items.entrySet()) {
                try {
                    for (int i = 0; i < item.getValue(); i++)
                        World.getItemHandler().addItem(Item.getItemByID(item.getKey().getItemID())
                                .createNew(x * Tile.TILE_SIZE + off,
                                        y * Tile.TILE_SIZE + off));
                } catch (NullPointerException ignored) {
                }
            }

            World.getEntityHandler().getPlayer().removeCrate(x, y);
        }

        // Remove machines from MachineHandler record.
        if(tile == Assets.stonePipeTile) MachineHandler.pipes.removeIf(pipe -> pipe.x == x && pipe.y == y);
        if(tile == Assets.nodeTile) MachineHandler.nodes.removeIf(node -> node.x == x && node.y == World.h - y - 1);
        if(tile == Assets.copperMechanicalDrillTile) MachineHandler.drills.removeIf(drill -> drill.x == x && drill.y == y);
        if(tile == Assets.assemblerTile) MachineHandler.assemblers.removeIf(assembler -> assembler.x == x && assembler.y == World.h - y - 1);
    }
}
