package com.iwilkey.designa.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;
import com.iwilkey.designa.world.WorldGeneration;

public class GameState extends State {

    public static boolean hasLost = false;
    private static World world;

    @Override
    public void start() { InputHandler.initGameStateInput(); }

    public static void loadWorld(String path) {
        world = new World(Game.gb, path);
        Game.gb.setWorld(world);
        Game.gb.getGame().setCamera(new Camera(Game.gb, (World.w / 2) * Tile.TILE_SIZE,
                LightManager.highestTile[World.w / 2] * Tile.TILE_SIZE));
    }

    public static void createNewWorld(String name) {
        world = new World(Game.gb, WorldGeneration.initWorld(name, 3000, 100));
        Game.gb.setWorld(world);
        Game.gb.getGame().setCamera(new Camera(Game.gb, (World.w / 2) * Tile.TILE_SIZE,
                LightManager.highestTile[World.w / 2] * Tile.TILE_SIZE));
    }

    @Override
    public void tick() {
        Game.gb.getCamera().tick();
        world.tick();

        if(hasLost) {
            if(InputHandler.escapeRequest) {
                InputHandler.escapeRequest = false;
                InputHandler.gameMenuRequest = false;
                World.getEntityHandler().getPlayer().getGameBuffer().getGame().setCamera(null);
                Camera.mat.setToTranslationAndScaling(new Vector3(0,0,0), new Vector3(1,1,1));
                World.getEntityHandler().getPlayer().getGameBuffer().getWorld().saveWorld();
                State.switchState(0);
            }
        }
    }

    @Override
    public void render(Batch b) {
        world.render(b);
    }

    @Override
    public void onGUI(Batch b) {
        World.getEntityHandler().getPlayer().getHUD().render(b);
        if(hasLost) {
            Text.draw(b, "You died.", (Gdx.graphics.getWidth() / 2) -
                    (("You died.".length() * 32) / 2), (Gdx.graphics.getHeight() / 2) + 18, 32);
            Text.draw(b, "Press 'ESC'", (Gdx.graphics.getWidth() / 2) -
                    (("Press 'ESC'".length() * 32) / 2), (Gdx.graphics.getHeight() / 2) - 18, 32);
        }
    }

    @Override
    public void dispose() {}

}
