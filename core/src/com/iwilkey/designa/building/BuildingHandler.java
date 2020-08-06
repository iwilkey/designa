package com.iwilkey.designa.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.creature.Player;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.tiles.tiletypes.AirTile;
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
        selectorX = (pointerOnTileX() * Tile.TILE_SIZE);
        selectorY = (pointerOnTileY() * Tile.TILE_SIZE);

        // Building and Destroying
        if(inRange && !onTop) {

            // Controlling the building will be different depending on the platform...
            // TODO: Implement these controls within the input manager and check if @bool isBuilding is on or something.
            switch(Gdx.app.getType()) {
                case Desktop:
                    // TODO: Replace @param ID '2' with ID of block selected in inventory.
                    if (InputHandler.rightMouseButtonDown) {
                        checkFace();
                        placeTile(4, pointerOnTileX(), pointerOnTileY());
                        gb.getWorld().getLightManager().bakeLighting();
                        // gb.getWorld().getLightManager().addLight(pointerOnTileX(), pointerOnTileY(), 6);
                    }

                    if (InputHandler.leftMouseButtonDown) {
                        checkFace();
                        damageTile(pointerOnTileX(), pointerOnTileY());
                        gb.getWorld().getLightManager().bakeLighting();
                        // gb.getWorld().getLightManager().addLight(pointerOnTileX(), pointerOnTileY(), 6);
                    }

                    break;

                case iOS:
                    break;

                case Android:
                    break;
            }

        } else if (onTop) {
            if (InputHandler.rightMouseButtonDown) {
                checkFace();
                gb.getWorld().getEntityHandler().getPlayer().jump();
                placeTile(2, pointerOnTileX(), pointerOnTileY());
                gb.getWorld().getLightManager().bakeLighting();
                // gb.getWorld().getLightManager().addLight(pointerOnTileX(), pointerOnTileY(), 6);
            }
        }

        selectorCollider.x = (int) selectorX;
        selectorCollider.y = (int) selectorY;
    }

    private void checkFace() {
        if(pointerOnTileX() * Tile.TILE_SIZE - player.getX() > 0) player.setFace(1);
        else player.setFace(0);
    }

    private void placeTile(int id, int x, int y) {
        if(gb.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof AirTile) {
            World.tiles[x][(World.h - y) - 1] = id;
            gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(id);
        }
    }

    private void damageTile(int x, int y) {
        if(!(gb.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof AirTile)) {
            gb.getWorld().tileBreakLevel[x][(World.h - y) - 1]--;

            if(gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] <= 0) {
                World.tiles[x][(World.h - y) - 1] = 0;
                gb.getWorld().tileBreakLevel[x][(World.h - y) - 1] = Tile.getStrength(0);
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

        if(Math.abs(selectorX - player.getX()) < 1.5 * Tile.TILE_SIZE &&
                Math.abs(selectorY - (player.getY() + 8)) < 2 * Tile.TILE_SIZE) {
            inRange = true;
            onTop = false;

            if(!(gb.getWorld().getEntityHandler().getPlayer().getCollisionBounds(0f,0f).intersects(selectorCollider))) {
                b.draw(Assets.selector, (int)(selectorX), (int)(selectorY),
                        Tile.TILE_SIZE, Tile.TILE_SIZE);
            } else {
                inRange = false;
                onTop = (selectorX - player.getX() < Tile.TILE_SIZE &&
                        selectorY - player.getY() < Tile.TILE_SIZE);
                if(onTop)
                    b.draw(Assets.jumpSelector, (int)(selectorX), (int)(selectorY),
                            Tile.TILE_SIZE, Tile.TILE_SIZE);
                else
                    b.draw(Assets.errorSelector, (int)(selectorX), (int)(selectorY),
                        Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        } else {
            inRange = false;
            onTop = false;
            b.draw(Assets.transSelector, (int)(selectorX), (int)(selectorY),
                    Tile.TILE_SIZE, Tile.TILE_SIZE);
        }

    }

}
