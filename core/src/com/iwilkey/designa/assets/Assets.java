package com.iwilkey.designa.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.gfx.SpriteSheet;

public class Assets {

    // Cursor TODO: Check if this causes problems on a smartphone...
    public static TextureRegion cursor;

    // Font
    public static TextureRegion[] font;

    // Colors
    public static TextureRegion[] sky_colors, light_colors;

    // GUI
    public static TextureRegion[] heart, breakLevel, craftingTabs;
    public static TextureRegion selector, errorSelector, transSelector, jumpSelector, inventorySelector, inventorySlot,
            itemRep;

    // Tiles
    public static TextureRegion air, grass, dirt, backDirt,
        oakWood, leaf, stone;
    public static TextureRegion[] torch;

    // Environment
    public static TextureRegion[] trees;

    // Tools
    public static TextureRegion simpleDrill;

    // Player
    public static TextureRegion[] player, player_jump;
        // Animations
        public static TextureRegion[] walk_right, walk_left;

    public static void init() {

        SpriteSheet ss = new SpriteSheet(new Texture("textures/spritesheet.png"));

        // Cursor
        cursor = ss.crop(0, 5, ss.SLOT_SIZE * 4, ss.SLOT_SIZE * 4);

        // Init font
        // Numbers and symbols start on x = 11 and y = 1
        font = new TextureRegion[27 + 16];
        // Init letters (26 plus space)
        for (int i = 0; i < 27; i++) {
            font[i] = ss.crop(i, 0, ss.SLOT_SIZE, ss.SLOT_SIZE);
        }

        // Init numbers and symbols (13)
        for(int i = 0; i < 16; i++) {
            font[i + 27] = ss.crop(i + 11, 1, ss.SLOT_SIZE, ss.SLOT_SIZE);
        }

        // Init colors
        sky_colors = new TextureRegion[10];
        for(int i = 0; i < 10; i++) {
            sky_colors[i] = ss.crop(i, 11, ss.SLOT_SIZE, ss.SLOT_SIZE);
        }
        light_colors = new TextureRegion[7];
        for(int i = 0; i < 7; i++) {
            light_colors[i] = ss.crop(i, 12, ss.SLOT_SIZE, ss.SLOT_SIZE);
        }

        // Init GUI
        heart = new TextureRegion[2];
        heart[0] = ss.crop(0, 3, ss.SLOT_SIZE, ss.SLOT_SIZE); // Full heart
        heart[1] = ss.crop(0, 4, ss.SLOT_SIZE, ss.SLOT_SIZE); // Empty heart
        selector = ss.crop(0, 1, ss.SLOT_SIZE, ss.SLOT_SIZE);
        errorSelector = ss.crop(1, 1, ss.SLOT_SIZE, ss.SLOT_SIZE);
        transSelector = ss.crop(2, 1, ss.SLOT_SIZE, ss.SLOT_SIZE);
        jumpSelector = ss.crop(1, 3, ss.SLOT_SIZE, ss.SLOT_SIZE);
        breakLevel = new TextureRegion[5];
        breakLevel[0] = ss.crop(5, 4, ss.SLOT_SIZE, ss.SLOT_SIZE); // Clean block
        breakLevel[1] = ss.crop(1, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
        breakLevel[2] = ss.crop(2, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
        breakLevel[3] = ss.crop(3, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
        breakLevel[4] = ss.crop(4, 4, ss.SLOT_SIZE, ss.SLOT_SIZE); // Most broken
        inventorySelector = ss.crop(0, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        inventorySlot = ss.crop(3, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        craftingTabs = new TextureRegion[3];
        craftingTabs[0] = ss.crop(6, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        craftingTabs[1] = ss.crop(9, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        craftingTabs[2] = ss.crop(12, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);
        itemRep = ss.crop(15, 13, ss.SLOT_SIZE * 3, ss.SLOT_SIZE * 3);

        // Tiles
        air = ss.crop(2, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        grass = ss.crop(0, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        dirt = ss.crop(1, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        backDirt = ss.crop(4, 5, ss.SLOT_SIZE, ss.SLOT_SIZE);
        oakWood = ss.crop(11, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        leaf = ss.crop(12, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        stone = ss.crop(11, 3, ss.SLOT_SIZE, ss.SLOT_SIZE);
        torch = new TextureRegion[4];
        torch[0] = ss.crop(11, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
        torch[1] = ss.crop(12, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
        torch[2] = ss.crop(13, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
        torch[3] = ss.crop(14, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Environment
        trees = new TextureRegion[3];
        trees[0] = ss.crop(10, 7, ss.SLOT_SIZE, ss.SLOT_SIZE * 4);
        trees[1] = ss.crop(11, 7, ss.SLOT_SIZE, ss.SLOT_SIZE * 4);
        trees[2] = ss.crop(12, 7, ss.SLOT_SIZE, ss.SLOT_SIZE * 4);

        // Tools
        simpleDrill = ss.crop(4, 6, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);

        // Init player
        player = new TextureRegion[2];
        player[0] = ss.crop(3, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        player[1] = ss.crop(5, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        player_jump = new TextureRegion[2];
        player_jump[0] = ss.crop(7, 5, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2); // Jump left
        player_jump[1] = ss.crop(9, 5, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2); // Jump right
            // Animations
            walk_right = new TextureRegion[2];
            walk_right[0] = ss.crop(7, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            walk_right[1] = ss.crop(9, 1, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            walk_left = new TextureRegion[2];
            walk_left[0] = ss.crop(7, 3, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            walk_left[1] = ss.crop(9, 3, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);

    }


}
