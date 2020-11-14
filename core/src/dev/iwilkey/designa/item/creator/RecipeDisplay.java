package dev.iwilkey.designa.item.creator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

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
    UIText[] amounts;
    Recipe recipe;

    public RecipeDisplay(UIManager uiManager, int x, int y) {
        this.uiManager = uiManager;
        this.x = x; this.y = y;
        name = uiManager.addText(new UIText("", 32, x, y - 12));

        amounts = new UIText[5];
        for(int i = 0; i < amounts.length; i++) {
            amounts[i] = new UIText("", 12, x + MathUtils.random(-12, 12), y - 12);
            uiManager.addText(amounts[i]);
        }

        recipe = null;
    }

    byte c = 0;
    short xx, yy;
    public void renderRecipe(Batch b, Item item) {

        if(item == null) {
            name.message = "";
            for(UIText t : amounts) t.message = "";
            return;
        }

        name.message = item.name();
        if(item.getRecipe() != recipe) for(UIText t : amounts) t.message = "";
        recipe = item.getRecipe();
        if(recipe == null) {
            for(UIText t : amounts) t.message = "";
            return;
        }

        c = 0;
        for(Map.Entry<String, Integer> ingredient : recipe.getRecipe().entrySet()) {
            Item i = Item.getItemFromString(ingredient.getKey());

            xx = (short)(x + ((INGREDIENT_SIZE + SPACING) * c)); yy = (short)(y - 54 - (INGREDIENT_SIZE / 2f));
            amounts[c].x = xx; amounts[c].y = yy - SPACING;
            amounts[c].message = "x" + ingredient.getValue();

            b.draw(i.getTexture(), xx, yy, INGREDIENT_SIZE, INGREDIENT_SIZE);
            c++;
        }

    }

}
