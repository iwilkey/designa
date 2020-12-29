package dev.iwilkey.designa.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.audio.Audio;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.gfx.Camera;
import dev.iwilkey.designa.gfx.Geometry;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.inventory.Slot;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.ItemType;
import dev.iwilkey.designa.item.creator.ItemCreator;
import dev.iwilkey.designa.math.Maths;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.tile.TileType;
import dev.iwilkey.designa.world.World;

import java.awt.*;

public class BuildingHandler {

    public static boolean isBuilding;
    final float MAX_DIST_RANGE = 2 * Tile.TILE_SIZE,
        MAX_RENDER_RANGE = 4 * Tile.TILE_SIZE;

    boolean inRange, onTop, inRenderRange;
    short tileXSelected, tileYSelected;
    Player player;
    World world;

    public BuildingHandler(World world, Player player) {
        this.world = world;
        this.player = player;
        isBuilding = true;
    }

    public void tick() {
        isBuilding = !ItemCreator.isActive;
        if(isBuilding) {
            input();
            checkInRange();
            render();
        }
    }

    Rectangle selector = new Rectangle(0, 0, Tile.TILE_SIZE,Tile.TILE_SIZE);
    private void input() {

        tileXSelected = (short)pointerOnTileX();
        tileYSelected = (short)pointerOnTileY();
        selector.x = tileXSelected * Tile.TILE_SIZE;
        selector.y = tileYSelected * Tile.TILE_SIZE;

        if (InputHandler.placeTileRequest) {
            if (player.inventory.selectedSlot().item != null) {
                if (player.inventory.selectedSlot().item.getType()
                        instanceof ItemType.NonCreatableItem.PlaceableTile) {
                    if ((player.inventory.selectedSlot().count - 1 >= 0))
                        placeTile(((ItemType.NonCreatableItem.PlaceableTile)
                                        (player.inventory.selectedSlot().item.getType())).correspondingTile,
                                tileXSelected, tileYSelected);
                }
            }
            
            world.lightHandler.addLight(tileXSelected, tileYSelected, 10);
            
            InputHandler.placeTileRequest = false;
        }

        if(InputHandler.damageTileRequest) {
        	if(inRange) {
	        	Slot selected = player.inventory.selectedSlot();
	        	
	        	if(selected.tool != null) {	
	        		if(selected.tool.toolType instanceof ItemType.CreatableItem.Tool.Sickle) {
	        			if(world.getFrontTile(tileXSelected, tileYSelected) == Tile.STONE) {
	        				damageTile(selected.tool.toolType.efficiency, tileXSelected, tileYSelected);

                            if(Maths.percentChance(((ItemType.CreatableItem.Tool.Sickle) selected.tool.toolType).luck + 40))
	        					world.activeItemHandler.spawn(Item.ROCK, (tileXSelected * Tile.TILE_SIZE) +
	        							MathUtils.random(4, 8), (int)(tileYSelected * Tile.TILE_SIZE) +
                                        MathUtils.random(4, 12));
	        				
	        			} else damageTile(selected.tool.toolType.efficiency / 2, tileXSelected, tileYSelected);
	        			if(world.getFrontTile(tileXSelected, tileYSelected) != Tile.AIR) selected.tool.timesUsed++;

	        			if(world.getBackTile(tileXSelected,  tileYSelected).type instanceof TileType.Natural.Ore) {
	        			    if(Maths.percentChance(((ItemType.CreatableItem.Tool.Sickle) selected.tool.toolType).luck))
                                world.activeItemHandler.spawn(((TileType.Natural.Ore)(world.getBackTile(tileXSelected,  tileYSelected).type)).drop,
                                        (tileXSelected * Tile.TILE_SIZE) + MathUtils.random(0, 8), (tileYSelected * Tile.TILE_SIZE) + 4);
	        				selected.tool.timesUsed++;
	        			}

	        		}
	        	} else {
	        		damageTile(1, tileXSelected, tileYSelected);
	        		if(world.getFrontTile(tileXSelected, tileYSelected) == Tile.STONE) {
	        		    if(Maths.percentChance(40))
                            world.activeItemHandler.spawn(Item.ROCK, (tileXSelected * Tile.TILE_SIZE) +
                                    MathUtils.random(4, 8), (tileYSelected * Tile.TILE_SIZE) +
                                    MathUtils.random(4, 12));
	        		}
	        	}
	        	
        	}
        	InputHandler.damageTileRequest = false;
        }

    }

