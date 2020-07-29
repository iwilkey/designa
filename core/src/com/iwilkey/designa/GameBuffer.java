package com.iwilkey.designa;

import com.iwilkey.designa.world.World;

public class GameBuffer {

    private Game game;
    private World world;

    public GameBuffer(Game game) {
        this.game = game;
        this.world = null;
    }

    public void setWorld(World w) {
        this.world = w;
    }

    public Game getGame() {
        return game;
    }

    public World getWorld() {
        return world;
    }

}
