package dev.iwilkey.designa.item.creator;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.inventory.Inventory;
import dev.iwilkey.designa.inventory.Slot;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.Recipe;
import dev.iwilkey.designa.ui.UIManager;
import dev.iwilkey.designa.ui.UIText;

import java.awt.Rectangle;
import java.util.Map;

public class ItemCreator {

    public static boolean isActive;
    public static UIManager uiManager;

    Rectangle activeCollider;
    Player player;
    RecipeDisplay recipeDisplay;

    public Inventory inventory;

    public ItemCreator(Player player, int x, int y, int width, int height) {

        isActive = false;
        activeCollider = new Rectangle(x, y, width, height);
        uiManager = new UIManager("item-creator");
        recipeDisplay = new RecipeDisplay(uiManager, 120, Game.WINDOW_HEIGHT - 10);
        inventory = player.inventory;

        for(int i = 0; i < Category.values().length; i++) {
            uiManager.addCategoryItemRecipeList(new CategoryItemRecipeList(this, Category.values()[i], x + 132, y -
                    ((CategoryItemRecipeList.SLOT_SIZE + CategoryItemRecipeList.SLOT_SPACE) * (2 + i)) - 20,
                    ((CategoryItemRecipeList.SLOT_SIZE + CategoryItemRecipeList.SLOT_SPACE) * 5), CategoryItemRecipeList.SLOT_SIZE));
            uiManager.addText(new UIText(Category.values()[i].name(), 20,10, Game.WINDOW_HEIGHT -
                    ((CategoryItemRecipeList.SLOT_SIZE + CategoryItemRecipeList.SLOT_SPACE) * (2 + i)) + 8));
        }

    }

    public void tick() {
        if(!isActive) return;
        uiManager.tick();
    }

    byte i = 0;
    public void render(Batch b) {
        if(!isActive) return;
        uiManager.render(b);

        i = 0;
        for(CategoryItemRecipeList l : uiManager.categoryItemList)
            try {
                if(l.hovering) {
                    i = 1;
                    recipeDisplay.renderRecipe(b, l.selectedSlot().item);
                }
            } catch (NullPointerException ignored) {}
        if(i == 0) recipeDisplay.renderRecipe(b, null);

    }

}
