package com.iwilkey.designa.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;

import java.awt.Rectangle;

public class WorldSelectorState extends State {

    int w, h;
    Texture bg;

    @Override
    public void start() {

        InputHandler.initMainMenuStateInput();
        bg = new Texture("textures/mainmenu/mainmenubg.png");
        w = Gdx.graphics.getWidth(); h = Gdx.graphics.getHeight();

    }

    int bgOff = 0;
    boolean pos = true;
    @Override
    public void tick() {

        if(pos) bgOff += 1.0f;
        else bgOff -= 1.0f;
        if(bgOff >= 1400) pos = !pos;
        else if (bgOff <= 0) pos = !pos;

        if(InputHandler.leftMouseButtonDown) {

        }


    }

    @Override
    public void render(Batch b) {

        b.draw(bg, -50 - bgOff, 0, bg.getWidth() * 2, bg.getHeight() * 2);

        Text.draw(b, "Create World", (w / 2) - (12 * 46 / 2), 600, 46);
        Text.draw(b, "Load World", (w / 2) - (10 * 46 / 2), 450, 46);
        Text.draw(b, "Back", (w / 2) - (4 * 46 / 2), 300, 46);

        Text.draw(b, "Designa " + Assets.VERSION +
                " Created by Ian Wilkey", 10, 10, 11);

    }

    @Override
    public void onGUI(Batch b) {

    }

    @Override
    public void dispose() {

    }
}
