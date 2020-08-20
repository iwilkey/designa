package com.iwilkey.designa.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;

import java.awt.Rectangle;

public class Item {

    public static Item[] items = new Item[256];

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
