package com.iwilkey.designa.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

import java.awt.Rectangle;

public class Item {

    public static Item[] items = new Item[256];

    // Item List

    // PlaceableBlocks
    public static Item dirtItem = new Item(Assets.dirt, "Dirt", 0,
            new ItemType.PlaceableBlock(Tile.dirtTile.getID()));
    public static Item stoneItem = new Item(Assets.stone, "Stone", 1,
            new ItemType.PlaceableBlock(Tile.stoneTile.getID()));
    public static Item oakWoodItem = new Item(Assets.oakWood, "OakWood", 2,
            new ItemType.PlaceableBlock(Tile.oakWoodTile.getID()));
        // CreatableTiles
        public static Item torchItem = new Item(Assets.torchThumb, "Torch", 20,
                new ItemType.PlaceableBlock.CreatableTile(Tile.torchTile.getID(), ItemRecipe.TORCH_RECIPE));

    //CreatableItems
        // Tools
            // Drills
            public static Item simpleDrill = new Item(Assets.simpleDrill, "Simple Drill", 10,
                    new ItemType.CreatableItem.Tool.Drill("simple-drill", 5, 1, ItemRecipe.SIMPLE_DRILL_RECIPE));

    // Resources
        // Wood
        public static Item barkResource = new Item(Assets.wood[0], "Bark", 100,
            new ItemType.Resource("Wood", "Bark", null));
        public static Item stickResource = new Item(Assets.wood[1], "Sticks", 101,
            new ItemType.Resource("Wood", "Sticks", ItemRecipe.STICK_RESOURCE_RECIPE));
        public static Item plywoodResource = new Item(Assets.wood[2], "Plywood", 102,
            new ItemType.Resource("Wood", "Plywood", ItemRecipe.PLYWOOD_RESOURCE_RECIPE));
        public static Item hardwoodResource = new Item(Assets.wood[3], "Hardwood", 103,
            new ItemType.Resource("Wood", "Hardwood", ItemRecipe.HARDWOOD_RESOURCE_RECIPE));
        public static Item reinforcedHardwoodResource = new Item(Assets.wood[4], "Reinforced Hardwood", 104,
            new ItemType.Resource("Wood", "Reinforced Hardwood", ItemRecipe.REINFORCED_HARDWOOD_RECIPE));
        public static Item strongwoodResource = new Item(Assets.wood[5], "Strongwood", 105,
            new ItemType.Resource("Wood", "Strongwood", ItemRecipe.STRONGWOOD_RECIPE));
        public static Item reinforcedStrongwoodResource = new Item(Assets.wood[6], "Reinforced Strongwood", 106,
            new ItemType.Resource("Wood", "Reinforced Strongwood", ItemRecipe.REINFORCED_STRONGWOOD_RECIPE));

        // Stone
        public static Item rockResource = new Item(Assets.rock[0], "Rocks", 107,
            new ItemType.Resource("Stone", "Rocks", null));
        public static Item gravelResource = new Item(Assets.rock[1], "Gravel", 108,
            new ItemType.Resource("Stone", "Gravel", ItemRecipe.GRAVEL_RESOURCE_RECIPE));
        public static Item concreteResource = new Item(Assets.rock[2], "Concrete", 109,
            new ItemType.Resource("Stone", "Concrete", ItemRecipe.CONCRETE_RESOURCE_RECIPE));
        public static Item reinforcedConcreteResource = new Item(Assets.rock[3], "Reinforced Concrete", 110,
            new ItemType.Resource("Stone", "Reinforced Concrete", ItemRecipe.REINFORCED_CONCRETE_RESOURCE_RECIPE));
        public static Item condensedSlabResource = new Item(Assets.rock[4], "Condensed Slab", 111,
            new ItemType.Resource("Stone", "Condensed Slab", ItemRecipe.CONDENSED_SLAB_RESOURCE_RECIPE));
        public static Item strongstoneResource = new Item(Assets.rock[5], "Strongstone", 112,
            new ItemType.Resource("Stone", "Strongstone", ItemRecipe.STRONGSTONE_RESOURCE_RECIPE));
        public static Item reinforcedStrongstoneResource = new Item(Assets.rock[6], "Reinforced Strongstone", 113,
            new ItemType.Resource("Stone", "Reinforced Strongstone", ItemRecipe.REINFORCED_STRONGSTONE_RESOURCE_RECIPE));


    public static final int ITEM_WIDTH = 8, ITEM_HEIGHT = 8;

    protected GameBuffer gb;
    protected TextureRegion texture;
    protected String name;
    protected final int itemID;
    protected ItemType type;
    protected Rectangle bounds;
    protected int x, y, count;
    protected boolean pickedUp = false;

    // Mock physics
    protected float gravity = -3.5f;
    protected float timeInAir = 0.0f;
    protected boolean isGrounded = false;

    public Item(TextureRegion tex, String name, int ID, ItemType t) {
        this.texture = tex;
        this.name = name;
        this.itemID = ID;
        this.type = t;
        count = 1;
        bounds = new Rectangle(x, y, ITEM_WIDTH, ITEM_HEIGHT);
        items[ID] = this;
    }

    public static Item getItemByID(int ID) {
        for (Item item : items) {
            if(item != null) if (item.getItemID() == ID) return item;
        }

        return Item.dirtItem;
    }

    public Item createNew(int x, int y) {
        Item i = new Item(texture, name, itemID, type);
        i.setPosition(x, y);
        return i;
    }

    public void tick() {
        if(gb.getWorld().getEntityHandler().getPlayer().
                getCollisionBounds(0f,0f).intersects(bounds)) {
            if(gb.getWorld().getEntityHandler().getPlayer().getInventory().addItem(this) == 1) {
                pickedUp = true;

                Assets.itemPickup[MathUtils.random(0,2)].play();

            }
        }
    }

    public void render(Batch b) { // Render in game world
        if(gb == null) {
            return;
        }

        render(b, x, y);
    }

    public void render(Batch b, int x, int y) {
        b.draw(texture, x, y, ITEM_WIDTH, ITEM_HEIGHT);
    }

    public void setPosition(int x, int y) {
        this.x = x; this.y = y;
        bounds.x = x; bounds.y = y;
    }

    // Getters and setters

    public void setGameBuffer(GameBuffer gb) { this.gb = gb; }

    public TextureRegion getTexture() { return texture; }
    public void setTexture(TextureRegion tex) { texture = tex; }

    public String getName() { return name; }

    public int getX() { return x; }
    public int getY() { return y; }

    public int getCount() { return count; }
    public void setCount(int c) { count = c; }

    public int getItemID() { return itemID; }
    public ItemType getItemType() { return type; }

    public boolean isPickedUp() { return pickedUp; }

}
