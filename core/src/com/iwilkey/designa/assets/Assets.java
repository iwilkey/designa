package com.iwilkey.designa.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.gfx.SpriteSheet;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemRecipe;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.particle.Particle;
import com.iwilkey.designa.tiles.Tile;

import java.util.ArrayList;

/**

 The Assets class encapsulates static instances of assets used during the runtime of Designa. This makes it easy to
 use them anywhere as they're needed.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

public class Assets {

    /**
     * Global vars (All static for easy access)
     */

    // Version
    public static String VERSION = "pa1.0.47";
    // Cursor
    public static TextureRegion cursor;
    // Font
    public static TextureRegion[] font;
    // Colors
    public static TextureRegion[] sky_colors, light_colors;
    // Geometry
    public static TextureRegion line, square;
    // GUI
    public static TextureRegion[] heart, breakLevel, craftingTabs, backBuilding, arrow;
    public static TextureRegion selector, errorSelector, transSelector, jumpSelector, inventorySelector, inventorySlot,
            itemRep, blueprintGUI, nodeConnector;
    // Tiles
    public static TextureRegion air, grass, dirt, backDirt,
        stone, torchThumb, copperOre, silverOre, ironOre,
        plywood, hardwood, reinforcedHardwood, strongwood, reinforcedStrongwood, crate;
    public static TextureRegion[] torch;
    // Resources
    public static TextureRegion[] wood, rock, copper, silver, iron, carbon;
    // Environment
    public static Texture[] clouds;
    public static Texture mountains;
    public static TextureRegion[] trees;
    // Tools
    public static TextureRegion simpleDrill, wrench, ladder;
    // Machines
        // Mech Drills
        public static TextureRegion[] copperMechanicalDrill;
        // Node
        public static TextureRegion[] node;
        // Pipes
        public static TextureRegion[] stonePipeRight, stonePipeDown, stonePipeLeft, stonePipeUp;
        // Assembler
        public static TextureRegion assembler;
    // Weapons
        public static TextureRegion blasterBase;
    // Player
    public static TextureRegion[] player, player_jump, playerGun;
        // Animations
        public static TextureRegion[] walk_right, walk_left, playerGunWalkRight, playerGunWalkLeft;
    // NPCs
    public static TextureRegion[] man, manJump, manWalkRight, manWalkLeft;
    // Enemies
    public static TextureRegion[] groundBotRight, groundBotLeft;
    // Sounds
    public static Sound[] itemPickup, treeHit, treeFall, jumpLand, zoom, dirtHit, stoneHit,
        closeInv, openInv, createItem;
    public static Sound invClick;
    // Names
    public static String[] maleNames;

    // Items

        // Wood
        public static Item barkResource, stickResource, plywoodResource, hardwoodResource, reinforcedHardwoodResource,
            strongwoodResource, reinforcedStrongwoodResource;
        // Stone
        public static Item rockResource, gravelResource, concreteResource, reinforcedConcreteResource, condensedSlabResource,
            strongstoneResource, reinforcedStrongstoneResource;
        // Copper
        public static Item copperScrapResource, recycledCopperResource, bluestoneResource, reinforcedBluestoneResource,
                romanVitriolResource;
        // Silver
        public static Item silverScrapResource, recycledSilverResource, coinSilverResource, sterlingSilverResource,
                reinforcedSterlingSilverResource, fineSilverResource, reinforcedFineSilverResource;
        // Iron
        public static Item ironScrapResource, recycledIronResource, castIronResource, reinforcedCastIronResource,
                steelResource, reinforcedSteelResource;
        // Carbon
        public static Item carbonSampleResource, graphiteResource, compressedGraphite, weakDiamondResource,
                diamondResource, reinforcedDiamondResource;

    // PlaceableBlocks
    public static Item dirtItem, stoneItem,
        plywoodTileItem, hardwoodTileItem, reinforcedHardwoodTileItem,
        strongwoodTileItem, reinforcedStrongwoodTileItem;
    // CreatableTiles
    public static Item torchItem, crateItem, ladderItem;

    //CreatableItems
        // Tools
            // Drills
            public static Item simpleDrillItem, wrenchItem;

        // Machines

            // Mechanical Drills
            public static Item copperMechanicalDrillItem;

            // Node
            public static Item nodeItem;

            // Pipes
            public static Item stonePipeItem;

            // Assembler
            public static Item assemblerItem;

        // Weapons
            public static Item blasterBaseItem;

    // Item Recipe
        // Wood
        public static ItemRecipe STICK_RESOURCE_RECIPE, PLYWOOD_RESOURCE_RECIPE, HARDWOOD_RESOURCE_RECIPE, REINFORCED_HARDWOOD_RECIPE,
            STRONGWOOD_RECIPE, REINFORCED_STRONGWOOD_RECIPE;

        // Stone
        public static ItemRecipe GRAVEL_RESOURCE_RECIPE, CONCRETE_RESOURCE_RECIPE, REINFORCED_CONCRETE_RESOURCE_RECIPE,
                CONDENSED_SLAB_RESOURCE_RECIPE, STRONGSTONE_RESOURCE_RECIPE, REINFORCED_STRONGSTONE_RESOURCE_RECIPE;

        // Copper
        public static ItemRecipe RECYCLED_COPPER_RECIPE, BLUESTONE_RECIPE, REINFORCED_BLUESTONE_RECIPE, ROMAN_VITRIOL_RECIPE;

        // Silver
        public static ItemRecipe RECYCLED_SILVER_RECIPE, COIN_SILVER_RECIPE, STERLING_SILVER_RECIPE, REINFORCED_STERLING_SILVER_RECIPE,
            FINE_SILVER_RECIPE, REINFORCED_FINE_SILVER_RECIPE;

        // Iron
        public static ItemRecipe RECYCLED_IRON_RECIPE, CAST_IRON_RECIPE, REINFORCED_CAST_IRON_RECIPE, STEEL_RECIPE,
                REINFORCED_STEEL_RECIPE;

        // Carbon
        public static ItemRecipe GRAPHITE_RECIPE, COMPRESSED_GRAPHITE_RECIPE, WEAK_DIAMOND_RECIPE, DIAMOND_RECIPE,
                REINFORCED_DIAMOND_RECIPE;

        // CreatableItems
            // Tools
                // Drills
                public static ItemRecipe SIMPLE_DRILL_RECIPE, WRENCH_RECIPE;

            public static ItemRecipe TORCH_RECIPE, CRATE_RECIPE, LADDER_RECIPE;

            // Construction
            public static ItemRecipe PLYWOOD_TILE_RECIPE, HARDWOOD_TILE_RECIPE, REINFORCED_HARDWOOD_TILE_RECIPE,
                STRONGWOOD_TILE_RECIPE, REINFORCED_STRONGWOOD_TILE_RECIPE;

            // Machines
                // Mech Drills
                public static ItemRecipe COPPER_MECHANICAL_DRILL_RECIPE;

                // Offloader
                public static ItemRecipe NODE_RECIPE;

                // Pipes
                public static ItemRecipe STONE_PIPE_RECIPE;

                // Assembler
                public static ItemRecipe ASSEMBLER_RECIPE;

            // Weapons
                public static ItemRecipe BLASTER_BASE_RECIPE;

        // Tiles
        public static Tile airTile, grassTile, dirtTile, stoneTile, treeTile;

        // Non-construction
        // Animated
        public static Tile torchTile, crateTile, ladderTile;

        // Ores
        public static Tile copperOreTile, silverOreTile, ironOreTile;

        // Construction
        public static Tile plywoodTile, hardwoodTile,
                reinforcedHardwoodTile, strongwoodTile, reinforcedStrongwoodTile;

        // Machines
        // Mech Drills
        public static Tile copperMechanicalDrillTile;

        // Offloader
        public static Tile nodeTile;

        // Pipes
        public static Tile stonePipeTile;

        // Assembler
        public static Tile assemblerTile;

        // Weapons
        public static Tile blasterBaseTile;

        // Item Representation Lists
        public static ArrayList<Item> resourceRepList, toolRepList, utilitiesRepList,
                tilesRepList, defenseRepList, machinesRepList, miscRepList;

        // Particles
        public static ArrayList<Particle> particles;

    /**
     *  This method is called by the Game class upon creation of the program.
     */
    public static void init() {
        initTextures(); // Init all textures
        initSounds(); // Init all sounds
        initObjects(); // Init all objects
    }

    /**
     * This method initiates all textures.
     */
    private static void initTextures() {

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

        // Init geom
        line = ss.crop(1, 216 / 8, 1, ss.SLOT_SIZE);
        square = ss.crop(16 / 8, 216 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);

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
        nodeConnector = ss.crop(1, 9, ss.SLOT_SIZE, ss.SLOT_SIZE);
        arrow = new TextureRegion[4];
        arrow[0] = ss.crop(8, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);
        arrow[1] = ss.crop(9, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);
        arrow[2] = ss.crop(10, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);
        arrow[3] = ss.crop(11, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Tiles
        // Natural Tiles
        air = ss.crop(2, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        grass = ss.crop(0, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        dirt = ss.crop(1, 2, ss.SLOT_SIZE, ss.SLOT_SIZE);
        backDirt = ss.crop(4, 5, ss.SLOT_SIZE, ss.SLOT_SIZE);
        stone = ss.crop(11, 3, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // CreatableTiles
            // Animated tiles
            torch = new TextureRegion[4];
                torch[0] = ss.crop(11, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
                torch[1] = ss.crop(12, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
                torch[2] = ss.crop(13, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
                torch[3] = ss.crop(14, 4, ss.SLOT_SIZE, ss.SLOT_SIZE);
                torchThumb = ss.crop(11, 5, ss.SLOT_SIZE, ss.SLOT_SIZE);
        crate = ss.crop(2, 18, ss.SLOT_SIZE * 2, ss.SLOT_SIZE);
        ladder = ss.crop(5, 28, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Ores
        copperOre = ss.crop(2, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);
        silverOre = ss.crop(3, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);
        ironOre = ss.crop(4, 20, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Construction Tiles
        plywood = ss.crop(0, 28, ss.SLOT_SIZE, ss.SLOT_SIZE);
        hardwood = ss.crop(1, 28, ss.SLOT_SIZE, ss.SLOT_SIZE);
        reinforcedHardwood = ss.crop(2, 28, ss.SLOT_SIZE, ss.SLOT_SIZE);
        strongwood = ss.crop(3, 28, ss.SLOT_SIZE, ss.SLOT_SIZE);
        reinforcedStrongwood = ss.crop(4, 28, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Resources
        // Wood
        wood = new TextureRegion[7];
        wood[0] = ss.crop(0, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
        wood[1] = ss.crop(1, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
        wood[2] = ss.crop(2, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
        wood[3] = ss.crop(3, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
        wood[4] = ss.crop(4, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
        wood[5] = ss.crop(5, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
        wood[6] = ss.crop(6, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);

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

        // Environment
        trees = new TextureRegion[3];
        trees[0] = ss.crop(10, 7, ss.SLOT_SIZE, ss.SLOT_SIZE * 4);
        trees[1] = ss.crop(11, 7, ss.SLOT_SIZE, ss.SLOT_SIZE * 4);
        trees[2] = ss.crop(12, 7, ss.SLOT_SIZE, ss.SLOT_SIZE * 4);
        clouds = new Texture[4];
        clouds[0] = new Texture("textures/environment/clouds/cloud-1.png");
        clouds[1] = new Texture("textures/environment/clouds/cloud-2.png");
        clouds[2] = new Texture("textures/environment/clouds/cloud-3.png");
        clouds[3] = new Texture("textures/environment/clouds/cloud-4.png");
        mountains = new Texture("textures/environment/mountain-range.png");

        // Tools
        simpleDrill = ss.crop(4, 6, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        wrench = ss.crop(10, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Machines
            // Mech Drills
            copperMechanicalDrill = new TextureRegion[4];
                copperMechanicalDrill[0] = ss.crop(8, 18, ss.SLOT_SIZE, ss.SLOT_SIZE);
                copperMechanicalDrill[1] = ss.crop(9, 18, ss.SLOT_SIZE, ss.SLOT_SIZE);
                copperMechanicalDrill[2] = ss.crop(10, 18, ss.SLOT_SIZE, ss.SLOT_SIZE);
                copperMechanicalDrill[3] = ss.crop(11, 18, ss.SLOT_SIZE, ss.SLOT_SIZE);

            // Offloader
            node = new TextureRegion[4];
                node[0] = ss.crop(12, 18, ss.SLOT_SIZE, ss.SLOT_SIZE);
                node[1] = ss.crop(13, 18, ss.SLOT_SIZE, ss.SLOT_SIZE);
                node[2] = ss.crop(14, 18, ss.SLOT_SIZE, ss.SLOT_SIZE);
                node[3] = ss.crop(15, 18, ss.SLOT_SIZE, ss.SLOT_SIZE);

            //Pipes
            stonePipeRight = new TextureRegion[3];
                stonePipeRight[0] = ss.crop(9, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
                stonePipeRight[1] = ss.crop(9, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
                stonePipeRight[2] = ss.crop(9, 23, ss.SLOT_SIZE, ss.SLOT_SIZE);
            stonePipeLeft = new TextureRegion[3];
                stonePipeLeft[2] = ss.crop(9, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
                stonePipeLeft[1] = ss.crop(9, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
                stonePipeLeft[0] = ss.crop(9, 23, ss.SLOT_SIZE, ss.SLOT_SIZE);
            stonePipeUp = new TextureRegion[3];
                stonePipeUp[2] = ss.crop(8, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
                stonePipeUp[1] = ss.crop(8, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
                stonePipeUp[0] = ss.crop(8, 23, ss.SLOT_SIZE, ss.SLOT_SIZE);
            stonePipeDown = new TextureRegion[3];
                stonePipeDown[0] = ss.crop(8, 21, ss.SLOT_SIZE, ss.SLOT_SIZE);
                stonePipeDown[1] = ss.crop(8, 22, ss.SLOT_SIZE, ss.SLOT_SIZE);
                stonePipeDown[2] = ss.crop(8, 23, ss.SLOT_SIZE, ss.SLOT_SIZE);

            // Assembler
            assembler = ss.crop(108 / 8,144 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);

        // Weapons
            blasterBase = ss.crop(32 / 8, 80 / 8, ss.SLOT_SIZE, ss.SLOT_SIZE);

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

        // Init Enemies
        groundBotRight = new TextureRegion[2];
        groundBotRight[0] = ss.crop(8, 16, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        groundBotRight[1] = ss.crop(10, 16, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        groundBotLeft = new TextureRegion[2];
        groundBotLeft[0] = ss.crop(12, 16, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);
        groundBotLeft[1] = ss.crop(14, 16, ss.SLOT_SIZE * 2, ss.SLOT_SIZE * 2);

    }

    /**
     *  This method initiates all sounds.
     */
    private static void initSounds() {
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
    }

    /**
     *  This method initiates all object instances.
     */
    private static void initObjects() {
        // Tiles
        airTile = new Tile.AirTile(0, 0);
        grassTile = new Tile.GrassTile(1, 4);
        dirtTile = new Tile.DirtTile(2, 3);
        stoneTile = new Tile.StoneTile(3, 16);
        treeTile = new Tile.TreeTile(255);

        // Non-construction
        // Animated
        torchTile = new Tile.TorchTile(4, 1);
        crateTile = new Tile.CrateTile(13, 20);
        ladderTile = new Tile.LadderTile(17, 8);

        // Ores
        copperOreTile = new Tile.CopperOreTile(5, 20);
        silverOreTile = new Tile.SilverOreTile(6, 20);
        ironOreTile = new Tile.IronOreTile(7, 20);

        // Construction
        plywoodTile = new Tile.PlywoodTile(8, 5);
        hardwoodTile = new Tile.HardwoodTile(9, 10);
        reinforcedHardwoodTile = new Tile.ReinforcedHardwoodTile(10, 15);
        strongwoodTile = new Tile.StrongwoodTile(11, 20);
        reinforcedStrongwoodTile = new Tile.ReinforcedStrongwoodTile(12, 25);

        // Machines
        // Mech Drills
        copperMechanicalDrillTile = new Tile.CopperMechanicalDrill(14, 40);

        // Offloader
        nodeTile = new Tile.Node(16, 30);

        // Pipes
        stonePipeTile = new Tile.Pipe.StonePipe(15, 12);

        // Assembler
        assemblerTile = new Tile.Assembler(18, 45);

        // Weapons
        blasterBaseTile = new Tile.BlasterBase(19, 35);

        // Items

        barkResource = new Item(Assets.wood[0], "Bark",100,
                new ItemType.Resource("Wood", "Bark"));
        stickResource = new Item(Assets.wood[1], "Sticks", 101,
                new ItemType.Resource("Wood", "Sticks"));
        plywoodResource = new Item(Assets.wood[2], "Plywood", 102,
                new ItemType.Resource("Wood", "Plywood"));
        hardwoodResource = new Item(Assets.wood[3], "Hardwood", 103,
                new ItemType.Resource("Wood", "Hardwood"));
        reinforcedHardwoodResource = new Item(Assets.wood[4], "Reinforced Hardwood", 104,
                new ItemType.Resource("Wood", "Reinforced Hardwood"));
        strongwoodResource = new Item(Assets.wood[5], "Strongwood", 105,
                new ItemType.Resource("Wood", "Strongwood"));
        reinforcedStrongwoodResource = new Item(Assets.wood[6], "Reinforced Strongwood", 106,
                new ItemType.Resource("Wood", "Reinforced Strongwood"));

        // Stone
        rockResource = new Item(Assets.rock[0], "Rocks", 107,
                new ItemType.Resource("Stone", "Rocks"));
        gravelResource = new Item(Assets.rock[1], "Gravel", 108,
                new ItemType.Resource("Stone", "Gravel"));
        concreteResource = new Item(Assets.rock[2], "Concrete", 109,
                new ItemType.Resource("Stone", "Concrete"));
        reinforcedConcreteResource = new Item(Assets.rock[3], "Reinforced Concrete", 110,
                new ItemType.Resource("Stone", "Reinforced Concrete"));
        condensedSlabResource = new Item(Assets.rock[4], "Condensed Slab", 111,
                new ItemType.Resource("Stone", "Condensed Slab"));
        strongstoneResource = new Item(Assets.rock[5], "Strongstone", 112,
                new ItemType.Resource("Stone", "Strongstone"));
        reinforcedStrongstoneResource = new Item(Assets.rock[6], "Reinforced Strongstone", 113,
                new ItemType.Resource("Stone", "Reinforced Strongstone"));

        // Copper
        copperScrapResource = new Item(Assets.copper[0], "Copper Scrap", 114,
                new ItemType.Resource("Copper", "Copper Scrap"));
        recycledCopperResource = new Item(Assets.copper[1], "Recycled Copper", 115,
                new ItemType.Resource("Copper", "Recycled Copper"));
        bluestoneResource = new Item(Assets.copper[2], "Bluestone", 116,
                new ItemType.Resource("Copper", "Bluestone"));
        reinforcedBluestoneResource = new Item(Assets.copper[3], "Reinforced Bluestone", 117,
                new ItemType.Resource("Copper", "Reinforced Bluestone"));
        romanVitriolResource = new Item(Assets.copper[4], "Roman Vitriol", 118,
                new ItemType.Resource("Copper", "Roman Vitriol"));

        // Silver
        silverScrapResource = new Item(Assets.silver[0], "Silver Scrap", 119,
                new ItemType.Resource("Silver", "Silver Scrap"));
        recycledSilverResource = new Item(Assets.silver[1], "Recycled Silver", 120,
                new ItemType.Resource("Silver", "Recycled Silver"));
        coinSilverResource = new Item(Assets.silver[2], "Coin Silver", 121,
                new ItemType.Resource("Silver", "Coin Silver"));
        sterlingSilverResource = new Item(Assets.silver[3], "Sterling Silver", 122,
                new ItemType.Resource("Silver", "Sterling Silver"));
        reinforcedSterlingSilverResource = new Item(Assets.silver[4], "Reinforced Sterling Silver", 123,
                new ItemType.Resource("Silver", "Reinforced Sterling Silver"));
        fineSilverResource = new Item(Assets.silver[5], "Fine Silver", 124,
                new ItemType.Resource("Silver", "Fine Silver"));
        reinforcedFineSilverResource = new Item(Assets.silver[6], "Reinforced Fine Silver", 125,
                new ItemType.Resource("Silver", "Reinforced Fine Silver"));

        // Iron
        ironScrapResource = new Item(Assets.iron[0], "Iron Scrap", 126,
                new ItemType.Resource("Iron", "Iron Scrap"));
        recycledIronResource = new Item(Assets.iron[1], "Recycled Iron", 127,
                new ItemType.Resource("Iron", "Recycled Iron"));
        castIronResource = new Item(Assets.iron[2], "Cast Iron", 128,
                new ItemType.Resource("Iron", "Cast Iron"));
        reinforcedCastIronResource = new Item(Assets.iron[3], "Reinforced Cast Iron", 129,
                new ItemType.Resource("Iron", "Reinforced Cast Iron"));
        steelResource = new Item(Assets.iron[4], "Steel", 130,
                new ItemType.Resource("Iron", "Steel"));
        reinforcedSteelResource = new Item(Assets.iron[5], "Reinforced Steel", 131,
                new ItemType.Resource("Iron", "Reinforced Steel"));

        // Carbon
        carbonSampleResource = new Item(Assets.carbon[0], "Carbon Sample", 132,
                new ItemType.Resource("Carbon", "Carbon Sample"));
        graphiteResource = new Item(Assets.carbon[1], "Graphite", 133,
                new ItemType.Resource("Carbon", "Graphite"));
        compressedGraphite = new Item(Assets.carbon[2], "Compressed Graphite", 134,
                new ItemType.Resource("Carbon", "Compressed Graphite"));
        weakDiamondResource = new Item(Assets.carbon[3], "Weak Diamond", 135,
                new ItemType.Resource("Carbon", "Weak Diamond"));
        diamondResource = new Item(Assets.carbon[4], "Diamond", 136,
                new ItemType.Resource("Carbon", "Diamond"));
        reinforcedDiamondResource = new Item(Assets.carbon[5], "Reinforced Diamond", 137,
                new ItemType.Resource("Carbon", "Reinforced Diamond"));

        // PlaceableBlocks
        dirtItem = new Item(Assets.dirt, "Dirt", 0,
                new ItemType.PlaceableBlock(dirtTile.getID()));
        stoneItem = new Item(Assets.stone, "Stone", 1,
                new ItemType.PlaceableBlock(stoneTile.getID()));
            // Construction
            plywoodTileItem = new Item(Assets.plywood, "Plywood Tile", 2,
                new ItemType.PlaceableBlock.CreatableTile(plywoodTile.getID()));
            hardwoodTileItem = new Item(Assets.hardwood, "Hardwood Tile", 3,
                new ItemType.PlaceableBlock.CreatableTile(hardwoodTile.getID()));
            reinforcedHardwoodTileItem = new Item(Assets.reinforcedHardwood, "Reinforced Hardwood Tile", 4,
                new ItemType.PlaceableBlock.CreatableTile(reinforcedHardwoodTile.getID()));
            strongwoodTileItem = new Item(Assets.strongwood, "Strongwood Tile", 5,
                new ItemType.PlaceableBlock.CreatableTile(strongwoodTile.getID()));
            reinforcedStrongwoodTileItem = new Item(Assets.reinforcedStrongwood, "Reinforced Strongwood Tile", 6,
                new ItemType.PlaceableBlock.CreatableTile(reinforcedStrongwoodTile.getID()));

        // Non-construction creatable tiles
        torchItem = new Item(Assets.torchThumb, "Torch", "Use this item to illuminate tiles\nwithin an 8 unit radius", 20,
                new ItemType.PlaceableBlock.CreatableTile(torchTile.getID()));
        crateItem = new Item(Assets.crate, "Crate", "Place this item to store items from your\ninventory, pipes, nodes, or assemblers.", 21,
                new ItemType.PlaceableBlock.CreatableTile.Storage(crateTile.getID()));
        ladderItem = new Item(Assets.ladder, "Ladder", 26,
                new ItemType.PlaceableBlock.CreatableTile(ladderTile.getID()));

        //CreatableItems
            // Tools
                // Drills
                simpleDrillItem = new Item(Assets.simpleDrill, "Copper Drill", 10,
                    new ItemType.CreatableItem.Tool.Drill("copper-drill", 5, 1));

                wrenchItem = new Item(Assets.wrench, "Wrench", 25,
                    new ItemType.CreatableItem.Tool.Wrench(1));

                // Mechanical Drill
                copperMechanicalDrillItem = new Item(Assets.copperMechanicalDrill[0], "Copper Mechanical Drill", 22,
                    new ItemType.PlaceableBlock.CreatableTile.MechanicalDrill(copperMechanicalDrillTile.getID()));

                // Node
                nodeItem = new Item(Assets.node[0], "Node", 24,
                    new ItemType.PlaceableBlock.CreatableTile.Node(nodeTile.getID()));

                // Pipes
                stonePipeItem = new Item(Assets.stonePipeUp[0], "Stone Pipe", 23,
                    new ItemType.PlaceableBlock.CreatableTile.Pipe(stonePipeTile.getID()));

                // Assembler
                assemblerItem = new Item(Assets.assembler, "Assembler", 27,
                    new ItemType.PlaceableBlock.CreatableTile.Assembler(assemblerTile.getID()));

            // Weapons
                blasterBaseItem = new Item(Assets.blasterBase, "Blaster Base", 28,
                    new ItemType.PlaceableBlock.CreatableTile.BlasterBase(blasterBaseTile.getID()));

        // Item Recipes

        // Wood
        STICK_RESOURCE_RECIPE = new ItemRecipe.SticksRecipe(Assets.stickResource);
        PLYWOOD_RESOURCE_RECIPE = new ItemRecipe.PlywoodRecipe(Assets.plywoodResource);
        HARDWOOD_RESOURCE_RECIPE = new ItemRecipe.HardwoodRecipe(Assets.hardwoodResource);
        REINFORCED_HARDWOOD_RECIPE = new ItemRecipe.ReinforcedHardwoodRecipe(Assets.reinforcedHardwoodResource);
        STRONGWOOD_RECIPE = new ItemRecipe.StrongwoodRecipe(Assets.strongwoodResource);
        REINFORCED_STRONGWOOD_RECIPE = new ItemRecipe.ReinforcedStrongwoodRecipe(Assets.reinforcedStrongwoodResource);

        // Stone
        GRAVEL_RESOURCE_RECIPE = new ItemRecipe.GravelRecipe(Assets.gravelResource);
        CONCRETE_RESOURCE_RECIPE = new ItemRecipe.ConcreteRecipe(Assets.concreteResource);
        REINFORCED_CONCRETE_RESOURCE_RECIPE = new ItemRecipe.ReinforcedConcreteRecipe(Assets.reinforcedConcreteResource);
        CONDENSED_SLAB_RESOURCE_RECIPE = new ItemRecipe.CondensedSlabRecipe(Assets.condensedSlabResource);
        STRONGSTONE_RESOURCE_RECIPE = new ItemRecipe.StrongstoneRecipe(Assets.strongstoneResource);
        REINFORCED_STRONGSTONE_RESOURCE_RECIPE = new ItemRecipe.ReinforcedStrongstoneRecipe(Assets.reinforcedStrongstoneResource);

        // Copper
        RECYCLED_COPPER_RECIPE = new ItemRecipe.RecycledCopperRecipe(Assets.recycledCopperResource);
        BLUESTONE_RECIPE = new ItemRecipe.BluestoneRecipe(Assets.bluestoneResource);
        REINFORCED_BLUESTONE_RECIPE = new ItemRecipe.ReinforcedBluestoneRecipe(Assets.reinforcedBluestoneResource);
        ROMAN_VITRIOL_RECIPE = new ItemRecipe.RomanVitriolRecipe(Assets.romanVitriolResource);

        // Silver
        RECYCLED_SILVER_RECIPE = new ItemRecipe.RecycledSilverRecipe(Assets.recycledSilverResource);
        COIN_SILVER_RECIPE = new ItemRecipe.CoinSilverRecipe(Assets.coinSilverResource);
        STERLING_SILVER_RECIPE = new ItemRecipe.SterlingSilverRecipe(Assets.sterlingSilverResource);
        REINFORCED_STERLING_SILVER_RECIPE = new ItemRecipe.ReinforcedSterlingSilverRecipe(Assets.reinforcedSterlingSilverResource);
        FINE_SILVER_RECIPE = new ItemRecipe.FineSilverRecipe(Assets.fineSilverResource);
        REINFORCED_FINE_SILVER_RECIPE = new ItemRecipe.ReinforcedFineSilverRecipe(Assets.reinforcedFineSilverResource);

        // Iron
        RECYCLED_IRON_RECIPE = new ItemRecipe.RecycledIronRecipe(Assets.recycledIronResource);
        CAST_IRON_RECIPE = new ItemRecipe.CastIronRecipe(Assets.castIronResource);
        REINFORCED_CAST_IRON_RECIPE = new ItemRecipe.ReinforcedCastIronRecipe(Assets.reinforcedCastIronResource);
        STEEL_RECIPE = new ItemRecipe.SteelRecipe(Assets.steelResource);
        REINFORCED_STEEL_RECIPE = new ItemRecipe.ReinforcedSteelRecipe(Assets.reinforcedSteelResource);

        // Carbon
        GRAPHITE_RECIPE = new ItemRecipe.GraphiteRecipe(Assets.graphiteResource);
        COMPRESSED_GRAPHITE_RECIPE = new ItemRecipe.CompressedGraphiteRecipe(Assets.compressedGraphite);
        WEAK_DIAMOND_RECIPE = new ItemRecipe.WeakDiamondRecipe(Assets.weakDiamondResource);
        DIAMOND_RECIPE = new ItemRecipe.DiamondRecipe(Assets.diamondResource);
        REINFORCED_DIAMOND_RECIPE = new ItemRecipe.ReinforcedDiamondRecipe(Assets.reinforcedDiamondResource);

        // CreatableItems
            // Utilities (Tools above)
                // Drills
                SIMPLE_DRILL_RECIPE = new ItemRecipe.SimpleDrillRecipe(Assets.simpleDrillItem);

                // Wrench
                WRENCH_RECIPE = new ItemRecipe.WrenchRecipe(Assets.wrenchItem);

                // Ladder
                LADDER_RECIPE = new ItemRecipe.LadderRecipe(Assets.ladderItem);

            TORCH_RECIPE = new ItemRecipe.TorchRecipe(Assets.torchItem);
            CRATE_RECIPE = new ItemRecipe.CrateRecipe(Assets.crateItem);

        // Construction
        PLYWOOD_TILE_RECIPE = new ItemRecipe.PlywoodTileRecipe(Assets.plywoodTileItem);
        HARDWOOD_TILE_RECIPE = new ItemRecipe.HardwoodTileRecipe(Assets.hardwoodTileItem);
        REINFORCED_HARDWOOD_TILE_RECIPE = new ItemRecipe.ReinforcedHardwoodTileRecipe(Assets.reinforcedHardwoodTileItem);
        STRONGWOOD_TILE_RECIPE = new ItemRecipe.StrongwoodTileRecipe(Assets.strongwoodTileItem);
        REINFORCED_STRONGWOOD_TILE_RECIPE = new ItemRecipe.ReinforcedStrongwoodTileRecipe(Assets.reinforcedStrongwoodTileItem);

        // Machines
            // Mech Drills
            COPPER_MECHANICAL_DRILL_RECIPE = new ItemRecipe.CopperMechanicalDrillRecipe(Assets.copperMechanicalDrillItem);

            // Node
            NODE_RECIPE = new ItemRecipe.NodeRecipe(Assets.nodeItem);

            // Pipes
            STONE_PIPE_RECIPE = new ItemRecipe.StonePipeRecipe(Assets.stonePipeItem);

            // Assembler
            ASSEMBLER_RECIPE = new ItemRecipe.AssemblerRecipe(Assets.assemblerItem);

        // Weapons
            BLASTER_BASE_RECIPE = new ItemRecipe.BlasterBaseRecipe(Assets.blasterBaseItem);

        // Item Representation Lists
        resourceRepList = new ArrayList<>();
        resourceRepList.add(stickResource);
        resourceRepList.add(plywoodResource);
        resourceRepList.add(hardwoodResource);
        resourceRepList.add(reinforcedHardwoodResource);
        resourceRepList.add(strongwoodResource);
        resourceRepList.add(reinforcedStrongwoodResource);
        resourceRepList.add(gravelResource);
        resourceRepList.add(concreteResource);
        resourceRepList.add(reinforcedConcreteResource);
        resourceRepList.add(condensedSlabResource);
        resourceRepList.add(strongstoneResource);
        resourceRepList.add(reinforcedStrongstoneResource);
        resourceRepList.add(recycledCopperResource);
        resourceRepList.add(bluestoneResource);
        resourceRepList.add(reinforcedBluestoneResource);
        resourceRepList.add(romanVitriolResource);
        resourceRepList.add(recycledSilverResource);
        resourceRepList.add(coinSilverResource);
        resourceRepList.add(sterlingSilverResource);
        resourceRepList.add(reinforcedSterlingSilverResource);
        resourceRepList.add(fineSilverResource);
        resourceRepList.add(reinforcedFineSilverResource);
        resourceRepList.add(recycledIronResource);
        resourceRepList.add(castIronResource);
        resourceRepList.add(reinforcedCastIronResource);
        resourceRepList.add(steelResource);
        resourceRepList.add(reinforcedSteelResource);
        resourceRepList.add(graphiteResource);
        resourceRepList.add(compressedGraphite);
        resourceRepList.add(weakDiamondResource);
        resourceRepList.add(diamondResource);
        resourceRepList.add(reinforcedDiamondResource);
        // resourceRepList.add(reinforcedDiamondResource);

        toolRepList = new ArrayList<>();
        toolRepList.add(simpleDrillItem);
        toolRepList.add(wrenchItem);

        utilitiesRepList = new ArrayList<>();
        utilitiesRepList.add(torchItem);
        utilitiesRepList.add(crateItem);

        tilesRepList = new ArrayList<>();
        tilesRepList.add(ladderItem);
        tilesRepList.add(plywoodTileItem);
        tilesRepList.add(hardwoodTileItem);
        tilesRepList.add(reinforcedHardwoodTileItem);
        tilesRepList.add(strongwoodTileItem);
        tilesRepList.add(reinforcedStrongwoodTileItem);

        defenseRepList = new ArrayList<>();
        defenseRepList.add(blasterBaseItem);

        machinesRepList = new ArrayList<>();
        machinesRepList.add(stonePipeItem);
        machinesRepList.add(nodeItem);
        machinesRepList.add(copperMechanicalDrillItem);
        machinesRepList.add(assemblerItem);

        miscRepList = new ArrayList<>();

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

    public static ArrayList<Particle> initParticles() {
        particles = new ArrayList<>();

        particles.add(new Particle("fire.pcl", "fire"));

        return particles;
    }

}
