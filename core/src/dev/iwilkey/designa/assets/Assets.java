package dev.iwilkey.designa.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.iwilkey.designa.gfx.SpriteSheet;

public class Assets {

    // Version
    public static String VERSION = "alpha 2.0.0";
    // Cursor
    public static TextureRegion cursor;
    // GUI
    public static TextureRegion inventorySlot, inventorySelector,
        addItemButton, subtractItemButton;
    // Tiles
    public static TextureRegion air, grass, dirt;

    // Entities
        // Creature
            // Player
    public static TextureRegion[] player, walk_right, walk_left;

    public static void init() {
        initTextures();
    }

    public static void initTextures() {

        SpriteSheet ss = new SpriteSheet(new Texture("textures/spritesheet.png"));

        // Cursor
        cursor = ss.crop(0, 5, ss.SLOT_SIZE * 4, ss.SLOT_SIZE * 4);

        // GUI
        inventorySlot = ss.crop(3, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        inventorySelector = ss.crop(0, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        addItemButton = ss.crop(144 / 8, 104 / 8, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        subtractItemButton = ss.crop(168 / 8, 104 / 8, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);

        // Tiles
        air = ss.crop(2, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        grass = ss.crop(0, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        dirt = ss.crop(1, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Entities
            // Creatures
                // Player
        player = new TextureRegion[2];
        player[0] = ss.crop(3, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        player[1] = ss.crop(5, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        walk_right = new TextureRegion[2];
        walk_right[0] = ss.crop(7, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        walk_right[1] = ss.crop(9, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        walk_left = new TextureRegion[2];
        walk_left[0] = ss.crop(7, 3, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        walk_left[1] = ss.crop(9, 3, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);

    }
}
