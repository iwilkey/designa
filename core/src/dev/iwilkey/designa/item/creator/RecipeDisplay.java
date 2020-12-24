package dev.iwilkey.designa.item.creator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.inventory.Inventory;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.Recipe;
import dev.iwilkey.designa.ui.UIManager;
import dev.iwilkey.designa.ui.UIText;

import java.util.Map;

public class RecipeDisplay {

    final byte INGREDIENT_SIZE = 24, SPACING = 8;

    int x, y;
    UIManager uiManager;
    UIText name;
    UIText[] amounts,
        needed;
    Recipe recipe;
    byte lookat = 0;
    Inventory inventory;

    public RecipeDisplay(UIManager uiManager, Inventory inv, int x, int y) {
        this.uiManager = uiManager;
        this.inventory = inv;
        this.x = x; this.y = y;
        name = uiManager.addText(new UIText("", 32, x, y - 12));

        amounts = new UIText[5]; needed = new UIText[5];
        for(int i = 0; i < amounts.length; i++) {
            amounts[i] = new UIText("", 12, x + MathUtils.random(-12, 12), y - 12);
            needed[i] = new UIText("", 18, 0, 0);
            uiManager.addText(amounts[i]); uiManager.addText(needed[i]);
        }

        recipe = null;
    }

    byte c = 0;
    short xx, yy;
    public void renderRecipe(Batch b, CategoryItemRecipeList cirl, Item item, byte slotNumber) {
        if(lookat != slotNumber) lookat = slotNumber;
        else {
            for (UIText uiText : needed) uiText.message = "";
        }

        if(item == null) {
            name.message = "Item Creator";
            for(UIText t : amounts) t.message = "";
            for (UIText uiText : needed) uiText.message = "";
            return;
        }

        if(cirl != null) {
            if (cirl.canCreate[slotNumber] == 1) {
                // We can create this item
                needed[0].message = "";
            } else {
                needed[0].message = "";
            }
            needed[0].x = (cirl.x + (cirl.width / 2)) - ((needed[0].message.length() * 12) / 2);
            needed[0].y = cirl.y + cirl.height + 14;
        }

        name.message = item.getName();
        if(item.getRecipe() != recipe) for(UIText t : amounts) t.message = "";
        recipe = item.getRecipe();
        if(recipe == null) {
            for(UIText t : amounts) t.message = "";
            return;
        }

        c = 0;
        for(Map.Entry<String, Integer> ingredient : recipe.getRecipe().entrySet()) {
            Item i = Item.getItemFromString(ingredient.getKey());
            
            b.draw(i.getTexture(), x, (yy - (24 * c) - (INGREDIENT_SIZE - 6)), INGREDIENT_SIZE, INGREDIENT_SIZE);
            
            xx = (short)(x + 16); 
            yy = (short)(y - 44);

            needed[c + 1].x = xx;
            needed[c + 1].y = (yy - (24 * c));
            needed[c + 1].message = " " + inventory.amountOf(Item.getItemFromString(ingredient.getKey())) +
                    "/" + ingredient.getValue() + " " + ingredient.getKey(); 

            if(inventory.amountOf(Item.getItemFromString(ingredient.getKey())) >= ingredient.getValue()) {
                b.draw(Assets.greenCheck, needed[c + 1].x + (needed[c + 1].message.length() * 12) + 12,
                        needed[c + 1].y - 11, 16, 16);
            } else {
                b.draw(Assets.redX, needed[c + 1].x + (needed[c + 1].message.length() * 12) + 12,
                        needed[c + 1].y - 11, 16, 16);
            }
            
            c++;
        }

    }

}
