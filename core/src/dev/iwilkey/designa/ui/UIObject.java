package dev.iwilkey.designa.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.gfx.Camera;
import dev.iwilkey.designa.input.InputHandler;

import java.awt.*;

public abstract class UIObject {

    public enum UIObjectType {
        TEXT, BUTTON, SCROLLABLE;
    }

    public static float XSCALE, YSCALE;

    public final Rectangle relRect;
    public int x, y, width, height;
    public Rectangle collider;
    public boolean hovering;
    public UIObjectType type;

    public UIObject(UIObjectType type, int x, int y, int width, int height) {
        this.type = type;
        relRect = new Rectangle(x, y, width, height);
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        if(this.type == UIObjectType.TEXT) collider = new Rectangle(x, y + height, width, -height);
        else collider = new Rectangle(x, y, width, height);
        hovering = false;
    }

    public abstract void tick();
    public abstract void render(Batch b);
    public abstract void onClick();

    public void onResize(int width, int height) {
        XSCALE = (float)width / Camera.GW;
        YSCALE = (float)height / Camera.GH;
        collider.height = (int)(relRect.height * YSCALE);
        collider.width = (int)(relRect.width * XSCALE);
        collider.x = (int)(relRect.x * XSCALE);
        collider.y = (int)(relRect.y * YSCALE);
    }

    public void onMouseMove() { hovering = collider.contains(InputHandler.cursorX, InputHandler.cursorY); }
    public void onMouseRelease() { if(hovering) onClick(); }

}
