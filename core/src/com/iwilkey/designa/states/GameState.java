package com.iwilkey.designa.states;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.world.World;

public class GameState extends State {

    private GameBuffer gb;
    private World world;

    public GameState(GameBuffer gb) {
        this.gb = gb;
        world = new World(gb, "worlds/testworld.txt");
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
