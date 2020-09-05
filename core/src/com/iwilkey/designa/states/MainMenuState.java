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
    Texture logo, playOff, playOn, quitOff, quitOn, settingsOff, settingsOn, bg;
    Rectangle playCollider, settingsCollider, quitCollider, cursor;
    boolean play, settings, quit;

    @Override
    public void start() {

        InputHandler.initMainMenuStateInput();

        logo = new Texture("textures/mainmenu/logo.png");
        playOff = new Texture("textures/mainmenu/buttons/play_off.png");
        playOn = new Texture("textures/mainmenu/buttons/play_on.png");
        quitOff = new Texture("textures/mainmenu/buttons/quit_off.png");
        quitOn = new Texture("textures/mainmenu/buttons/quit_on.png");
        settingsOff = new Texture("textures/mainmenu/buttons/settings_off.png");
        settingsOn = new Texture("textures/mainmenu/buttons/settings_on.png");
        bg = new Texture("textures/mainmenu/mainmenubg.png");

        w = Gdx.graphics.getWidth(); h = Gdx.graphics.getHeight();

        playCollider = new Rectangle((w / 2) - (playOff.getWidth() / 2), 275 + 120,
                playOff.getWidth(), playOff.getHeight());
        settingsCollider = new Rectangle((w / 2) - (settingsOff.getWidth() / 2), 190 + 120,
                settingsOff.getWidth(), settingsOff.getHeight());
        quitCollider = new Rectangle((w / 2) - (quitOff.getWidth() / 2), 105 + 120,
                quitOff.getWidth(), quitOff.getHeight());

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
        int up = 120;
        if(!fM) b.draw(logo, 0, 0, w, h);
        else {
            b.draw(bg, -50 - bgOff, 0, bg.getWidth() * 2, bg.getHeight() * 2);
            b.draw(logo, 0, 0 + up, w, h);
            if(play) b.draw(playOn, (w / 2) - (playOff.getWidth() / 2), 275 + up, playOff.getWidth(), playOff.getHeight());
            else b.draw(playOff, (w / 2) - (playOff.getWidth() / 2), 275 + up,
                    playOff.getWidth(), playOff.getHeight());

            if(settings) b.draw(settingsOn, (w / 2) - (settingsOff.getWidth() / 2), 190 + up, settingsOff.getWidth(), settingsOff.getHeight());
            else b.draw(settingsOff, (w / 2) - (settingsOff.getWidth() / 2), 190 + up,
                    settingsOff.getWidth(), settingsOff.getHeight());

            if(quit) b.draw(quitOn, (w / 2) - (quitOff.getWidth() / 2), 105 + up, quitOff.getWidth(), quitOff.getHeight());
            else b.draw(quitOff, (w / 2) - (quitOff.getWidth() / 2), 105 + up,
                    quitOff.getWidth(), quitOff.getHeight());

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
