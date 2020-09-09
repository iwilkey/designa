package com.iwilkey.designa.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.Game;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.gui.ui.ClickListener;
import com.iwilkey.designa.gui.ui.TextButton;
import com.iwilkey.designa.gui.ui.UIManager;
import com.iwilkey.designa.input.InputHandler;

import java.awt.Rectangle;

public class WorldSelectorState extends State {

    int w, h;
    Texture bg;
    private UIManager uiManager;

    @Override
    public void start() {

        InputHandler.initMainMenuStateInput();
        bg = new Texture("textures/mainmenu/mainmenubg.png");
        w = Gdx.graphics.getWidth(); h = Gdx.graphics.getHeight();

        uiManager = new UIManager();
        InputHandler.setUIManager(uiManager);

        int yButton = 500;

        uiManager.addObject(new TextButton((w / 2), yButton, 46, "Create World", new ClickListener() {
            @Override
            public void onClick() {

            }
        }));
        uiManager.addObject(new TextButton((w / 2), yButton - (100), 46, "Load World", new ClickListener() {
            @Override
            public void onClick() {

            }
        }));
        uiManager.addObject(new TextButton((w / 2), yButton - (100 * 2), 46, "Back", new ClickListener() {
            @Override
            public void onClick() {
                switchState(0);
            }
        }));

    }

    int bgOff = 0;
    boolean pos = true;
    @Override
    public void tick() {

        uiManager.tick();

        if(pos) bgOff += 1.0f;
        else bgOff -= 1.0f;
        if(bgOff >= 1400) pos = !pos;
        else if (bgOff <= 0) pos = !pos;

    }

    @Override
    public void render(Batch b) {

        b.draw(bg, -50 - bgOff, 0, bg.getWidth() * 2, bg.getHeight() * 2);
        uiManager.render(b);
        Text.draw(b, "Designa " + Assets.VERSION, 10, 10, 11);

    }

    @Override
    public void onGUI(Batch b) {

    }

    @Override
    public void dispose() {

    }
}
