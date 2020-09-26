package com.iwilkey.designa.inventory.technology.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.input.InputHandler;

import java.awt.Rectangle;

public abstract class SelectableItem {

    public boolean isSelected = false;
    public Rectangle collider;

    public SelectableItem(int x, int y, int w, int h) {
        collider = new Rectangle(x, y, w, h);
    }

    public abstract void onClick();
    public abstract void render(Batch b);

}
