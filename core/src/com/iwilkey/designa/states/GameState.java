package com.iwilkey.designa.states;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.world.World;
import com.iwilkey.designa.world.WorldGeneration;

public class GameState extends State {

    private final GameBuffer gb;
    private final World world;

    public GameState(GameBuffer gb) {
        this.gb = gb;
        world = new World(gb, WorldGeneration.GenerateTerrain("World" + MathUtils.random(10, 100000), 300, 100));
        gb.setWorld(world);
    }

    @Override
    public void start() {
        InputHandler.initGameStateInput();
    }

    @Override
    public void tick() {
        gb.getCamera().tick();
        world.tick();
    }

    @Override
    public void render(Batch b) {
        world.render(b);
    }

    @Override
    public void onGUI(Batch b) {
        gb.getWorld().getEntityHandler().getPlayer().getHUD().render(b);
    }

    @Override
    public void dispose() {

    }

}
