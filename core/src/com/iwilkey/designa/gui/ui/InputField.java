package com.iwilkey.designa.gui.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;

import java.util.Scanner;

public class InputField extends UIObject {

    private String text;
    private boolean activeTyping, cursorVis, playClick = false;
    private int cursor = 0, textSize = 0;
    Scanner scanner;

    public InputField(float x, float y, int w, int h) {
        super(x - (w / 2), y, w, h);
        text = "";
        textSize = (h - 48 <= 0) ? h : h - 48;
        activeTyping = false;
        scanner = new Scanner(System.in);
    }

    long timer = 0, cursorLife = 32;
    @Override
    public void tick() {

        if(hovering && !playClick) {
            Assets.invClick.play(0.12f);
            playClick = true;
        } else if (!hovering && playClick) playClick = false;

        if(activeTyping) {
            cursor = (int)x + text.length() * (textSize + 1);
            timer++;
            if(timer > cursorLife) {
                cursorVis = !cursorVis;
                timer = 0;
            }
        } else timer = 0;
    }

    @Override
    public void render(Batch b) {

        if(hovering && !activeTyping) {
            Text.draw(b, text, (int)(x + (w / 2)) - (textSize * text.length() / 2) - 22, (int)y + 10, textSize);
            b.draw(Assets.selector, x - 16, y, w + 32, 4);
        } else if(activeTyping) {
            if(cursorVis) b.draw(Assets.selector, cursor, y + 6, 1, h - 24);
            Text.draw(b, text, (int)x, (int)y + 10, textSize);
            b.draw(Assets.selector, x - 16, y, w + 32, 4);
        } else {
            Text.draw(b, text, (int)(x + (w / 2)) - (textSize * text.length() / 2) - 22, (int)y + 10, textSize);
            b.draw(Assets.selector, x, y, w, 4);
        }
    }

    @Override
    public void onClick() {
        Assets.createItem[MathUtils.random(0, 2)].play(0.35f);
        if(hovering) activeTyping = !activeTyping;
        else activeTyping = false;
    }

    @Override
    public void onKeyDown(int key) {
        String allowed = "abcdefghijklmnopqrstuvwxyz 0123456789.!?,'";
        if(activeTyping) {
            if(key == Input.Keys.ENTER) {
                Assets.createItem[MathUtils.random(0, 2)].play(0.35f);
                activeTyping = false;
            }
            if(key == Input.Keys.BACKSPACE) { if(text.length() - 1 >= 0) text = text.substring(0, text.length() - 1); }
            if(text.length() * textSize <= w - (4 * textSize)) {
                if (key == Input.Keys.SPACE) text += " ";
                if (allowed.contains(Input.Keys.toString(key).toLowerCase())) text += Input.Keys.toString(key);
                Assets.invClick.play(0.08f);
            }
        }
    }

    public String getText() { return text; }
}
