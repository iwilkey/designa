package com.iwilkey.designa.inventory;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.items.ItemType;

public class ToolSlot {

    private final Inventory inventory;
    public int x, y;
    public final int w = 64, h = 64;

    private TextureRegion itemTexture;
    private int itemCount;

    public static InventorySlot currentItem;

    public ToolSlot(Inventory i) {
        this.inventory = i;
        currentItem = null;
        itemTexture = null;
    }

    public void tick() {
        for(int y = 0; y < inventory.invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for(int x = 0; x < inventory.invWidth / InventorySlot.SLOT_WIDTH; x++) {
                if(inventory.getSlots()[x][y].isSelected) {
                    if(inventory.getSlots()[x][y].getItem() != null){
                        itemTexture = inventory.getSlots()[x][y].getItem().getTexture();
                        itemCount = inventory.getSlots()[x][y].itemCount;
                        currentItem = inventory.getSlots()[x][y];
                        currentItem.tick();
                    }
                    else {
                        itemTexture = null;
                        currentItem = null;
                    }
                }
            }
        }
    }

    public void render(Batch b) {

        try {
            if (itemTexture != null) {
                if (!(currentItem.getItem().getItemType() instanceof ItemType.CreatableItem.Tool.Drill)) {

                    b.draw(itemTexture, x + (25 / 2f), y + (25 / 2f), w - 25, h - 25);
                    Text.draw(b, Integer.toString(itemCount), x, y - 4, 11);
                } else {
                    b.draw(itemTexture, x + (15 / 2f), y + (15 / 2f), w - 15, h - 15);
                }

                // TODO: Render the name of the item with the correct space based off of the size of the name.
                // Text.draw(b, currentItem.getItem().getName(), x, y, 11);
            }
        } catch (NullPointerException ignored) {}

    }

}
