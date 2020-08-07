package com.iwilkey.designa.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;

public class ToolSlot {

    private Inventory inventory;
    public int x, y;
    public final int w, h;

    public ToolSlot(Inventory i) {
        this.inventory = i;
        w = 64; h = 64;
    }

    public void tick() {}

    public void render(Batch b) {
        b.draw(Assets.toolSlot, x, y, w, h);
    }

}
