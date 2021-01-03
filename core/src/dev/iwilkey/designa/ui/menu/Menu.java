package dev.iwilkey.designa.ui.menu;

import com.badlogic.gdx.graphics.g2d.Batch;

import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.ui.*;

public abstract class Menu {

    final short max = 10;
    public short timer;

    UIManager manager;
    public int level;

    public Menu(UIManager manager) {
        this.manager = manager;
    }

    public void tick() {

        if(timer < 110) {
            timer++;
        }

        if(InputHandler.mouseCurrentlyMoving) onMouseMove();
        if(InputHandler.leftMouseButtonUp) onMouseRelease();

        if(manager.texts != null) {
            for(UIText t : manager.texts) {
                if(t.tag != level && t.tag != -1) continue;
                t.tick();
            }
        }

        if(manager.buttons != null) {
            for(UIButton b : manager.buttons) {
                if(b.tag != level && b.tag != -1) continue;
                b.tick();
            }
        }
    }

    public void render(Batch b) {

        if(manager.texts != null) {
            for(UIText t : manager.texts) {
                if(t.tag != level && t.tag != -1) continue;
                t.render(b);
            }
        }


        if(manager.buttons != null) {
            for(UIButton bb : manager.buttons) {
                if(bb.tag != level && bb.tag != -1) continue;
                bb.render(b);
            }
        }
    }

    public void onMouseMove() {
        if(manager.buttons != null) {
            for(UIButton bb : manager.buttons) {
                if(bb.tag != level && bb.tag != -1) continue;
                bb.onMouseMove();
            }
        }
    }

    public void onMouseRelease() {
        if(manager.buttons != null) {
            for(UIButton bb : manager.buttons) {
                if(timer < max) return;
                if(bb.tag != level && bb.tag != -1) continue;
                bb.onMouseRelease();
            }
        }
    }

    public void onResize(int width, int height) {
        if(manager.texts != null) for(UIText t : manager.texts) t.onResize(width, height);
        if(manager.buttons != null) for(UIButton b : manager.buttons) b.onResize(width, height);
    }

}
