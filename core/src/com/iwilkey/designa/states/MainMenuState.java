package com.iwilkey.designa.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;

import java.awt.Rectangle;

public class MainMenuState extends State {

    int w, h;
    Texture logo, bg;
    Rectangle playCollider, settingsCollider, quitCollider, cursor;
    boolean play, settings, quit;

    @Override
    public void start() {

        InputHandler.initMainMenuStateInput();

        logo = new Texture("textures/mainmenu/logo.png");
        bg = new Texture("textures/mainmenu/mainmenubg.png");

        w = Gdx.graphics.getWidth(); h = Gdx.graphics.getHeight();

        playCollider = new Rectangle((w / 2) - (72 * 4 / 2), 275 + 150,
                72 * 4, 60);
        settingsCollider = new Rectangle((w / 2) - (46 * 8 / 2), 190 + 150,
                46 * 8, 60);
        quitCollider = new Rectangle((w / 2) - (46 * 4 / 2), 105 + 150,
                46 * 4, 60);

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

        if(pos) bgOff += 1.0f;
        else bgOff -= 1.0f;
        if(bgOff >= 1400) pos = !pos;
        else if (bgOff <= 0) pos = !pos;

        cursor = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
        play = cursor.intersects(playCollider);
        settings = cursor.intersects(settingsCollider);
        quit = cursor.intersects(quitCollider);

        if(InputHandler.leftMouseButtonDown) {

            if(play) {
                Game.getStates().get(1).start();
                State.setState(Game.getStates().get(1));
            }

            if(quit) System.exit(-1);

        }

    }

    @Override
    public void render(Batch b) {
        int up = 150;
        if(!fM) b.draw(logo, 0, 0, w, h);
        else {
            b.draw(bg, -50 - bgOff, 0, bg.getWidth() * 2, bg.getHeight() * 2);
            b.draw(logo, 0, up, w, h);

            if(play) Text.draw(b, "PLAY", (w / 2) - (72 * 4 / 2), 275 + up, 72);
            else Text.draw(b, "PLAY", (w / 2) - (46 * 4 / 2), 275 + up, 46);

            if(settings) Text.draw(b, "SETTINGS", (w / 2) - (46 * 8 / 2), 190 + up, 46);
            else Text.draw(b, "SETTINGS", (w / 2) - (32 * 8 / 2), 190 + up, 32);

            if(quit) Text.draw(b, "Quit", (w / 2) - (46 * 4 / 2), 105 + up, 46);
            else Text.draw(b, "Quit", (w / 2) - (32 * 4 / 2), 105 + up, 32);

            Text.draw(b, "Designa " + Assets.VERSION +
                    " Created by Ian Wilkey", 10, 10, 11);
        }

    }

    @Override
    public void onGUI(Batch b) {

    }

    @Override
    public void dispose() {

    }
}
