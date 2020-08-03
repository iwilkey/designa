package com.iwilkey.designa.gui;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.creature.Player;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.tiles.Tile;

public class HUD {
    private final Player player;

    private int healthX, healthY;
    private final int heartSpacing = 16;

    public HUD(Player player) {
        this.player = player;
    }

    public void tick() {

        // heart
        int xx = (Game.w) - ((16 * 24) / 2) + 18;
        int yy = Game.h - 10 - 16;
        if(healthX != xx) healthX = xx;
        if(healthY != yy) healthY = yy;

    }

    public void render(Batch b) {
        renderHealthBar(b);
        Text.draw(b, "Designa", 14, Game.h - 14 - 8);
    }

    private void renderHealthBar(Batch b) {
        for (int i = 0; i < 10; i++) {
            if (player.getHealth() >= i + 1) {
                b.draw(Assets.heart[0], healthX + (i * heartSpacing),
                        healthY, Tile.TILE_SIZE, Tile.TILE_SIZE);
            } else {
                b.draw(Assets.heart[1], healthX + (i * heartSpacing),
                        healthY, Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
    }
}
