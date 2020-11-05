package dev.iwilkey.designa.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class UIImageButton extends UIButton {

    public TextureRegion image;
    public int scale;

    public UIImageButton(int x, int y, TextureRegion image, int scale, ClickListener clickListener) {
        super("", x, y, image.getRegionWidth() * scale, image.getRegionHeight() * scale, clickListener);
        this.image = image;
        this.scale = scale;
    }

    @Override
    public void render(Batch b) {
        b.draw(image, x, y, width, height);
    }

}
