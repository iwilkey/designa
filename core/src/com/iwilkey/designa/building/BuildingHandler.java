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
import com.iwilkey.designa.machines.MachineHandler;
import com.iwilkey.designa.machines.MachineType;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**

 The BuildingHandler class gives the player the ability to interact with the world around them. It is divided
 up into many functions, all of which are explained briefly before their definition.

 @author Ian Wilkey (iwilkey)
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
                            if(player.isGrounded() && !player.isJumping()) player.jump();
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

            // Check to see if the tile was a mechanical drill and if so, the tile is handled there instead of here.
            if(mechDrillHandler(id, x, y)) return;
            // Check to see if the tile was a node and if so, the tile is handled there instead of here.
            if(nodeHandler(id, x, y)) return;
            // Check to see if the tile was a pipe and if so, the tile is handled there instead of here.
            if(pipePlacementHandler(id, x, y)) return;

            // And the tile currently selected is air...
            if (World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.AirTile) {

                // Check to see if it's a special tile (i.e torch or crate) and handle it there instead of here.
                if(!(specialTilesAdd(id, x, y))) return;

                // Set the ID slot in the tile map to @id.
                World.tiles[x][(World.h - y) - 1] = id;
                // Reset its break level to its strength.
                World.tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
                // Subtract from the item count in the inventory.
                ToolSlot.currentItem.itemCount--;
            }
        } else { // But the player is back building...

            // Pipes are not allowed on the back tiles
            if(ToolSlot.currentItem.getItem() == Assets.stonePipeItem) return;

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
        if (id == Tile.torchTile.getID()) {
            // If there is a back tile there to support the torch, then add it.
            if (World.backTiles[x][World.h - y - 1] != 0) {
                gb.getWorld().getLightManager().addLight(pointerOnTileX(), pointerOnTileY(), 8);
                gb.getWorld().getLightManager().bakeLighting();
                return true;
                // Otherwise check to see if there is a front tile below it to support it, then add it.
            } else if (World.tiles[x][World.h - y] != 0 && World.tiles[x][World.h - y] != Tile.torchTile.getID()) {
                gb.getWorld().getLightManager().addLight(pointerOnTileX(), pointerOnTileY(), 8);
                gb.getWorld().getLightManager().bakeLighting();
                return true;
                // There's nothing to support this torch.
            } else return false;
        }

        // Is the tile a crate?
        if(id == Tile.crateTile.getID()) {
            player.addCrate(x, y); // Invoke the addCrate method on the player.
            return true;
        }

        return true;

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
            if(World.getTile(x, y) == Tile.copperOreTile ||
                    World.getTile(x, y) == Tile.silverOreTile ||
                    World.getTile(x, y) == Tile.ironOreTile) {

                ToolSlot.currentItem.itemCount--;
                int resourceID = World.tiles[x][(World.h - y) - 1];
                World.tiles[x][(World.h - y) - 1] = id;
                World.tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
                MachineHandler.addMechanicalDrill(x, World.h - y - 1, Tile.tiles[resourceID],
                        MachineType.MechanicalDrill.ResourceType.COPPER);
                Assets.stoneHit[MathUtils.random(0,2)].play(0.5f);

                return true;
            }

        return true;
    }

    /**
     * This method will check if the tile was a node and handle it accordingly.
     * @param id The tile ID.
     * @param x The x position (in TILES) where the new tile will be placed in the tile map.
     * @param y The y position (in TILES) where the new tile will be placed in the tile map.
     * @return Did method take care of the tile or not?
     */
    private boolean nodeHandler(int id, int x, int y) {
        // Grab the item and dictate if it's a node or not. If not, no reason to run this method.
        Item item = ToolSlot.currentItem.getItem();
        if(!(item.getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.Node)) return false;

        ToolSlot.currentItem.itemCount--;
        int resourceID = World.tiles[x][(World.h - y) - 1];
        World.tiles[x][(World.h - y) - 1] = id;
        World.tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
        MachineHandler.addNode(x, World.h - y - 1);
        Assets.stoneHit[MathUtils.random(0,2)].play(0.5f);

        return true;

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
            MachineHandler.addPipe(x, y);
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

        // If the player is not back building...
        if (!backBuilding) {
            // And the tile selected is air...
            if (!(World.getTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.AirTile)) {
                // Check to see if the item current held exists and isn't null...
                if (ToolSlot.currentItem != null) {
                    // Ignored NullPointerException because it doesn't break the game. TODO Fix later
                    try {
                        // If the item currently held is a drill...
                        if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.CreatableItem.Tool.Drill)
                            // Damage the block the unit strength of drill.
                            World.tileBreakLevel[x][(World.h - y) - 1] -=
                                    ((ItemType.CreatableItem.Tool.Drill) ToolSlot.currentItem.getItem().getItemType()).getStrength();
                        // The item isn't null, but is like a tile or something that can't break things like drills can.
                        else World.tileBreakLevel[x][(World.h - y) - 1] -= 1;
                    } catch (NullPointerException ignored) {}
                // Otherwise, just damage the block by one.
                } else World.tileBreakLevel[x][(World.h - y) - 1] -= 1;

                // Now, invoke the checkBreak method to see if the tile is damaged enough to be broken.
                checkBreak(x, y);

                // Play the appropriate building sound effect.
                buildingSound();

            }
        // Otherwise the player is building on the back tiles.
        } else {
            // Check if the selected back tile is air...
            if (!(gb.getWorld().getBackTile(pointerOnTileX(), pointerOnTileY()) instanceof Tile.AirTile)) {
                // And if the current item held exists and isn't null...
                if (ToolSlot.currentItem != null) {
                    // If the tool is a drill...
                    if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.CreatableItem.Tool.Drill)
                        // Damage with strength of drill
                        World.backTileBreakLevel[x][(World.h - y) - 1] -=
                                ((ItemType.CreatableItem.Tool.Drill) ToolSlot.currentItem.getItem().getItemType()).getStrength();
                    // Otherwise one.
                    else World.backTileBreakLevel[x][(World.h - y) - 1] -= 1;
                } else World.backTileBreakLevel[x][(World.h - y) - 1] -= 1;
                checkBreak(x, y);
                buildingSound();
            }
        }
    }

    /**
     * This method is invoked every time a tile is damaged to check and see if it should be completely destroyed or not.
     * @param x The x position of tile.
     * @param y The y position of tile.
     */
    private void checkBreak(int x, int y) {
        // Offset var init for debugging purposes.
        int off = 4;

        // If the player isn't building on the back tiles...
        if(!backBuilding) {
            // If the tile break level at that position is less than or equal to zero, it must be destroyed.
            if (World.tileBreakLevel[x][(World.h - y) - 1] <= 0) {
                // Get the tile instance there
                Tile tile = World.getTile(x, y);
                // Run specialTilesRemove to handle things like crates or torches since they have extraneous processes that
                    // have to happen
                specialTilesRemove(tile, x, y);

                // The following set of conditions pertain to if there is an instance of a torch one tile above the current tile.
                if(World.tiles[x][World.h - y - 2] == Tile.torchTile.getID()) {
                    // Invoke specialTilesRemove to get rid of the light at the specific y value passed in...
                    specialTilesRemove(Tile.torchTile, x, y + 1);
                    // Give the torch back to the player in item form...
                    World.getItemHandler().addItem(Item.getItemByID(Tile.torchTile.getItemID()).createNew(pointerOnTileX() * Tile.TILE_SIZE + off,
                            pointerOnTileY() * Tile.TILE_SIZE + off));
                    // Place air where the torch was...
                    World.tiles[x][(World.h - y) - 2] = 0;
                    // Reset the tileBreakLevel for air strength...
                    World.tileBreakLevel[x][(World.h - y) - 2] = Tile.getStrength(0);
                    // Bake the lighting.
                    gb.getWorld().getLightManager().bakeLighting();
                }

                // Reset the tile map to air here.
                World.tiles[x][(World.h - y) - 1] = 0;
                // Reset the strength.
                World.tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(0);
                // Give the player whatever item the tile specifies in it's class.
                World.getItemHandler().addItem(Item.getItemByID(tile.getItemID()).createNew(pointerOnTileX() * Tile.TILE_SIZE + off,
                        pointerOnTileY() * Tile.TILE_SIZE + off));
                gb.getWorld().getLightManager().bakeLighting();
            }
        // Otherwise the player was back building
        } else {
            // Same algorithm used above but for back tiles...
            if (World.backTileBreakLevel[x][(World.h - y) - 1] <= 0) {
                Tile tile = gb.getWorld().getBackTile(x, y);
                World.backTiles[x][(World.h - y) - 1] = 0;
                World.backTileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(0);
                World.getItemHandler().addItem(Item.getItemByID(tile.getItemID()).createNew(pointerOnTileX() * Tile.TILE_SIZE + off,
                        pointerOnTileY() * Tile.TILE_SIZE + off));
                gb.getWorld().getLightManager().bakeLighting();
            }
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
        if (tile == Tile.torchTile) {
            gb.getWorld().getLightManager().removeLight(x, y);
            return;
        }

        // If the tile is a crate, destroy the crate and spawn into the game world all the items that were in the crate.
        if(tile == Tile.crateTile) {
            HashMap<Item, Integer> items = new HashMap<>();
            for(Crate crate : World.getEntityHandler().getPlayer().crates)
                if(crate.x == x && crate.y == y) items = crate.destroy();

            for(Map.Entry<Item, Integer> item : items.entrySet()) {
                try {
                    for(int i = 0; i < item.getValue(); i++)
                        World.getItemHandler().addItem(Item.getItemByID(item.getKey().getItemID())
                                .createNew(pointerOnTileX() * Tile.TILE_SIZE + off,
                            pointerOnTileY() * Tile.TILE_SIZE + off));
                } catch (NullPointerException ignored) {}
            }

            World.getEntityHandler().getPlayer().removeCrate(x, y);
            return;
        }
    }

    /**
     * This method will take into account where the cursor compared to the camera and evaluate
     * the proper x coordinate for the tile selected.
     * @return The proper x coordinate of the tile selected in the tile map.
     */
    private int pointerOnTileX() {
        return (int) ((((InputHandler.cursorX) - Camera.position.x) /
                Tile.TILE_SIZE) / Camera.scale.x);
    }

    /**
     * This method will take into account where the cursor compared to the camera and evaluate
     * the proper y coordinate for the tile selected.
     * @return The proper y coordinate of the tile selected in the tile map.
     */
    private int pointerOnTileY() {
        return (int) ((((InputHandler.cursorY) - Camera.position.y) /
                Tile.TILE_SIZE) / Camera.scale.y);
    }

    /**
     * The render method for the BuildingManager.
     * @param b Every render method needs to be passed the graphics batch.
     */
    public void render(Batch b) {
        // If the inventory isn't active...
        if(!Inventory.active) {
            // Find the difference between the player and the cursor to evaluate if the tile selected is in range or not.
            if (Math.abs(selectorX - player.getX()) < 1.5 * Tile.TILE_SIZE &&
                    Math.abs(selectorY - (player.getY() + 8)) < 2 * Tile.TILE_SIZE) {
                // In this case, the player is in range and not on top.
                inRange = true;
                onTop = false;

                // If not back building...
                if(!backBuilding) {
                    // If the selector collider doesn't intersect with the player, draw the while selector.
                    if (!(World.getEntityHandler().getPlayer().getCollisionBounds(0f, 0f).intersects(selectorCollider))) {
                        b.draw(Assets.selector, (int) (selectorX), (int) (selectorY),
                                Tile.TILE_SIZE, Tile.TILE_SIZE);
                    // Otherwise, in range is false, but onTop can still be evaluated.
                    } else {
                        inRange = false;
                        onTop = (selectorX - player.getX() < Tile.TILE_SIZE &&
                                selectorY - player.getY() < Tile.TILE_SIZE);
                        // Draw the appropriate selector to reflect the state of the BuildingManager.
                        if (onTop)
                            b.draw(Assets.jumpSelector, (int) (selectorX), (int) (selectorY),
                                    Tile.TILE_SIZE, Tile.TILE_SIZE);
                        else
                            b.draw(Assets.errorSelector, (int) (selectorX), (int) (selectorY),
                                    Tile.TILE_SIZE, Tile.TILE_SIZE);
                    }
                // The player is back building, so as long as it's in range, the selector is white.
                } else b.draw(Assets.selector, (int) (selectorX), (int) (selectorY),
                            Tile.TILE_SIZE, Tile.TILE_SIZE);
            // Otherwise, the player is completely out of bounds. Render the transparent selector.
            } else {
                inRange = false;
                onTop = false;
                b.draw(Assets.transSelector, (int) (selectorX), (int) (selectorY),
                        Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
    }
}
