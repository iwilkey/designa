package dev.iwilkey.designa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.Settings;

public class UIText extends UIObject {

    public BitmapFont font;
    public int size, tag;
    public String message;

    public UIText(String message, int size, int x, int y) {
        super(UIObjectType.TEXT, x, y, (Settings.GUI_FONT_SIZE * message.length()) - (size * 4),
                -(int)((size*3) * ((Settings.GUI_FONT_SIZE + size) / 128.0f)));
        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
        this.message = message; this.size = size;
        font.getData().setScale((Settings.GUI_FONT_SIZE + size) / 128.0f);
        this.tag = -1;
    }

    public UIText(String message, int tag, int size, int x, int y) {
        super(UIObjectType.TEXT, x, y, (Settings.GUI_FONT_SIZE * message.length()) - (size * 4),
                -(int)((size*3) * ((Settings.GUI_FONT_SIZE + size) / 128.0f)));
        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
        this.message = message; this.size = size;
        font.getData().setScale((Settings.GUI_FONT_SIZE + size) / 128.0f);
        this.tag = tag;
    }

    public void move(float x, float y) {
        relRect.x += x; relRect.y += y;
        this.x += x; this.y += y;
        onResize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
    }

    @Override
    public void render(Batch b) {
        font.getData().setScale((Settings.GUI_FONT_SIZE + size) / 128.0f);
        font.draw(b, message, x, y);
    }

    public void render(Batch b, int xx, int yy, int size) {
        font.getData().setScale((Settings.GUI_FONT_SIZE + size) / 128.0f);
        font.draw(b, message, xx, yy);
    }

    public void renderExact(Batch b, int xx, int yy, int size) {
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
