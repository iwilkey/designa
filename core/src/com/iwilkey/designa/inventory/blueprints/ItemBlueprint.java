package com.iwilkey.designa.inventory.blueprints;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.InventorySlot;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemRecipe;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.utils.Utils;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class ItemBlueprint {

    private BlueprintSection bs;
    private Item item;
    private ItemRecipe recipe;
    private int number;

    public boolean isSelected = false;
    public Rectangle collider, createCollider;

    private final int x, y, w, h, i, ROW_CAP = 5;

    public boolean canCreate = false;
    public boolean mouseOver = false;

    public ItemBlueprint(BlueprintSection bs, Item item, int n) {
        this.bs = bs;
        this.item = item;

        if(item.getItemType() instanceof  ItemType.CreatableItem)
            this.recipe = (((ItemType.CreatableItem) item.getItemType()).getRecipe());
        else if(item.getItemType() instanceof  ItemType.PlaceableBlock.CreatableTile)
            this.recipe = (((ItemType.PlaceableBlock.CreatableTile) item.getItemType()).getRecipe());

        this.number = n + 1;

        this.i = 32;
        this.w = 48; this.h = 48;
        this.x = (Inventory.BLUEPRINT_X + (((number) % ROW_CAP) * w)) - (w / 2);
        int nextY = ((int)((double)(number / ROW_CAP))) + 1;
        this.y = bs.tabY - (88 - 50) - (50 * nextY);

        collider = new Rectangle(this.x, this.y, this.w, this.h);
        createCollider = new Rectangle(Inventory.BLUEPRINT_X + 32 + 22, Inventory.BLUEPRINT_Y - 360 - 116, 82, 42);
    }

    public void tick() {
        checkResources();
    }

    public void checkResources() {
        InventorySlot[][] slots = Inventory.slots;
        HashMap<String, Integer> invTally = new HashMap<String, Integer>();

        for(Map.Entry<Item, String> entry : recipe.getRecipe().entrySet()) {

            for(int y = 0; y < bs.blueprints.inventory.invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                for (int x = 0; x < bs.blueprints.inventory.invWidth / InventorySlot.SLOT_WIDTH; x++) {
                    if (slots[x][y] != null) {
                        try {
                            if (slots[x][y].getItem().getItemID() == entry.getKey().getItemID()) {
                                if (!(invTally.containsKey(slots[x][y].getItem().getName()))) {
                                    invTally.put(slots[x][y].getItem().getName(), slots[x][y].itemCount);
                                } else {
                                    invTally.put(slots[x][y].getItem().getName(),
                                            invTally.get(slots[x][y].getItem().getName()) + slots[x][y].itemCount);
                                }
                            }
                        } catch (NullPointerException ignored) {}
                    }
                }
            }

        }

        int checkout = 0;
        for(Map.Entry<String, Integer> invTal : invTally.entrySet()) {
            for(Map.Entry<Item, String> recipe : recipe.getRecipe().entrySet()) {
                if(invTal.getKey().equals(recipe.getKey().getName())) {
                    if(invTal.getValue() >= Utils.parseInt(recipe.getValue())) {
                        checkout++;
                        break;
                    }
                }
            }
        }

        canCreate = checkout >= recipe.getRecipe().size();

    }

    public void create() {
        InventorySlot[][] slots = Inventory.slots;

        for (Map.Entry<Item, String> entry : recipe.getRecipe().entrySet()) {
            yLoop: for (int y = 0; y < bs.blueprints.inventory.invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                for (int x = 0; x < bs.blueprints.inventory.invWidth / InventorySlot.SLOT_WIDTH; x++) {
                    if (slots[x][y] != null) {
                        try {
                            if (slots[x][y].getItem().getItemID() == entry.getKey().getItemID()) {
                                slots[x][y].itemCount -= Math.min(slots[x][y].itemCount, Utils.parseInt(entry.getValue()));
                                break yLoop;
                            }
                        } catch (NullPointerException ignored) {}
                    }
                }
            }
        }

        bs.blueprints.inventory.addItem(item);
    }

    public void renderRep(Batch b) {
        b.draw(Assets.itemRep, x, y, w, h);
        b.draw(item.getTexture(), x + (i / 4f), y + (i / 4f), i, i);
    }

    public void render(Batch b) {

        if(mouseOver) {
            try {
                Text.draw(b, item.getName(), Inventory.BLUEPRINT_X + 32 + ((("Items Required".length() / 2) -
                        (item.getName().length() / 2)) * 11) - 20, Inventory.BLUEPRINT_Y - 340, 11);
            } catch (NullPointerException ignored) {}

            Text.draw(b, "Items Required", Inventory.BLUEPRINT_X + 32, Inventory.BLUEPRINT_Y - 360, 8);

            // TODO: Make this centered and maybe more efficient
            int recipeSize = recipe.getRecipe().size();
            int c = 0;
            for(Map.Entry<Item, String> entry : recipe.getRecipe().entrySet()) {
                b.draw(entry.getKey().getTexture(), Inventory.BLUEPRINT_X + 32 + c, Inventory.BLUEPRINT_Y - 360 - 32, 16, 16);
                Text.draw(b, "x" + Utils.toString(Utils.parseInt(entry.getValue())), Inventory.BLUEPRINT_X + 32 + c + 8, Inventory.BLUEPRINT_Y - 360 - 32, 8);
                c += 40;
                if(c + 40 > 80) c = 0;
            }
        }

        if(isSelected) {
            b.draw(Assets.inventorySelector, x, y, w, h);

            if(canCreate) {
                b.draw(Assets.inventorySlot, createCollider.x, createCollider.y, createCollider.width, createCollider.height);
                Text.draw(b, "Create", Inventory.BLUEPRINT_X + 32 + 27, Inventory.BLUEPRINT_Y - 360 - 100, 11);
            }

        }
    }

    public boolean isSelected() { return this.isSelected; }
    public void setSelected(boolean s) { this.isSelected = s; }

}
