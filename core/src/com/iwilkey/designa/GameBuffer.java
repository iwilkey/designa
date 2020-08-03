package com.iwilkey.designa;

import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.world.World;

public class GameBuffer {

    private final Game game;
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
    public Camera getCamera() { return game.getCamera(); }

}
