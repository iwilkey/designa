package com.iwilkey.designa.blueprints;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.inventory.InventorySlot;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemRecipe;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.utils.Utils;

import java.awt.Rectangle;
import java.util.Map;

public class ItemRepresentation {

    private BlueprintSection bs;
    private Item item;
    private ItemRecipe recipe;
    private int number;

    private boolean isSelected = false;
    public Rectangle collider;

    private final int x, y, w, h, i, ROW_CAP = 5;

    private boolean canCreate = false;

    public ItemRepresentation(BlueprintSection bs, Item item, int n) {
        this.bs = bs;
        this.item = item;

        if(item.getItemType() instanceof  ItemType.CreatableItem)
            this.recipe = (((ItemType.CreatableItem) item.getItemType()).getRecipe());
        else if(item.getItemType() instanceof  ItemType.CreatableTile)
            this.recipe = (((ItemType.CreatableTile) item.getItemType()).getRecipe());

        this.number = n + 1;

        this.i = 32;
        this.w = 48; this.h = 48;
        this.x = (892 + (((number) % ROW_CAP) * w)) - (w / 2);
        int nextY = ((int)((double)(number / ROW_CAP))) + 1;
        this.y = bs.tabY - (88 - 50) - (50 * nextY);

        collider = new Rectangle(this.x, this.y, this.w, this.h);
    }

    public void tick() {}

    private boolean checkResources() {
        // TODO: Make this work.
        InventorySlot[][] slots = bs.workbench.inventory.slots;

        int checkout = 0;
        for(int y = 0; y < bs.workbench.inventory.invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for(int x = 0; x < bs.workbench.inventory.invWidth / InventorySlot.SLOT_WIDTH; x++) {
                if(slots[x][y] != null) {
                    for(Map.Entry<Item, String> entry : recipe.getRecipe().entrySet()) {
                        if(slots[x][y].getItem() != null) {
                            if (slots[x][y].getItem() == entry.getKey()) {
                                if (slots[x][y].itemCount >= Utils.parseInt(entry.getValue())) {
                                    checkout++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        canCreate = checkout >= recipe.getRecipe().size();
        return canCreate;
    }

    public void render(Batch b) {
        b.draw(Assets.itemRep, x, y, w, h);
        b.draw(item.getTexture(), x + (i / 4), y + (i / 4), i, i);

        if(isSelected) {
            b.draw(Assets.inventorySelector, x, y, w, h);

            Text.draw(b, "Items Required", 892 + 32, 210, 8);

            // TODO: Make this centered and maybe more efficient
            int recipeSize = recipe.getRecipe().size();
            int c = 0;
            for(Map.Entry<Item, String> entry : recipe.getRecipe().entrySet()) {
                b.draw(entry.getKey().getTexture(), 892 + 32 + c, 175, 16, 16);
                Text.draw(b, "x" + Utils.toString(Utils.parseInt(entry.getValue())), 892 + 32 + c + 8, 175, 8);
                c += 40;
                if(c + 40 > 80) c = 0;
            }

            System.out.println(checkResources());

        }
    }

    public boolean isSelected() { return this.isSelected; }
    public void setSelected(boolean s) { this.isSelected = s; }

}
