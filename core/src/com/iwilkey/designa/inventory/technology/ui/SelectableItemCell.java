package com.iwilkey.designa.inventory.technology.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.InventorySlot;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemRecipe;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.utils.Utils;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectableItemCell extends SelectableItem {

    Item itemRep;
    boolean canCreate = false;
    public float offY = 0;
    public static int iIX, iIY, iIW, iIH;

    public SelectableItemCell(Item item, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.itemRep = item;
        iIX = x; iIY = y; iIW = x - 46; iIH = y - h - 84;
    }

    public void tick(ArrayList<SelectableItemCell> cells, int y, int h) {
        input(cells, y, h);
    }

    @Override
    public void onClick() {

    }

    public void input(ArrayList<SelectableItemCell> cells, int y, int h) {
        Rectangle mouse = new Rectangle();
        if(collider.y + offY < y || collider.y + offY > y + h - 64) return;
        if(InputHandler.leftMouseButton) {
            mouse = new Rectangle(InputHandler.cursorX, InputHandler.cursorY - (int)offY, 1, 1);
        }

        if (mouse.intersects(collider) && InputHandler.leftMouseButtonDown) {
            for(SelectableItemCell sic : cells) sic.isSelected = false;
            isSelected = true;
            onClick();
            Assets.invClick.play(0.35f);
        }

    }

    @Override
    public void render(Batch b) {
    }

    int count = 0; int finalDescY = 0; int recX = 0, recY = 0, xSpace = 24 + 64; long timer = 0, timer2 = 0, checkMax = 32;
    String message = "Sweeping inventory";
    public void render(Batch b, int x, int y, int h, int w) {
        if(collider.y + offY < y || collider.y + offY > y + h - 64) return;
        b.draw(Assets.inventorySlot, collider.x, collider.y + offY, collider.width, collider.height);
        b.draw(itemRep.getTexture(), collider.x + 12, collider.y + offY + 12, 24, 24);
        if(isSelected) {
            b.draw(Assets.inventorySelector, collider.x, collider.y + offY, collider.width, collider.height);
            Text.draw(b, itemRep.getName(), (x + w) - (w / 2) - ((itemRep.getName().length() * 12) / 2), y - 22, 12);

            String[] descriptionTokens = itemRep.getDescription().split("\n");
            for(int i = 0; i < descriptionTokens.length; i++) {
                Text.draw(b, descriptionTokens[i], (x + w) - (w / 2) - ((descriptionTokens[i].length() * 8) / 2) - 8, y - (10 * (i + 4)), 8);
                if(i == descriptionTokens.length - 1) {
                    finalDescY = y - (10 * (i + 4));
                }
            }

            // Show recipe
            ItemRecipe recipe = null;
            if(itemRep.getItemType() instanceof ItemType.Resource) {
                recipe = ((ItemType.Resource)(itemRep.getItemType())).getItemRecipe();
            } else if (itemRep.getItemType() instanceof ItemType.PlaceableBlock.CreatableTile) {
                recipe = ((ItemType.PlaceableBlock.CreatableTile)(itemRep.getItemType())).getRecipe();
            } else if (itemRep.getItemType() instanceof ItemType.CreatableItem) {
                recipe = ((ItemType.CreatableItem)(itemRep.getItemType())).getRecipe();
            }

            for(Map.Entry<Item, String> r : recipe.getRecipe().entrySet()) {
                recX = (int)(((x + w) - (w / 2f)) + (xSpace * count) -
                        (((recipe.getRecipe().entrySet().size() - 1) * xSpace) / 2f) - 8);
                recY = finalDescY - 28;
                b.draw(r.getKey().getTexture(), recX, recY, 24, 24);
                Text.draw(b, "x" + r.getValue(), recX + 26, recY, 8);

                if(r.getKey().getName().split(" ").length > 1) {
                    String[] nameTokens = r.getKey().getName().split(" ");
                    for(int i = 0; i < nameTokens.length; i++) {
                        Text.draw(b, nameTokens[i], recX -
                                (int)((nameTokens[i].length() * 8) / 2f) + 8, recY - 12 - (12 * i), 8);
                    }
                } else Text.draw(b, r.getKey().getName(), recX -
                        (int)((r.getKey().getName().length() * 8) / 2f), recY - 12, 8);
                count++;
            }
            count = 0;

            timer++;
            if(timer > checkMax) {
                canCreate = checkCanCreate(recipe);
                timer = 0;
            }

            if(canCreate) {

                Rectangle createCollider = new Rectangle((int)((x + w) - (w/2f)) - (int)(("Click here to create!".length() * 8) / 2f) - 12,
                        recY - 54, "Click here to create!".length() * 8 + 28, 16);
                b.draw(Assets.square, createCollider.x,
                        createCollider.y, createCollider.width, createCollider.height);
                Text.draw(b, "Click here to create!", (int)((x + w) - (w/2f)) - (int)(("Click here to create!".length() * 8) / 2f) - 8,
                        recY - 50, 8);

                // Creating!
                if(InputHandler.leftMouseButtonDown) {
                    Rectangle cursor = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                    if(cursor.intersects(createCollider)) {
                        canCreate = checkCanCreate(recipe);
                        if(canCreate) create(recipe);
                    }
                }

            } else {
                timer2++;
                if(timer2 > (checkMax / 3)) {
                    if(message.length() >= "Sweeping inventory".length() + 4) message = "Sweeping inventory";
                    message += ".";
                    timer2 = 0;
                }
                Text.draw(b, message, (int)((x + w) - (w/2f)) - (int)(("Sweeping inventory...".length() * 8) / 2f),
                        recY - 50, 8);
            }

        }
    }

    private boolean checkCanCreate(ItemRecipe recipe) {
        int valid = 0;
        try {
            recipeLoop: for (Map.Entry<Item, String> r : recipe.getRecipe().entrySet()) {
                for (int yy = 0; yy < 200 / InventorySlot.SLOT_HEIGHT; yy++) {
                    for (int xx = 0; xx < 400 / InventorySlot.SLOT_WIDTH; xx++) {
                        // If, while looping over the items in the inventory we find the recipe item...
                        if (Inventory.slots[xx][yy].getItem() != null) {
                            if (Inventory.slots[xx][yy].getItem().getName().equals(r.getKey().getName())) {
                                int amount = 0;

                                // Now, tabulate the total amount of that item in the inventory.
                                for (int yyy = 0; yyy < 200 / InventorySlot.SLOT_HEIGHT; yyy++) {
                                    for (int xxx = 0; xxx < 400 / InventorySlot.SLOT_WIDTH; xxx++) {
                                        if (Inventory.slots[xxx][yyy].getItem() != null) {
                                            if (Inventory.slots[xxx][yyy].getItem().getName().equals(r.getKey().getName())) {
                                                amount += Inventory.slots[xxx][yyy].itemCount;
                                            }
                                        }
                                    }
                                }

                                // Now, see if we have enough of that item, and increase valid.
                                if (amount >= Utils.parseInt(r.getValue())) {
                                    valid++;
                                    continue recipeLoop;
                                } else return false;
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {}
        return valid == recipe.getRecipe().size();
    }

    private boolean create(ItemRecipe recipe) {
        // We have perms to make this item!

        boolean full = true;
        for (int yy = 0; yy < 200 / InventorySlot.SLOT_HEIGHT; yy++) {
            for (int xx = 0; xx < 400 / InventorySlot.SLOT_WIDTH; xx++) {
                if (Inventory.slots[xx][yy].getItem() == null) {
                    full = false;
                    break;
                }
            }
        }

        if(full) return false;

        HashMap<Item, Integer> itemsLeft = new HashMap<>();
        for (Map.Entry<Item, String> r : recipe.getRecipe().entrySet())
            itemsLeft.put(r.getKey(), Utils.parseInt(r.getValue()));

        recipeLoop: for (Map.Entry<Item, String> r : recipe.getRecipe().entrySet()) {
            for (int yy = 0; yy < 200 / InventorySlot.SLOT_HEIGHT; yy++) {
                for (int xx = 0; xx < 400 / InventorySlot.SLOT_WIDTH; xx++) {
                    // If, while looping over the items in the inventory we find the recipe item...
                    if (Inventory.slots[xx][yy].getItem() != null) {
                        if (Inventory.slots[xx][yy].getItem().getName().equals(r.getKey().getName())) {
                            int itemCount = Inventory.slots[xx][yy].itemCount;
                            int itemLeft = itemsLeft.get(r.getKey());

                            // This means that we use up all the items in the current slot.
                            if(itemLeft - itemCount > 0) {
                                Inventory.slots[xx][yy].putItem(null, 0);
                                itemsLeft.put(r.getKey(), itemLeft - itemCount);
                                System.out.println("RAN!");
                            }

                            // This means that the slot exactly finishes the items left.
                            if(itemLeft - itemCount == 0) {
                                Inventory.slots[xx][yy].putItem(null, 0);
                                itemsLeft.put(r.getKey(), 0);
                                continue recipeLoop;
                            }

                            // This means that we used up all the items left but still have some slot items left over.
                            if(itemLeft - itemCount < 0) {
                                Inventory.slots[xx][yy].putItem(r.getKey(), itemCount - itemLeft);
                                itemsLeft.put(r.getKey(), 0);
                                continue recipeLoop;
                            }
                        }
                    }
                }
            }
        }

        Inventory.addItem(recipe.item);
        Assets.createItem[MathUtils.random(0,2)].play(0.35f);

        return true;
    }
}
