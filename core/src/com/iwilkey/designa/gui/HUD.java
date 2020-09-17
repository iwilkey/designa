package com.iwilkey.designa.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.iwilkey.designa.Game;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.building.BuildingHandler;
import com.iwilkey.designa.entities.creature.passive.Player;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.gui.ui.ClickListener;
import com.iwilkey.designa.gui.ui.TextButton;
import com.iwilkey.designa.gui.ui.UIManager;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.ToolSlot;
import com.iwilkey.designa.inventory.crate.Crate;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.states.State;
import com.iwilkey.designa.tiles.Tile;

public class Hud {

    private final Player player;
    private UIManager uiManager;
    private int healthX, healthY;
    private final int heartSpacing = 16;
    public static int SELECTED_PIPE_DIRECTION = 0;
    public static boolean gameMenu = false;

    public Hud(Player player) {
        this.player = player;
        uiManager = new UIManager();
        InputHandler.setUIManager(uiManager);

        uiManager.addObject(new TextButton(Gdx.graphics.getWidth() / 2,
                (Gdx.graphics.getHeight() / 2) + 30, 20, "Resume", new ClickListener() {
            @Override
            public void onClick() {
                gameMenu = false;
            }
        }));

        uiManager.addObject(new TextButton(Gdx.graphics.getWidth() / 2,
                (Gdx.graphics.getHeight() / 2) - 30, 20, "Save and Quit", new ClickListener() {
            @Override
            public void onClick() {
                gameMenu = false;
                player.getGameBuffer().getGame().setCamera(null);
                Camera.mat.setToTranslationAndScaling(new Vector3(0,0,0), new Vector3(1,1,1));
                player.getGameBuffer().getWorld().saveWorld();
                State.switchState(0);
            }
        }));

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

        if(gameMenu) uiManager.tick();

    }

    private void input() {
        try {
            if (ToolSlot.currentItem.getItem() != null) {
                if (ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.Pipe ||
                        ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.Node) {
                    if(InputHandler.pipeRotateRequest) {
                        SELECTED_PIPE_DIRECTION++;
                        if(SELECTED_PIPE_DIRECTION + 1 == 5) SELECTED_PIPE_DIRECTION = 0;
                        InputHandler.pipeRotateRequest = false;
                    }
                }
            }
        } catch (NullPointerException ignored) {}

        if(InputHandler.gameMenuRequest) {
            for(Crate crate : player.crates) {
                if(crate.isActive) {
                    crate.isActive = false;
                    InputHandler.gameMenuRequest = false;
                    return;
                }
            }
            gameMenu = true;
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
                        ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.PlaceableBlock.CreatableTile.Node) {
                    b.draw(Assets.arrow[SELECTED_PIPE_DIRECTION], Game.w - 80 + 14, 160, 32, 32);
                }
            }
        } catch (NullPointerException ignored) {}

        if(Inventory.active) Text.draw(b, "Inventory", 350, Game.h - 160, 11);
        else Text.draw(b, "press 'F' to open inventory", 14, 14, 8);

        if(gameMenu) {
            b.draw(Assets.blueprintGUI, (Game.w / 2) - (400 / 2), (Game.h / 2) - (600 / 2), 400, 600);
            uiManager.render(b);
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
