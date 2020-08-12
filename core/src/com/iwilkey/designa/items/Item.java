package com.iwilkey.designa.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.tiles.Tile;

import java.awt.Rectangle;

public class Item {

    public enum Items {

        DIRT(0),
        STONE(1),
        SIMPLE_DRILL(10);

        private int ID;
        Items(int ID) {
            this.ID = ID;
        }
        public int ID() { return ID; }
    }

    public static Item[] items = new Item[256];

    // Item List
    public static Item dirtItem = new Item(Assets.dirt, "Dirt", 0,
            new ItemType.PlaceableBlock(Tile.dirtTile.getID()));
    public static Item stoneItem = new Item(Assets.stone, "Stone", 1,
            new ItemType.PlaceableBlock(Tile.stoneTile.getID()));
    public static Item simpleDrill = new Item(Assets.simpleDrill, "Simple Drill", 10,
            new ItemType.Drill("simple-drill", 5, 1, ItemRecipe.SIMPLE_DRILL_RECIPE));


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
        for(int i = 0; i < items.length; i++) {
            if(items[i].getItemID() == ID) return items[i];
        }

        return null;
    }

    public Item createNew(int x, int y) {
        Item i = new Item(texture, name, itemID, type);
        i.setPosition(x, y);
        return i;
    }

    public void tick() {
        if(gb.getWorld().getEntityHandler().getPlayer().
                getCollisionBounds(0f,0f).intersects(bounds)) {
            if(gb.getWorld().getEntityHandler().getPlayer().getInventory().addItem(this) == 1) pickedUp = true;
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