    float distFromPlayerX, distFromPlayerY;
    private void checkInRange() {

        distFromPlayerX = Math.abs((tileXSelected * Tile.TILE_SIZE) - (player.x - 11 + (player.width / 2f)));
        distFromPlayerY = Math.abs((tileYSelected * Tile.TILE_SIZE) - (player.y + (player.width / 2f)));

        inRange = (!player.collider.intersects(selector) && distFromPlayerX < MAX_DIST_RANGE && distFromPlayerY < MAX_DIST_RANGE) &&
            tileYSelected > 0;
        inRenderRange = (distFromPlayerX < MAX_RENDER_RANGE && distFromPlayerY < MAX_RENDER_RANGE);

    }

    public boolean placeTile(Tile tile, int x, int y) {
        if(!inRange) return false;
        if(world.getFrontTile(x, y) == Tile.AIR) {
            player.inventory.selectedSlot().count--;
            world.FRONT_TILES[x][y][0] = (byte)tile.getTileID();
            world.FRONT_TILES[x][y][1] = (byte)tile.getStrength();
            world.lightHandler.bake();
            return true;
        }
        return false;
    }

    public boolean damageTile(int amount, int x, int y) {
        if(!inRange || y < 1) return false;
        if(world.getFrontTile(x, y) != Tile.AIR) {
            world.FRONT_TILES[x][y][1] -= amount;
            checkBreak(x, y);
            return true;
        }
        return false;
    }

    public boolean checkBreak(int x, int y) {
        if(world.FRONT_TILES[x][y][1] <= 0) {
            world.activeItemHandler.spawn(Tile.getItemFromTile(world.getFrontTile(x, y)), (int)((x * Tile.TILE_SIZE) +
                    (Item.ITEM_WIDTH / 2f)) + MathUtils.random(-2, 2), (int)((y * Tile.TILE_SIZE) + (Item.ITEM_HEIGHT / 2f) + 2));
            world.FRONT_TILES[x][y][0] = (byte)Tile.AIR.getTileID();
            world.lightHandler.bake();
            return true;
        }
        return false;
    }

    public void render() {
        if(isBuilding) {
            if(!inRenderRange) return;
            if(inRange) {
                Geometry.requests.add(new Geometry.RectangleOutline(
                        tileXSelected * Tile.TILE_SIZE, tileYSelected * Tile.TILE_SIZE,
                        Tile.TILE_SIZE, Tile.TILE_SIZE, 6, Color.WHITE));
            } else {
                Geometry.requests.add(new Geometry.RectangleOutline(
                        tileXSelected * Tile.TILE_SIZE, tileYSelected * Tile.TILE_SIZE,
                        Tile.TILE_SIZE, Tile.TILE_SIZE, 6, Color.RED));
            }
        }
    }

    float scaleX, scaleY;
    private int pointerOnTileX() {
        scaleX = (Game.WINDOW_WIDTH / (float)Camera.GW);
        return (int) ((((InputHandler.cursorX / scaleX) - Camera.position.x) /
                Tile.TILE_SIZE) / Camera.scale.x);
    }

    private int pointerOnTileY() {
        scaleY = (Game.WINDOW_HEIGHT / (float)Camera.GH);
        return (int) ((((InputHandler.cursorY / scaleY) - Camera.position.y) /
                Tile.TILE_SIZE) / Camera.scale.y);
    }

}
