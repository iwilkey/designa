package com.iwilkey.designa.building;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.creature.passive.Player;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.gui.Hud;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.ToolSlot;
import com.iwilkey.designa.inventory.crate.Crate;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**

 The BuildingHandler class gives the player the ability to interact with the world around them. It is divided
 up into many functions, all of which are explained briefly before their definition.

 @author Ian Wilkey
 @version VERSION
 @since 7/21/2020

 */

public class BuildingHandler {

    /**
     * Global vars
     */

    // Final vars
    private final GameBuffer gb;
    private final Player player;
    private final Rectangle selectorCollider;

    // Geometry
    private float selectorX = 0, selectorY = 0;

    // Booleans
    private boolean inRange = false, onTop = false;
    public static boolean backBuilding = false;

    // Longs
    private long timer = 0, actionCooldown = 15;

    /**
     * Constructor for BuildingHandler. No override.
     * @param gb The BuildingHandler needs the GameBuffer.
     * @param p The BuildingHandler needs the Player instance.
     */
    public BuildingHandler(GameBuffer gb, Player p) {
        this.gb = gb;
        this.player = p;

        // Create the collider for the box collider the player sees follow the mouse around. It changes color
            // based off of if one is able to build or not.
        selectorCollider = new Rectangle((int) selectorX,
                (int) selectorY, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }

    /**
     * The BuildingHandler tick method.
     */
    public void tick() {
        // Make sure the selector is set in the correct place each tick.
        setSelector();

        /*
            *
            *   BUILDING / DESTROYING LOGIC
            *
         */

        // The purpose of these nested conditions are to verify if the player can place a tile, and if so,
            // how to place it.
        if (!Inventory.active) {

            // Handle a backBuilding toggle request. (Switch from front to back or back to front)
            if(InputHandler.backBuildingToggleRequest) {
                backBuilding = !backBuilding;
                Assets.invClick.play(0.5f);
                InputHandler.backBuildingToggleRequest = false;
            }

            // If the selector is in range and not on top of the player...
            if (inRange && !onTop) {
                // And they have requested to place a block...
                if (InputHandler.placeRequest) {
                    // And the current item they hold exists and isn't null...
                    if(ToolSlot.currentItem != null)
                        // Then evoke the placeTile method with the proper information if the item they hold
                            // is a legit placeable block.
                        if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock)
                            placeTile(((ItemType.PlaceableBlock) ToolSlot.currentItem.getItem().getItemType()).getTileID(),
                                    pointerOnTileX(), pointerOnTileY());
                    // And the tile that is currently selected is a chest tile, then let the player see what's
                        // in the crate.
                    if (World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.CrateTile)
                        toggleCrate(pointerOnTileX(), pointerOnTileY());

                    InputHandler.placeRequest = false;
                }

                // And they have requested to destroy a block then evoke the damage tile method with the proper information.
                if (InputHandler.destroyRequest) {
                    damageTile(pointerOnTileX(), pointerOnTileY());
                    InputHandler.destroyRequest = false;
                }
            }  else if (onTop) { // But the selector is actually on top of the player...
                // And they request to place a block...
                if (InputHandler.placeRequest) {
                    // And they item they have selected exists and isn't null...
                    if(ToolSlot.currentItem != null)
                        // Then jump the player and allow the block to be placed underneath the player ('cause I like it).
                        if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock) {
                            Player player = World.getEntityHandler().getPlayer();

                            /*

                                *   The idea here is to make it so the player can't jump and place anything underneath
                                *   if there is a block a tile above their head. The algorithm below is close but not
                                *   quite right. TODO: Refine it.

                            if(!(World.getTile((int) ((player.getX()) / Tile.TILE_SIZE),
                                    (int) (player.getY() / Tile.TILE_SIZE) + 2) instanceof Tile.AirTile) ||
                                    !(World.getTile((int) ((player.getX() + 4) / Tile.TILE_SIZE),
                                            (int) (player.getY() / Tile.TILE_SIZE) + 2) instanceof Tile.AirTile)) {
                                InputHandler.placeRequest = false;
                                return;

                            }
                             */

                            placeTile(((ItemType.PlaceableBlock) ToolSlot.currentItem.getItem().getItemType()).getTileID(),
                                    pointerOnTileX(), pointerOnTileY());
                            World.getEntityHandler().getPlayer().jump();
                        }

                    InputHandler.placeRequest = false;

                }
            }

            /*
                 *
                 *   COOL DOWN / PROLONGED ACTION LOGIC
                 *
             */

            // Ignore the NullPointerException because it doesn't stop the game. TODO Fix at some point
            try {
                // If the current tool the player has selected is the Drill...
                if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.CreatableItem.Tool.Drill) actionCooldown = 5;
                else actionCooldown = 15;
            } catch (NullPointerException ignored) {}

