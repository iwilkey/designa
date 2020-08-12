package com.iwilkey.designa.blueprints;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
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

    public ItemRepresentation(BlueprintSection bs, Item item, int n) {
        this.bs = bs;
        this.item = item;

        this.recipe = (((ItemType.CreatableItem) item.getItemType()).getRecipe());

        this.number = n + 1;

        this.i = 32;
        this.w = 48; this.h = 48;
        this.x = (892 + (((number) % ROW_CAP) * w)) - (w / 2);
        int nextY = ((int)((double)(number / ROW_CAP))) + 1;
        this.y = bs.tabY - (88 - 50) - (50 * nextY);

        collider = new Rectangle(this.x, this.y, this.w, this.h);
    }

    public void tick() {}

    public void render(Batch b) {
        b.draw(Assets.itemRep, x, y, w, h);
        b.draw(item.getTexture(), x + (i / 4), y + (i / 4), i, i);

        if(isSelected) {
            b.draw(Assets.inventorySelector, x, y, w, h);

            Text.draw(b, "Items Required", 892 + 32, 210, 8);

            // TODO: Make this centered and maybe more efficient
            int recipeSize = recipe.getRecipe().size();
            int c = 0;
            for(Map.Entry<String, String> entry : recipe.getRecipe().entrySet()) {
                Item i = Item.getItemByID(Utils.parseInt(entry.getKey()));
                b.draw(i.getTexture(), 892 + 32 + c, 175, 16, 16);
                Text.draw(b, "x" + Utils.toString(Utils.parseInt(entry.getValue())), 892 + 32 + c + 8, 175, 8);
                c += 40;
                if(c + 40 > 80) c = 0;
            }


        }
    }

    public boolean isSelected() { return this.isSelected; }
    public void setSelected(boolean s) { this.isSelected = s; }

}
