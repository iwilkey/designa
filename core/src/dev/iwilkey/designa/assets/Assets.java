package dev.iwilkey.designa.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
        addItemButton, subtractItemButton, greenCheck, redX;
    public static TextureRegion[] breakLevel;
    // Tiles
    public static TextureRegion backDirt, air, grass, dirt, stone,
            copperOre, silverOre, ironOre;
    // Resources
    public static TextureRegion[] rock, copper, silver, iron, carbon;
    // Tools
    public static TextureRegion stoneSickle, copperSickle, silverSickle,
    	ironSickle, diamondSickle;
    // Environment
    public static Texture[] clouds;

    // Entities
        // Creature
            // Player
    public static TextureRegion[] player, walk_right, walk_left;

    // Sounds
    public static Sound[] itemPickup, treeHit, treeFall, jumpLand, zoom, dirtHit, stoneHit,
            closeInv, openInv, createItem, blasterFire, explosion;
    public static Sound invClick;

    public static void init() {
        initTextures();
        initSounds();
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
        greenCheck = ss.crop(104 / 8, 56 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);
        redX = ss.crop(112 / 8, 56 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Tiles
        backDirt = ss.crop(4, 5, ss.SLOT_SIZE, ss.SLOT_SIZE);
        air = ss.crop(2, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        grass = ss.crop(0, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        dirt = ss.crop(1, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        stone = ss.crop(11, 3, ss.SLOT_SIZE, ss.SLOT_SIZE);
        copperOre = ss.crop(2, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);
        silverOre = ss.crop(3, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);
        ironOre = ss.crop(4, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Resources
        // Stone
        rock = new TextureRegion[7];
        rock[0] = ss.crop(0, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
        rock[1] = ss.crop(1, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
        rock[2] = ss.crop(2, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
        rock[3] = ss.crop(3, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
        rock[4] = ss.crop(4, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
        rock[5] = ss.crop(5, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
        rock[6] = ss.crop(6, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Copper
        copper = new TextureRegion[5];
        copper[0] = ss.crop(0, 23, ss.SLOT_SIZE, ss.SLOT_SIZE);
        copper[1] = ss.crop(1, 23, ss.SLOT_SIZE, ss.SLOT_SIZE);
        copper[2] = ss.crop(2, 23, ss.SLOT_SIZE, ss.SLOT_SIZE);
        copper[3] = ss.crop(3, 23, ss.SLOT_SIZE, ss.SLOT_SIZE);
        copper[4] = ss.crop(4, 23, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Silver
        silver = new TextureRegion[7];
        silver[0] = ss.crop(0, 24, ss.SLOT_SIZE, ss.SLOT_SIZE);
        silver[1] = ss.crop(1, 24, ss.SLOT_SIZE, ss.SLOT_SIZE);
        silver[2] = ss.crop(2, 24, ss.SLOT_SIZE, ss.SLOT_SIZE);
        silver[3] = ss.crop(3, 24, ss.SLOT_SIZE, ss.SLOT_SIZE);
        silver[4] = ss.crop(4, 24, ss.SLOT_SIZE, ss.SLOT_SIZE);
        silver[5] = ss.crop(5, 24, ss.SLOT_SIZE, ss.SLOT_SIZE);
        silver[6] = ss.crop(6, 24, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Iron
        iron = new TextureRegion[6];
        iron[0] = ss.crop(0, 25, ss.SLOT_SIZE, ss.SLOT_SIZE);
        iron[1] = ss.crop(1, 25, ss.SLOT_SIZE, ss.SLOT_SIZE);
        iron[2] = ss.crop(2, 25, ss.SLOT_SIZE, ss.SLOT_SIZE);
        iron[3] = ss.crop(3, 25, ss.SLOT_SIZE, ss.SLOT_SIZE);
        iron[4] = ss.crop(4, 25, ss.SLOT_SIZE, ss.SLOT_SIZE);
        iron[5] = ss.crop(5, 25, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Carbon
        carbon = new TextureRegion[6];
        carbon[0] = ss.crop(0, 26, ss.SLOT_SIZE, ss.SLOT_SIZE);
        carbon[1] = ss.crop(1, 26, ss.SLOT_SIZE, ss.SLOT_SIZE);
        carbon[2] = ss.crop(2, 26, ss.SLOT_SIZE, ss.SLOT_SIZE);
        carbon[3] = ss.crop(3, 26, ss.SLOT_SIZE, ss.SLOT_SIZE);
        carbon[4] = ss.crop(4, 26, ss.SLOT_SIZE, ss.SLOT_SIZE);
        carbon[5] = ss.crop(5, 26, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Tools
        stoneSickle = ss.crop(104 / 8, 48 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);
        copperSickle = ss.crop(112 / 8, 48 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);
        silverSickle = ss.crop(120 / 8, 48 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);
        ironSickle = ss.crop(128 / 8, 48 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);
        diamondSickle = ss.crop(136 / 8, 48 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);

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

    public static void initSounds() {
        itemPickup = new Sound[3];
        itemPickup[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/item-pickup.ogg"));
        itemPickup[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/item-pickup-2.ogg"));
        itemPickup[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/item-pickup-3.ogg"));
        treeHit = new Sound[3];
        treeHit[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/tree-hit.ogg"));
        treeHit[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/tree-hit-2.ogg"));
        treeHit[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/tree-hit-3.ogg"));
        treeFall = new Sound[3];
        treeFall[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/tree-fall.ogg"));
        treeFall[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/tree-fall-2.ogg"));
        treeFall[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/tree-fall-3.ogg"));
        jumpLand = new Sound[3];
        jumpLand[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/jump-land.ogg"));
        jumpLand[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/jump-land-2.ogg"));
        jumpLand[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/jump-land-3.ogg"));
        zoom = new Sound[3];
        zoom[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/zoom.ogg"));
        zoom[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/zoom-2.ogg"));
        zoom[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/zoom-3.ogg"));
        dirtHit = new Sound[3];
        dirtHit[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/dirt-hit.ogg"));
        dirtHit[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/dirt-hit-2.ogg"));
        dirtHit[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/dirt-hit-3.ogg"));
        stoneHit = new Sound[3];
        stoneHit[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/stone-hit.ogg"));
        stoneHit[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/stone-hit-2.ogg"));
        stoneHit[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/stone-hit-3.ogg"));
        closeInv = new Sound[3];
        closeInv[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/close-inv.ogg"));
        closeInv[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/close-inv-2.ogg"));
        closeInv[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/close-inv-3.ogg"));
        openInv = new Sound[3];
        openInv[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/open-inv.ogg"));
        openInv[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/open-inv-2.ogg"));
        openInv[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/open-inv-3.ogg"));
        invClick = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/inv-click.ogg"));
        createItem = new Sound[3];
        createItem[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/create-item.ogg"));
        createItem[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/create-item-2.ogg"));
        createItem[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/create-item-3.ogg"));
        blasterFire = new Sound[3];
        blasterFire[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/fire-1.ogg"));
        blasterFire[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/fire-2.ogg"));
        blasterFire[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/fire-3.ogg"));
        explosion = new Sound[3];
        explosion[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/explosion-1.ogg"));
        explosion[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/explosion-2.ogg"));
        explosion[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ambient/explosion-3.ogg"));
    }
}
