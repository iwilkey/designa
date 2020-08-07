package com.iwilkey.designa.inventory;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.items.Item;

import java.awt.Rectangle;

public class InventorySlot {

    public static final int SLOT_WIDTH = 50, SLOT_HEIGHT = 50;
    public static final int TEXTURE_SIZE = 18, PADDING = 6;
    private final Inventory inventory;
    private final int cellNumber;
    private Rectangle collider;
    private Item item;
    public int itemCount;
    public boolean isSelected = false;

    public InventorySlot(Inventory i, int num) {
        this.inventory = i;
        this.cellNumber = num;

        collider = new Rectangle(0,0,SLOT_WIDTH, SLOT_HEIGHT);

        item = null;
        itemCount = 0;
    }

    public void tick() {}

    public void render(Batch b, int x, int y) {

        b.draw(Assets.inventorySlot, x + 12, y + 12, SLOT_WIDTH, SLOT_HEIGHT);
        collider.x = (int) (x + 12); collider.y = (int) (y + 12);

        if(item != null) {
            if(itemCount < 10) Text.draw(b, Integer.toString(itemCount), collider.x + (SLOT_WIDTH / 2) - 5, collider.y + PADDING + 2, 10);
            else if(itemCount < 100) Text.draw(b, Integer.toString(itemCount), collider.x + (SLOT_WIDTH / 2) - 9, collider.y + PADDING + 2, 9);
            else if(itemCount == 100) Text.draw(b, Integer.toString(itemCount), collider.x + (SLOT_WIDTH / 2) - 12, collider.y + PADDING + 2, 8);
            b.draw(item.getTexture(), collider.x + (SLOT_WIDTH / 2f) - (TEXTURE_SIZE / 2f),
                    collider.y + (SLOT_HEIGHT / 2f) - (TEXTURE_SIZE / 2f) + PADDING, TEXTURE_SIZE, TEXTURE_SIZE);
        }

        if(isSelected) {
            b.draw(Assets.inventorySelector, collider.x, collider.y, SLOT_WIDTH, SLOT_HEIGHT);
        }

    }

    public Item getItem() { return item; }
    public void putItem(Item i, int c) {
        this.item = i;
        itemCount = c;
    }
    public Rectangle getCollider() { return collider; }


}
