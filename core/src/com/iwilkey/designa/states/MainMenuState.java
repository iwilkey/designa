package com.iwilkey.designa.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.gui.ui.ClickListener;
import com.iwilkey.designa.gui.ui.TextButton;
import com.iwilkey.designa.gui.ui.UIManager;
import com.iwilkey.designa.input.InputHandler;

public class MainMenuState extends State {

    int w, h;
    Texture logo, bg;
    private UIManager uiManager;

    @Override
    public void start() {
        timer = 0; fM = false;

        InputHandler.initMainMenuStateInput();

        logo = new Texture("textures/mainmenu/logo.png");
        bg = new Texture("textures/mainmenu/mainmenubg.png");
        w = Gdx.graphics.getWidth(); h = Gdx.graphics.getHeight();

        uiManager = new UIManager();
        InputHandler.setUIManager(uiManager);

        int yButton = 425;

        uiManager.addObject(new TextButton((w / 2), yButton, 46, "Play", new ClickListener() {
            @Override
            public void onClick() {
                switchState(1);
            }
        }));
        uiManager.addObject(new TextButton((w / 2), yButton - (100), 46, "Settings", new ClickListener() {
            @Override
            public void onClick() {

            }
        }));
        uiManager.addObject(new TextButton((w / 2), yButton - (100 * 2), 46, "Quit", new ClickListener() {
            @Override
            public void onClick() {
                System.exit(-1);
            }
        }));

    }

    int bgOff = 0;
    boolean pos = true, fM = false;
    long timer = 0, fullMenu = 200;
    @Override
    public void tick() {
        if(timer >= fullMenu) fM = true;
        else {
            timer++;
            return;
        }

        uiManager.tick();

        if(pos) bgOff += 1.0f;
        else bgOff -= 1.0f;
        if(bgOff >= 1400) pos = !pos;
        else if (bgOff <= 0) pos = !pos;

    }

    @Override
    public void render(Batch b) {
        int up = 170;
        if(!fM) b.draw(logo, 0, 0, w, h);
        else {
            b.draw(bg, -50 - bgOff, 0, bg.getWidth() * 2, bg.getHeight() * 2);
            b.draw(logo, 0, up, w, h);
            uiManager.render(b);
            Text.draw(b, "Designa " + Assets.VERSION, 10, 10, 11);
        }

    }

    @Override
    public void onGUI(Batch b) {

    }

    @Override
    public void dispose() {

    }
}
