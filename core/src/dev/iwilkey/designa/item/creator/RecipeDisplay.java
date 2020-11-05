package dev.iwilkey.designa.item.creator;

import com.badlogic.gdx.graphics.g2d.Batch;

import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.ui.UIManager;
import dev.iwilkey.designa.ui.UIText;

public class RecipeDisplay {

    UIManager uiManager;
    UIText name;

    public RecipeDisplay(UIManager uiManager, int x, int y) {
        this.uiManager = uiManager;
        name = uiManager.addText(new UIText("", 20, x, y));
    }

    public void renderRecipe(Batch b, Item item) {
        if(item == null) return;
        name.message = item.name();
    }

}
