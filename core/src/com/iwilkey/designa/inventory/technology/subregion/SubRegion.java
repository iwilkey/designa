package com.iwilkey.designa.inventory.technology.subregion;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class SubRegion {

    public int x, y, w, h;
    public String name;

    public SubRegion(String name, int x, int y, int w, int h) {
        this.name = name; this.x = x; this.y = y; this.w = w; this.h = h;
    }

    public abstract void tick();
    public abstract void render(Batch b);
    public abstract void renderBorder(Batch b);

}
