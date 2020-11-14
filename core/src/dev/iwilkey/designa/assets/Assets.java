package dev.iwilkey.designa.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.iwilkey.designa.gfx.SpriteSheet;

public class Assets {

    // Version
    public static String VERSION = "alpha 2.0.0";
    // Cursor
    public static TextureRegion cursor;
    // Colors
    public static TextureRegion[] lightColors;
    // GUI
    public static TextureRegion inventorySlot, inventorySelector,
        addItemButton, subtractItemButton;
    public static TextureRegion[] breakLevel;
    // Tiles
    public static TextureRegion backDirt, air, grass, dirt, stone;
    // Environment
    public static Texture[] clouds;

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

        // Colors
        lightColors = new TextureRegion[7];
        for(int i = 0; i < 7; i++)
            lightColors[i] = ss.crop(i, 12, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // GUI
        inventorySlot = ss.crop(3, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        inventorySelector = ss.crop(0, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        addItemButton = ss.crop(144 / 8, 104 / 8, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        subtractItemButton = ss.crop(168 / 8, 104 / 8, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        breakLevel = new TextureRegion[5];
            breakLevel[0] = ss.crop(5, 4, ss.SLOT_SIZE, ss.SLOT_SIZE); // Clean block
            breakLevel[1] = ss.crop(1, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
            breakLevel[2] = ss.crop(2, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
            breakLevel[3] = ss.crop(3, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
            breakLevel[4] = ss.crop(4, 4, ss.SLOT_SIZE, ss.SLOT_SIZE); // Most broken

        // Tiles
        backDirt = ss.crop(4, 5, ss.SLOT_SIZE, ss.SLOT_SIZE);
        air = ss.crop(2, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        grass = ss.crop(0, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        dirt = ss.crop(1, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        stone = ss.crop(11, 3, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Environment
        clouds = new Texture[4];
            clouds[0] = new Texture("textures/environment/clouds/cloud-1.png");
            clouds[1] = new Texture("textures/environment/clouds/cloud-2.png");
            clouds[2] = new Texture("textures/environment/clouds/cloud-3.png");
            clouds[3] = new Texture("textures/environment/clouds/cloud-4.png");

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
