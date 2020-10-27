package dev.iwilkey.designa.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.gfx.Camera;
import dev.iwilkey.designa.gfx.Geometry;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.world.World;

import java.awt.Rectangle;

public class BuildingHandler {

    public static boolean isBuilding;
    final float MAX_DIST_RANGE = 2 * Tile.TILE_SIZE;

    boolean inRange, onTop;
    short tileXSelected, tileYSelected;
    Player player;
    World world;

    public BuildingHandler(World world, Player player) {
        this.world = world;
        this.player = player;
        isBuilding = true;
    }

    public void tick() {
        if(isBuilding) {
            input();
            checkInRange();
            render();
        }
    }

    Rectangle selector = new Rectangle(0,0, Tile.TILE_SIZE, Tile.TILE_SIZE);
    private void input() {

        tileXSelected = (short)pointerOnTileX();
        tileYSelected = (short)pointerOnTileY();
        selector.x = tileXSelected * Tile.TILE_SIZE;
        selector.y = tileYSelected * Tile.TILE_SIZE;

        if (InputHandler.placeTileRequest) {
            placeTile(Tile.DIRT, tileXSelected, tileYSelected);
            InputHandler.placeTileRequest = false;
        }

        if(InputHandler.damageTileRequest) {
            damageTile(tileXSelected, tileYSelected);
            world.activeItemHandler.spawn(Item.DIRT, tileXSelected * Tile.TILE_SIZE + MathUtils.random(-4, 4), tileYSelected * Tile.TILE_SIZE);
            InputHandler.damageTileRequest = false;
        }

    }

    float distFromPlayerX, distFromPlayerY;
    private void checkInRange() {

        distFromPlayerX = Math.abs((tileXSelected * Tile.TILE_SIZE) - (player.x - 11 + (player.width / 2f)));
        distFromPlayerY = Math.abs((tileYSelected * Tile.TILE_SIZE) - (player.y + (player.width / 2f)));

        inRange = (!player.collider.intersects(selector) && distFromPlayerX < MAX_DIST_RANGE && distFromPlayerY < MAX_DIST_RANGE) &&
            tileYSelected > 0;

    }

    public boolean placeTile(Tile tile, int x, int y) {
        if(!inRange) return false;
        if(world.getTile(x, y) == Tile.AIR) {
            world.FRONT_TILES[x][y] = (byte)tile.getTileID();
            world.lightHandler.addLight(x, y, 10);
            world.lightHandler.bake();
            return true;
        }
        return false;
    }

    public boolean damageTile(int x, int y) {
        if(!inRange || y < 1) return false;
        if(world.getTile(x, y) != Tile.AIR) {
            world.FRONT_TILES[x][y] = (byte)Tile.AIR.getTileID();
            world.lightHandler.removeLight(x, y);
            world.lightHandler.bake();
            return true;
        }
        return false;
    }

    public void render() {
        if(isBuilding) {
            if(inRange) {
                Geometry.requests.add(new Geometry.RectangleOutline(
                        tileXSelected * Tile.TILE_SIZE, tileYSelected * Tile.TILE_SIZE,
                        Tile.TILE_SIZE, Tile.TILE_SIZE, 2, Color.WHITE));
            } else {
                Geometry.requests.add(new Geometry.RectangleOutline(
                        tileXSelected * Tile.TILE_SIZE, tileYSelected * Tile.TILE_SIZE,
                        Tile.TILE_SIZE, Tile.TILE_SIZE, 2, Color.RED));
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
