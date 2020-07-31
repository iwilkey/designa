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

import java.awt.Rectangle;

public class BuildingHandler {

    private GameBuffer gb;
    private Player player;
    private float selectorX = 0, selectorY = 0;
    private boolean inRange = false;
    private Rectangle selectorCollider;

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
        if(inRange) {

            // Controlling the building will be different depending on the platform...
            switch(Gdx.app.getType()) {
                case Desktop:
                    // TODO: Replace '2' with ID of block selected in inventory.
                    if (InputHandler.rightMouseButtonDown) placeTile(2, pointerOnTileX(), pointerOnTileY());

                    // If right mouse button was just clicked...
                    if (InputHandler.leftMouseButtonDown) gb.getWorld().tiles[pointerOnTileX()]
                            [(gb.getWorld().h - pointerOnTileY()) - 1] = 0;

                    break;

                case iOS:
                    break;

                case Android:
                    break;
            }


        }

        selectorCollider.x = (int) selectorX;
        selectorCollider.y = (int) selectorY;
    }

    private void placeTile(int id, int x, int y) {
        if(gb.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof AirTile) {
            gb.getWorld().tiles[x][(gb.getWorld().h - y) - 1] = id;
            // gb.getWorld().tileBreakLevel[x][y] = Tile.getStrength(id);
        } else {
            return;
        }
    }

    /*
    private void damageTile(int x, int y) {
        if(!(gb.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof AirTile)) {
            gb.getWorld().tileBreakLevel[x][y]--;

            if(gb.getWorld().tileBreakLevel[x][y] <= 0) {
                gb.getWorld().tiles[x][y] = 0;
                gb.getWorld().tileBreakLevel[x][y] = Tile.getStrength(0);
            }

        } else {
            return;
        }
    }
     */

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
            Math.abs(selectorY - player.getY()) < 2 * Tile.TILE_SIZE) {
            inRange = true;

            if(!(gb.getWorld().getEntityHandler().getPlayer().getCollisionBounds(0f,0f).intersects(selectorCollider))) {
                b.draw(Assets.selector, (int)(selectorX), (int)(selectorY),
                        Tile.TILE_SIZE, Tile.TILE_SIZE);
            } else {
                inRange = false;
                b.draw(Assets.errorSelector, (int)(selectorX), (int)(selectorY),
                        Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        } else {
            inRange = false;
            b.draw(Assets.transSelector, (int)(selectorX), (int)(selectorY),
                    Tile.TILE_SIZE, Tile.TILE_SIZE);
        }

    }

}
