package dev.iwilkey.designa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class UIText extends UIObject {

    public BitmapFont font;
    public int size;
    public String message;

    public UIText(String message, int size, int x, int y) {
        super(UIObjectType.TEXT, x, y, (size * message.length()) - (size * 4),
                -(int)((size*3) * (size / 128.0f)));
        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
        this.message = message; this.size = size;
        font.getData().setScale(size / 128.0f);
    }

    @Override
    public void render(Batch b) {
        font.getData().setScale(size / 128.0f);
        font.draw(b, message, x, y);
    }

    public void render(Batch b, int xx, int yy, int size) {
        font.getData().setScale(size / 128.0f);
        font.draw(b, message, xx, yy);
    }

    @Override
    public void tick() {}

    @Override
    public void onClick() {
        System.out.println("Click text");
    }

}
