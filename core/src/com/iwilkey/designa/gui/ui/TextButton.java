package com.iwilkey.designa.gui.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;

public class TextButton extends UIObject {

    private String text;
    private ClickListener clicker;
    private boolean playClick = false;

    public TextButton(float x, float y, int h, String message, ClickListener listener) {
        super(x - (int)(message.length() * h / 2), y, message.length() * h, h);
        this.text = message;
        this.clicker = listener;
    }

    @Override
    public void tick() {
        if(hovering && !playClick) {
            Assets.invClick.play(0.12f);
            playClick = true;
        } else if (!hovering && playClick) playClick = false;
    }

    @Override
    public void render(Batch b) {
        if(hovering) Text.draw(b, text, (int)x - (6 * (text.length() / 2)), (int) y - (3), h + 6);
        else Text.draw(b, text, (int)x, (int) y, h);
    }

    @Override
    public void onClick() {
        Assets.createItem[MathUtils.random(0, 2)].play(0.35f);
        clicker.onClick();
    }
}
