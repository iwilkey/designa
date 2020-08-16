package com.iwilkey.designa.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    public static TextureRegion[] heart, breakLevel, craftingTabs, backBuilding;
    public static TextureRegion selector, errorSelector, transSelector, jumpSelector, inventorySelector, inventorySlot,
            itemRep, blueprintGUI;

    // Tiles
    public static TextureRegion air, grass, dirt, backDirt,
        oakWood, leaf, stone, torchThumb;
    public static TextureRegion[] torch;

    // Environment
    public static TextureRegion[] trees;

    // Tools
    public static TextureRegion simpleDrill;

    // Player
    public static TextureRegion[] player, player_jump, playerGun;
        // Animations
        public static TextureRegion[] walk_right, walk_left, playerGunWalkRight, playerGunWalkLeft;

    // NPCs
    public static TextureRegion[] man, manJump, manWalkRight, manWalkLeft;

    // Sounds
    public static Sound[] itemPickup, treeHit, treeFall, jumpLand, zoom, dirtHit, stoneHit,
        closeInv, openInv, createItem;
    public static Sound invClick;

    // Names
    public static String[] maleNames;

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
        backBuilding = new TextureRegion[2];
        backBuilding[0] = ss.crop(11, 6, ss.SLOT_SIZE, ss.SLOT_SIZE);
        backBuilding[1] = ss.crop(12, 6, ss.SLOT_SIZE, ss.SLOT_SIZE);
        blueprintGUI = ss.crop(0, 18, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 3);

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
        torchThumb = ss.crop(11, 5, ss.SLOT_SIZE, ss.SLOT_SIZE);

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
        playerGun = new TextureRegion[2];
        playerGun[0] = ss.crop(13, 2, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        playerGun[1] = ss.crop(15, 2, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            playerGunWalkRight = new TextureRegion[2];
            playerGunWalkRight[0] = ss.crop(17, 2, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            playerGunWalkRight[1] = ss.crop(19, 2, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            playerGunWalkLeft = new TextureRegion[2];
            playerGunWalkLeft[0] = ss.crop(17, 4, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            playerGunWalkLeft[1] = ss.crop(19, 4, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);

        // Init NPCs
        man = new TextureRegion[2];
        man[0] = ss.crop(0, 16, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        man[1] = ss.crop(2, 16, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        manJump = new TextureRegion[2];
        manJump[1] = ss.crop(4, 18, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        manJump[0] = ss.crop(6, 18, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            // Animations
            manWalkRight = new TextureRegion[2];
            manWalkRight[0] = ss.crop(4, 16, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            manWalkRight[1] = ss.crop(6, 16, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            manWalkLeft = new TextureRegion[2];
            manWalkLeft[0] = ss.crop(4, 18, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
            manWalkLeft[1] = ss.crop(6, 18, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);

        // Sounds
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

        maleNames = new String[] { "Anderson", "Ashwoon", "Aikin", "Bateman", "Bongard", "Bowers", "Boyd",
                "Cannon", "Cast", "Deitz", "Dewalt", "Ebner", "Frick", "Hancock", "Haworth", "Hesch",
                "Hoffman", "Kassing", "Knutson", "Lawless", "Lawicki", "Mccord", "McCormack", "Miller",
                "Myers", "Nugent", "Ortiz", "Orwig", "Ory", "Paiser", "Pak", "Pettigrew", "Quinn",
                "Quizoz", "Ramachandran", "Resnick", "Sagar", "Schickowski", "Schiebel", "Sellon",
                "Severson", "Shaffer", "Solberg", "Soloman", "Sonderling", "Soukup", "Soulis",
                "Stahl", "Sweeney", "Tandy", "Trebil", "Trusela", "Trussel", "Turco", "Uddin",
                "Uflan", "Ulrich", "Upson", "Vader", "Vail", "Valente", "Van Zandt", "Vanderpoel",
                "Ventotla", "Vogal", "Wagle", "Wagner", "Wakefield", "Weinstein", "Weiss", "Woo",
                "Yang", "Yates", "Yocum", "Zeaser", "Zeller", "Ziegler", "Bauer", "Baxster",
                "Casal", "Cataldi", "Caswell", "Celedon", "Chambers", "Chapman", "Christensen",
                "Darnell", "Davidson", "Davis", "DeLorenzo", "Dinkins", "Doran", "Dugelman", "Dugan",
                "Duffman", "Eastman", "Ferro", "Ferry", "Fletcher", "Fietzer", "Hylan", "Hydinger",
                "Illingsworth", "Ingram", "Irwin", "Jagtap", "Jenson", "Johnson", "Johnsen", "Jones",
                "Jurgenson", "Kalleg", "Kaskel", "Keller", "Leisinger", "LePage", "Lewis", "Linde",
                "Lulloff", "Maki", "Martin", "McGinnis", "Mills", "Moody", "Moore", "Napier", "Nelson",
                "Norquist", "Nuttle", "Olson", "Ostrander", "Reamer", "Reardon", "Reyes", "Rice",
                "Ripka", "Roberts", "Rogers", "Root", "Sandstrom", "Sawyer", "Schlicht", "Schmitt",
                "Schwager", "Schutz", "Schuster", "Tapia", "Thompson", "Tiernan", "Tisler" };

    }


}
