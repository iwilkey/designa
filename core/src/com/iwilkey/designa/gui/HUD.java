package com.iwilkey.designa.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.utils.Null;
import com.iwilkey.designa.Game;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.building.BuildingHandler;
import com.iwilkey.designa.entities.creature.passive.Player;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.ToolSlot;
import com.iwilkey.designa.inventory.crate.Crate;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.tiles.Tile;

public class Hud {
    private final Player player;

    private int healthX, healthY;
    private final int heartSpacing = 16;

    public static int SELECTED_PIPE_DIRECTION = 0;

    public static boolean gameMenu = false;

    Texture resumeOff, resumeOn, settingsOff, settingsOn, saveOff, saveOn;

    public Hud(Player player) {

        resumeOff = new Texture("textures/game/resume_off.png");
        resumeOn = new Texture("textures/game/resume_on.png");
        settingsOff = new Texture("textures/mainmenu/buttons/settings_off.png");
        settingsOn = new Texture("textures/mainmenu/buttons/settings_on.png");
        saveOff = new Texture("textures/game/saveandquit_off.png");
        saveOn = new Texture("textures/game/saveandquit_on.png");

        this.player = player;
    }

    public void tick() {

        // heart
        int xx = (Game.w) - ((16 * 24) / 2) + 18;
        int yy = Game.h - 10 - 16;
        if(healthX != xx) healthX = xx;
        if(healthY != yy) healthY = yy;

        // Tool slot
        xx = (Game.w) - (player.getToolSlot().w) - 18;
        yy = (player.getToolSlot().h) - 48;
        if(player.getToolSlot().x != xx) player.getToolSlot().x = xx;
        if(player.getToolSlot().y != yy) player.getToolSlot().y = yy;

        input();
    }

    private void input() {
        try {
            if (ToolSlot.currentItem.getItem() != null) {
                if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.Pipe ||
                        ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.Offloader) {
                    if(InputHandler.pipeRotateRequest) {
                        SELECTED_PIPE_DIRECTION++;
                        if(SELECTED_PIPE_DIRECTION + 1 == 5) SELECTED_PIPE_DIRECTION = 0;
                        InputHandler.pipeRotateRequest = false;
                    }
                }
            }
        } catch (NullPointerException ignored) {}

        if(InputHandler.gameMenuRequest) {
            InputHandler.gameMenuRequest = false;
        }
    }

    public void render(Batch b) {

        renderHealthBar(b);
        player.getInventory().render(b);
        player.getToolSlot().render(b);
        if(player.crates.size() != 0) for(Crate crate : player.crates) {
            if(crate.isActive) crate.render(b);
        }

        if(BuildingHandler.backBuilding){
            b.draw(Assets.backBuilding[1], Game.w - 80, 72, 64, 64);
            Text.draw(b, "Back", Game.w - 80, 132, 11);
        }
        else {
            b.draw(Assets.backBuilding[0], Game.w - 80, 72, 64, 64);
            Text.draw(b, "Front", Game.w - 80, 132, 11);
        }

        Text.draw(b, Assets.VERSION + " " + Integer.toString(Game.tps) + " tps",
                14, Game.h - 14 - 8, 11);
        try {
            if (ToolSlot.currentItem.getItem() != null) {
                if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.Pipe ||
                        ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.Offloader) {
                    b.draw(Assets.arrow[SELECTED_PIPE_DIRECTION], Game.w - 80 + 14, 160, 32, 32);
                }
            }
        } catch (NullPointerException ignored) {}

        if(Inventory.active) Text.draw(b, "Inventory", 350, Game.h - 160, 11);
        else Text.draw(b, "press 'F' to open inventory", 14, 14, 8);

        if(gameMenu) {
            b.draw(Assets.blueprintGUI, (Game.w / 2) - (400 / 2), (Game.h / 2) - (600 / 2), 400, 600);
            b.draw(resumeOff, (Game.w / 2) - (resumeOff.getWidth() * 0.50f / 2), (Game.h / 2) -
                            (resumeOff.getHeight() * 0.50f / 2) + 100 - 40,
                resumeOff.getWidth() * 0.50f, resumeOff.getHeight() * 0.50f);
            b.draw(settingsOff, (Game.w / 2) - (settingsOff.getWidth() * 0.50f / 2), (Game.h / 2) -
                            (settingsOff.getHeight() * 0.50f / 2),
                    settingsOff.getWidth() * 0.50f, settingsOff.getHeight() * 0.50f);
            b.draw(saveOff, (Game.w / 2) - (saveOff.getWidth() * 0.50f / 2), (Game.h / 2) -
                            (saveOff.getHeight() * 0.50f / 2) - 60,
                    saveOff.getWidth() * 0.50f, saveOff.getHeight() * 0.50f);
        }
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