            // If the player is requesting a prolonged action...
            if(InputHandler.prolongedActionRequest) {
                // Damage the tile every pass of actionCooldown.
                timer++;
                if(timer > actionCooldown) {
                    if(inRange) damageTile(pointerOnTileX(), pointerOnTileY());
                    timer = 0;
                }
            } else {
                timer = 0;
            }
        }
    }

    /**
     * This method will set the tile selector appropriately based off of where the InputHandler says the cursor is.
     */
    private void setSelector() {
        selectorX = (pointerOnTileX() * Tile.TILE_SIZE);
        selectorY = (pointerOnTileY() * Tile.TILE_SIZE);
        selectorCollider.x = (int) selectorX;
        selectorCollider.y = (int) selectorY;
    }

    /**
     * This method compares the players x position with the mouse x position and dictates the direction the player faces.
     */
    private void checkFace() {
        if(pointerOnTileX() * Tile.TILE_SIZE - player.getX() > 0) player.setFace(1);
        else player.setFace(0);
    }

    /**
     * This method is invoked every time the program dictates a place request to be legit.
     * @param id The tile ID.
     * @param x The x position (in TILES) where the new tile will be placed in the tile map.
     * @param y The y position (in TILES) where the new tile will be placed in the tile map.
     */
    private void placeTile(int id, int x, int y) {
        // Change the players facing direction if necessary.
        checkFace();

        // If the player isn't building on the back tile map...
        if(!backBuilding) {
            // And the tile currently selected is air...
            if (World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.AirTile) {

                // Check to see if it's a special tile (i.e torch or crate) and handle it there instead of here.
                if(!(specialTilesAdd(id, x, y))) return;
                // Check to see if the tile was a mechanical drill and if so, the tile is handled there instead of here.
                if(mechDrillHandler(id, x, y)) return;
                // Check to see if the tile was a pipe and if so, the tile is handled there instead of here.
                if(pipePlacementHandler(id, x, y)) return;

                // Set the ID slot in the tile map to @id.
                World.tiles[x][(World.h - y) - 1] = id;
                // Reset its break level to its strength.
                World.tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
                // Subtract from the item count in the inventory.
                ToolSlot.currentItem.itemCount--;
            }
        } else { // But the player is back building...
            // So, if the back tile currently selected is air...
            if (gb.getWorld().getBackTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.AirTile) {
                // Set the back tile ID slot in the tile map to @id.
                World.backTiles[x][(World.h - y) - 1] = id;
                // Reset its break level to its strength.
                World.backTileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
                // Subtract from the item count in the inventory.
                ToolSlot.currentItem.itemCount--;
            }
        }

        // Bake the lighting!
        gb.getWorld().getLightManager().bakeLighting();

        // Play the appropriate building sound.
        buildingSound();

    }

    /**
     * This method will play the appropriate building sound when called.
     */
    private void buildingSound() {
        if(World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.GrassTile ||
                World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.DirtTile)
            Assets.dirtHit[MathUtils.random(0,2)].play(0.3f);
        else Assets.stoneHit[MathUtils.random(0,2)].play(0.5f);
    }

    /**
     * This method will check if the tile ID passed in is a special tile that has more functionality than a regular tile.
     * @param id The tile ID.
     * @param x The x position (in TILES) where the new tile will be placed in the tile map.
     * @param y The y position (in TILES) where the new tile will be placed in the tile map.
     * @return Did method take care of the tile or not?
     */
    private boolean specialTilesAdd(int id, int x, int y) {

        // Is the tile a torch?
        if (id == Tile.torchTile.getID())
            // If there is a back tile there to support the torch, then add it.
            if(World.backTiles[x][World.h - y - 1] != 0) {
                gb.getWorld().getLightManager().addLight(pointerOnTileX(), pointerOnTileY(), 8);
                return true;
            // Otherwise check to see if there is a front tile below it to support it, then add it.
            } else if (World.tiles[x][World.h - y] != 0 && World.tiles[x][World.h - y] != Tile.torchTile.getID()) {
                gb.getWorld().getLightManager().addLight(pointerOnTileX(), pointerOnTileY(), 8);
                return true;
            // There's nothing to support this torch.
            } else return false;

        // Is the tile a crate?
        if(id == Tile.crateTile.getID()) {
            player.addCrate(x, y); // Invoke the addCrate method on the player.
            return true;
        }

        return false;

    }

    /**
     * This method will check if the tile was a mechanical drill and handle it accordingly.
     * @param id The tile ID.
     * @param x The x position (in TILES) where the new tile will be placed in the tile map.
     * @param y The y position (in TILES) where the new tile will be placed in the tile map.
     * @return Did method take care of the tile or not?
     */
    private boolean mechDrillHandler(int id, int x, int y) {
        // Grab the item and dictate if it's a mechanical drill or not. If not, no reason to run this method.
        Item item = ToolSlot.currentItem.getItem();
        if(!(item.getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.MechanicalDrill)) return false;

        // If the tile currently selected is an ore, the player is allowed to place it.
        if(World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.CopperOreTile ||
                World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.SilverOreTile ||
                World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.IronOreTile) {

            ToolSlot.currentItem.itemCount--;
            World.tiles[x][(World.h - y) - 1] = id;
            World.tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
            Assets.stoneHit[MathUtils.random(0,2)].play(0.5f);

            return true;
        }

        return false;
    }

    /**
     * This method will check if the tile was a pipe and handle it accordingly.
     * @param id The tile ID.
     * @param x The x position (in TILES) where the new tile will be placed in the tile map.
     * @param y The y position (in TILES) where the new tile will be placed in the tile map.
     * @return Did method take care of the tile or not?
     */
    private boolean pipePlacementHandler(int id, int x, int y) {
        // Grab the item and dictate if it's a pipe or not. If not, no reason to run this method.
        Item item = ToolSlot.currentItem.getItem();
        if(!(item.getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.Pipe)) return false;

        // If the tile selected is air the player is allowed to place the pipe.
        if (World.getTile(x, y) instanceof Tile.AirTile) {

            World.tiles[x][(World.h - y) - 1] = id;
            World.tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
            ToolSlot.currentItem.itemCount--;

            // The pipe map needs to be updated with the correct direction the player has selected.
            World.pipeMap[x][y] = Hud.SELECTED_PIPE_DIRECTION;

            gb.getWorld().getLightManager().bakeLighting();

            Assets.stoneHit[MathUtils.random(0,2)].play(0.5f);

            return true;
        }

        return false;
    }

    /**
     * This method will turn on the crate that is at the x and y values passed in.
     * @param x The x value of crate in question.
     * @param y The y value of crate in question.
     */
    private void toggleCrate(int x, int y) {
        for(Crate crate : player.crates) crate.isActive = crate.x == x && crate.y == y;
        Assets.createItem[0].play(0.3f);
    }

    /**
     * This method is invoked every time the program dictates a destroy request to be legit.
     * @param x The x value (in tiles) requested.
     * @param y The y value (in tiles) requested.
     */
    private void damageTile(int x, int y) {
        // Change the players facing direction if necessary.
        checkFace();

        // TODO Finish JavaDoc
        if (!backBuilding) {
            if (!(World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.AirTile)) {
                if (ToolSlot.currentItem != null) {
                    try {
                        if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.CreatableItem.Tool.Drill) {
                            World.tileBreakLevel[x][(World.h - y) - 1] -=
                                    ((ItemType.CreatableItem.Tool.Drill) ToolSlot.currentItem.getItem().getItemType()).getStrength();
                        } else {
                            World.tileBreakLevel[x][(World.h - y) - 1] -= 1;
                        }
                    } catch (NullPointerException ignored) {}

                } else {
                    World.tileBreakLevel[x][(World.h - y) - 1] -= 1;
                }
                checkBreak(x, y);
                buildingSound();
            }
        } else {
            if (!(gb.getWorld().getBackTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.AirTile)) {
                if (ToolSlot.currentItem != null) {
                    if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.CreatableItem.Tool.Drill) {
                        World.backTileBreakLevel[x][(World.h - y) - 1] -=
                                ((ItemType.CreatableItem.Tool.Drill) ToolSlot.currentItem.getItem().getItemType()).getStrength();
                    } else {
                        World.backTileBreakLevel[x][(World.h - y) - 1] -= 1;
                    }

                } else {
                    World.backTileBreakLevel[x][(World.h - y) - 1] -= 1;
                }
                checkBreak(x, y);
                buildingSound();
            }
        }

    }

    private void checkBreak(int x, int y) {
        int off = 4;
        if(!backBuilding) {
            if (World.tileBreakLevel[x][(World.h - y) - 1] <= 0) {
                Tile tile = World.getTile(x, y);

                specialTilesRemove(tile, x, y);

                // Torch above
                if(World.tiles[x][World.h - y - 2] == Tile.torchTile.getID()) {
                    specialTilesRemove(Tile.torchTile, x, y + 1);
                    World.getItemHandler().addItem(Item.getItemByID(Tile.torchTile.getItemID()).createNew(pointerOnTileX() * Tile.TILE_SIZE + off,
                            pointerOnTileY() * Tile.TILE_SIZE + off));
                    World.tiles[x][(World.h - y) - 2] = 0;
                    gb.getWorld().tileBreakLevel[x][(World.h - y) - 2] = Tile.getStrength(0);
                    gb.getWorld().getLightManager().bakeLighting();
                }

                if(!(tile instanceof Tile.StoneTile)) {
                    World.getItemHandler().addItem(Item.getItemByID(tile.getItemID()).createNew(pointerOnTileX() * Tile.TILE_SIZE + off,
                            pointerOnTileY() * Tile.TILE_SIZE + off));
                } else {
                    World.getItemHandler().addItem(Assets.rockResource.createNew(pointerOnTileX() * Tile.TILE_SIZE + off,
                            pointerOnTileY() * Tile.TILE_SIZE + off));
                }

                World.tiles[x][(World.h - y) - 1] = 0;
                gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(0);
                gb.getWorld().getLightManager().bakeLighting();
            }
        } else {
            if (gb.getWorld().backTileBreakLevel[x][(World.h - y) - 1] <= 0) {
                Tile tile = gb.getWorld().getBackTile(x, y);

                World.getItemHandler().addItem(Item.getItemByID(tile.getItemID()).createNew(pointerOnTileX() * Tile.TILE_SIZE + off,
                        pointerOnTileY() * Tile.TILE_SIZE + off));

                World.backTiles[x][(World.h - y) - 1] = 0;
                gb.getWorld().backTileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(0);
                gb.getWorld().getLightManager().bakeLighting();
            }
        }
    }

    private void specialTilesRemove(Tile tile, int x, int y) {
        int off = 4;
        if (tile == Tile.torchTile) gb.getWorld().getLightManager().removeLight(x, y);

        if(tile == Tile.crateTile) {
            HashMap<Item, Integer> items = new HashMap<>();
            for(Crate crate : gb.getWorld().getEntityHandler().getPlayer().crates) {
                if(crate.x == x && crate.y == y) items = crate.destroy();
            }

            for(Map.Entry<Item, Integer> item : items.entrySet()) {
                try {
                    for(int i = 0; i < item.getValue(); i++)
                        World.getItemHandler().addItem(Item.getItemByID(item.getKey().getItemID()).createNew(pointerOnTileX() * Tile.TILE_SIZE + off,
                            pointerOnTileY() * Tile.TILE_SIZE + off));
                } catch (NullPointerException ignored) {}
            }

            gb.getWorld().getEntityHandler().getPlayer().removeCrate(x, y);
        }

        if(tile == Tile.stonePipeTile) {
            World.pipeMap[x][y] = -1;
            gb.getWorld().getLightManager().bakeLighting();
            Assets.stoneHit[MathUtils.random(0,2)].play(0.5f);
        }
    }

    private int pointerOnTileX() {
        return (int) ((((InputHandler.cursorX) - Camera.position.x) /
                Tile.TILE_SIZE) / Camera.scale.x);
    }

    private int pointerOnTileY() {
        return (int) ((((InputHandler.cursorY) - Camera.position.y) /
                Tile.TILE_SIZE) / Camera.scale.y);
    }

    public void render(Batch b) {

        if(!Inventory.active) {
            if (Math.abs(selectorX - player.getX()) < 1.5 * Tile.TILE_SIZE &&
                    Math.abs(selectorY - (player.getY() + 8)) < 2 * Tile.TILE_SIZE) {
                inRange = true;
                onTop = false;

                if(!backBuilding) {
                    if (!(gb.getWorld().getEntityHandler().getPlayer().getCollisionBounds(0f, 0f).intersects(selectorCollider))) {
                        b.draw(Assets.selector, (int) (selectorX), (int) (selectorY),
                                Tile.TILE_SIZE, Tile.TILE_SIZE);
                    } else {
                        inRange = false;
                        onTop = (selectorX - player.getX() < Tile.TILE_SIZE &&
                                selectorY - player.getY() < Tile.TILE_SIZE);
                        if (onTop)
                            b.draw(Assets.jumpSelector, (int) (selectorX), (int) (selectorY),
                                    Tile.TILE_SIZE, Tile.TILE_SIZE);
                        else
                            b.draw(Assets.errorSelector, (int) (selectorX), (int) (selectorY),
                                    Tile.TILE_SIZE, Tile.TILE_SIZE);
                    }
                } else {
                    b.draw(Assets.selector, (int) (selectorX), (int) (selectorY),
                            Tile.TILE_SIZE, Tile.TILE_SIZE);
                }

            } else {
                inRange = false;
                onTop = false;
                b.draw(Assets.transSelector, (int) (selectorX), (int) (selectorY),
                        Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
    }
}
