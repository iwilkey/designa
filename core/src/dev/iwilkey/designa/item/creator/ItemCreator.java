package dev.iwilkey.designa.item.creator;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.ui.UIManager;
import dev.iwilkey.designa.ui.UIText;

import java.awt.*;

public class ItemCreator {

    public static boolean isActive;
    public static UIManager uiManager;

    Rectangle activeCollider;
    Player player;
    RecipeDisplay recipeDisplay;

    public ItemCreator(Player player, int x, int y, int width, int height) {
        isActive = true;
        activeCollider = new Rectangle(x, y, width, height);
        uiManager = new UIManager("item-creator");
        recipeDisplay = new RecipeDisplay(uiManager, 120, Game.WINDOW_HEIGHT - 10);

        for(int i = 0; i < Category.values().length; i++) {
            uiManager.addCategoryItemRecipeList(new CategoryItemRecipeList(Category.values()[Category.values().length - i - 1], x + 132, y -
                    ((CategoryItemRecipeList.SLOT_SIZE + CategoryItemRecipeList.SLOT_SPACE) * (2 + i)) - 20,
                    ((CategoryItemRecipeList.SLOT_SIZE + CategoryItemRecipeList.SLOT_SPACE) * 5), CategoryItemRecipeList.SLOT_SIZE));
            uiManager.addText(new UIText(Category.values()[i].name(), 20,10, Game.WINDOW_HEIGHT - ((CategoryItemRecipeList.SLOT_SIZE + CategoryItemRecipeList.SLOT_SPACE) * (2 + i)) + 8));
        }

    }

    public void tick() {
        if(!isActive) return;
        uiManager.tick();
    }

    public void render(Batch b) {
        if(!isActive) return;
        uiManager.render(b);

        for(CategoryItemRecipeList l : uiManager.categoryItemList)
            try {
                if(l.hovering) recipeDisplay.renderRecipe(b, l.selectedSlot().item);
            } catch (NullPointerException ignored) {}

    }

}
