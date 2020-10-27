package dev.iwilkey.designa.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.input.InputHandler;

import java.awt.*;

public abstract class UIObject {

    public enum UIObjectType {
        TEXT, BUTTON;
    }

    public int x, y, width, height;
    public Rectangle collider;
    public boolean hovering;
    public UIObjectType type;

    public UIObject(UIObjectType type, int x, int y, int width, int height) {
        this.type = type;
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        if(this.type == UIObjectType.TEXT) collider = new Rectangle(x, y + height, width, -height);
        else collider = new Rectangle(x, y, width, height);
        hovering = false;
    }

    public abstract void tick();
    public abstract void render(Batch b);
    public abstract void onClick();

    float xDiff, yDiff;
    public void onResize(int width, int height) {
        xDiff = (float)width / Game.WINDOW_WIDTH;
        yDiff = (float)height / Game.WINDOW_HEIGHT;
        collider.height = (int)Math.ceil(collider.height * yDiff);
        collider.width = (int)Math.ceil(collider.width * xDiff);
        collider.x = (int)Math.ceil(collider.x * xDiff);
        collider.y = (int)Math.ceil(collider.y * yDiff);
    }

    public void onMouseMove() { hovering = collider.contains(InputHandler.cursorX, InputHandler.cursorY); }
    public void onMouseRelease() { if(hovering) onClick(); }

}
