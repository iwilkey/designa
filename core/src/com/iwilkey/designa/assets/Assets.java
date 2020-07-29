package com.iwilkey.designa.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.iwilkey.designa.gfx.SpriteSheet;

public class Assets {

    // Font
    public static TextureRegion[] font;

    // Tiles
    public static TextureRegion air, grass, dirt;

    // Player
    public static TextureRegion player;

    public static void init() {

        SpriteSheet ss = new SpriteSheet(new Texture("textures/spritesheet.png"));

        // Init font
        font = new TextureRegion[27];
        for (int i = 0; i < 27; i++) {
            font[i] = ss.crop(i, 0, ss.SLOT_SIZE, ss.SLOT_SIZE);
        }

        // Tiles
        air = ss.crop(2, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        grass = ss.crop(0, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        dirt = ss.crop(1, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Init player
        player = ss.crop(3, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);

    }


}
