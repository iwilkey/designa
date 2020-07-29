package com.iwilkey.designa.states;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.gfx.Text;
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

    }

    @Override
    public void tick() {
        world.tick();
    }

    @Override
    public void render(Batch b) {
        world.render(b);
        Text.draw(b, "Designa demo", 14, Game.h - 14 - 8);
    }

    @Override
    public void dispose() {

    }

}
