package com.iwilkey.designa.gui.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.input.InputHandler;

import java.awt.*;

public abstract class UIObject {

    protected float x, y;
    protected int w, h;
    protected Rectangle bounds;
    protected boolean hovering;

    public UIObject(float x, float y, int w, int h) {
        this.x = x; this.y = y;
        this.w = w; this.h = h;
        bounds = new Rectangle((int)x, (int)y, w, h);
    }

    public abstract void tick();
    public abstract void render(Batch b);
    public abstract void onClick();
    public abstract void onKeyDown(int key);

    public void onMouseMove() { hovering = bounds.contains(InputHandler.cursorX, InputHandler.cursorY); }
    public void onMouseRelease() { if(hovering) onClick(); }

    // Getters and setters

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getW() { return w; }
    public void setW(int w) { this.w = w; }
    public float getH() { return h; }
    public void setH(int h) { this.h = h; }
    public boolean isHovering() { return hovering; }
    public void setHovering(boolean hovering) { this.hovering = hovering; }

}
