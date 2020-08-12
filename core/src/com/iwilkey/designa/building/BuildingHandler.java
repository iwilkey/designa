package com.iwilkey.designa.building;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.creature.Player;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.ToolSlot;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.tiles.tiletypes.AirTile;
import com.iwilkey.designa.tiles.tiletypes.DirtTile;
import com.iwilkey.designa.tiles.tiletypes.GrassTile;
import com.iwilkey.designa.world.World;

import java.awt.Rectangle;

public class BuildingHandler {

    private final GameBuffer gb;
    private final Player player;
    private float selectorX = 0, selectorY = 0;
    private boolean inRange = false, onTop = false;
    private final Rectangle selectorCollider;

    public BuildingHandler(GameBuffer gb, Player p) {
        this.gb = gb;
        this.player = p;

        selectorCollider = new Rectangle((int) selectorX,
                (int) selectorY, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }

    public void tick() {
        setSelector();

        if (!Inventory.active) {

            if (inRange && !onTop) {

                if (InputHandler.placeRequest) {

                    if(ToolSlot.currentItem != null)
                        if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock)
                            placeTile(((ItemType.PlaceableBlock) ToolSlot.currentItem.getItem().getItemType()).getTileID(),
                                    pointerOnTileX(), pointerOnTileY());

                    InputHandler.placeRequest = false;
                }

                if (InputHandler.destroyRequest) {
                    damageTile(pointerOnTileX(), pointerOnTileY());
                    InputHandler.destroyRequest = false;
                }

            }  else if (onTop) {
                if (InputHandler.placeRequest) {
                    if(ToolSlot.currentItem != null)
                        if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock) {
                            placeTile(((ItemType.PlaceableBlock) ToolSlot.currentItem.getItem().getItemType()).getTileID(),
                                    pointerOnTileX(), pointerOnTileY());
                            gb.getWorld().getEntityHandler().getPlayer().jump();
                        }
                    InputHandler.placeRequest = false;
                }
            }
        }
    }

    private void setSelector() {
        selectorX = (pointerOnTileX() * Tile.TILE_SIZE);
        selectorY = (pointerOnTileY() * Tile.TILE_SIZE);
        selectorCollider.x = (int) selectorX;
        selectorCollider.y = (int) selectorY;
    }


    private void checkFace() {
        if(pointerOnTileX() * Tile.TILE_SIZE - player.getX() > 0) player.setFace(1);
        else player.setFace(0);
    }

    private void placeTile(int id, int x, int y) {
        checkFace();
        gb.getWorld().getLightManager().addLight(pointerOnTileX(), pointerOnTileY(), 8);
        if(gb.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof AirTile) {
            ToolSlot.currentItem.itemCount--;
            World.tiles[x][(World.h - y) - 1] = id;
            gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
            gb.getWorld().getLightManager().bakeLighting();
        }
    }

    private void damageTile(int x, int y) {
        checkFace();
        if(!(gb.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof AirTile)) {
            if(ToolSlot.currentItem != null) {
                if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.Drill) {
                    gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] -= ((ItemType.Drill) ToolSlot.currentItem.getItem().getItemType()).getStrength();
                    if (gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] <= 0) {
                        Tile tile = gb.getWorld().getTile(x, y);

                        World.getItemHandler().addItem(Item.getItemByID(tile.getItemID()).createNew(pointerOnTileX() * Tile.TILE_SIZE + 8,
                                pointerOnTileY() * Tile.TILE_SIZE + 8));

                        World.tiles[x][(World.h - y) - 1] = 0;
                        gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(0);
                        gb.getWorld().getLightManager().bakeLighting();
                    }
                }
            } else {
                if(gb.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof DirtTile ||
                        gb.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof GrassTile ||
                        ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock) {
                    gb.getWorld().tileBreakLevel[x][(World.h - y) - 1]--;
                    if (gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] <= 0) {
                        Tile tile = gb.getWorld().getTile(x, y);

                        World.getItemHandler().addItem(Item.getItemByID(tile.getItemID()).createNew(pointerOnTileX() * Tile.TILE_SIZE + 4,
                                pointerOnTileY() * Tile.TILE_SIZE + 6));

                        World.tiles[x][(World.h - y) - 1] = 0;
                        gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(0);
                        gb.getWorld().getLightManager().bakeLighting();
                    }
                }
            }
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
                inRange = false;
                onTop = false;
                b.draw(Assets.transSelector, (int) (selectorX), (int) (selectorY),
                        Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
    }
}
