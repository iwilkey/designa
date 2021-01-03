package dev.iwilkey.designa.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.iwilkey.designa.Game;

public class UIImageButton extends UIButton {

    public TextureRegion image;
    public int scale;

    public UIImageButton(int x, int y, TextureRegion image, int scale, ClickListener clickListener) {
        super("", x, y, image.getRegionWidth() * scale, image.getRegionHeight() * scale, clickListener);
        this.image = image;
        this.scale = scale;
    }

    public void move(int x, int y) {
        relRect.x = x; relRect.y = y;
        this.x = x; this.y = y;
        onResize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
    }

    @Override
    public void render(Batch b) {
        b.draw(image, x, y, width, height);
    }

}
