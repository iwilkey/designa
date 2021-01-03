package dev.iwilkey.designa.item.creator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.Settings;
import dev.iwilkey.designa.clock.Clock;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.gfx.Geometry;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.inventory.Inventory;
import dev.iwilkey.designa.inventory.Slot;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.Recipe;
import dev.iwilkey.designa.ui.ClickListener;
import dev.iwilkey.designa.ui.UIButton;
import dev.iwilkey.designa.ui.UIManager;
import dev.iwilkey.designa.ui.UIText;

import java.awt.Rectangle;
import java.util.Map;

public class ItemCreator {

    public static boolean isActive;
    public static UIManager uiManager;
    UIManager customizer;

    Rectangle activeCollider;
    Player player;
    RecipeDisplay recipeDisplay;

    public Inventory inventory;

    final UIButton CRAFTBOOK_MOVE;

    boolean customizingCraftbook = false;

    public ItemCreator(Player player, int x, int y, int width, int height) {

        isActive = false;
        activeCollider = new Rectangle(x, y, width, height);
        uiManager = new UIManager("item-creator");
        inventory = player.inventory;
        customizer = new UIManager("Item Customizer");
        recipeDisplay = new RecipeDisplay(uiManager, inventory, 120, Game.WINDOW_HEIGHT - 10);

        for(int i = 0; i < Category.values().length; i++) {
            uiManager.addCategoryItemRecipeList(new CategoryItemRecipeList(this, Category.values()[i], x + 180, y -
                    ((Settings.GUI_SLOT_SIZE + (Settings.GUI_SLOT_SPACING)) * (2 + i)) - 20 - 40,
                    ((Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING) * 5), Settings.GUI_SLOT_SIZE));
            uiManager.addText(new UIText(Category.values()[i].name(), 0,10, Game.WINDOW_HEIGHT -
                    ((Settings.GUI_SLOT_SIZE + (Settings.GUI_SLOT_SPACING)) * (2 + i)) + 8 - 50));
        }

        CRAFTBOOK_MOVE = customizer.addButton(new UIButton("", Settings.ITEM_CREATOR_POSITION.x + Settings.ITEM_CREATOR_POSITION.width - 40,
                Game.WINDOW_HEIGHT - 40, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                customizingCraftbook = true;
                new Thread() {
                    public void run() {
                        while(true) {
                            int multiplier = 2;
                            Settings.ITEM_CREATOR_POSITION.x += InputHandler.dx * multiplier;
                            Settings.ITEM_CREATOR_POSITION.y += InputHandler.dy * multiplier;

                            recipeDisplay.move((float)InputHandler.dx * multiplier, (float)InputHandler.dy * multiplier);
                            uiManager.relocate((float)InputHandler.dx * multiplier, (float)InputHandler.dy * multiplier);
                            CRAFTBOOK_MOVE.move(Settings.ITEM_CREATOR_POSITION.x + Settings.ITEM_CREATOR_POSITION.width - 40,
                                    Settings.ITEM_CREATOR_POSITION.y + Settings.ITEM_CREATOR_POSITION.height - 40);
                            if(InputHandler.leftMouseButton || InputHandler.rightMouseButton) {
                                customizingCraftbook = false;
                                break;
                            }

                            try { sleep(1000 / 40); } catch (InterruptedException ignored) {}
                        }
                    }
                }.start();
            }
        }));

    }

    public void tick() {
        if(!isActive) return;
        uiManager.tick();
        customizer.tick();
        Geometry.requests.add(new Geometry.GUIRectangleOutline(Settings.ITEM_CREATOR_POSITION.x,
                Settings.ITEM_CREATOR_POSITION.y, Settings.ITEM_CREATOR_POSITION.width, Settings.ITEM_CREATOR_POSITION.height, 6, Color.WHITE));

    }

    byte i = 0;
    long timer = 0;
    short craftTimeCap = 10;
    public void render(Batch b) {
        if(!isActive) return;
        uiManager.render(b);
        customizer.render(b);

        i = 0;
        for(CategoryItemRecipeList l : uiManager.categoryItemList)
            try {
                if(l.hovering) {
                    i = 1;
                    recipeDisplay.renderRecipe(b, l, l.selectedSlot().item, (byte)l.selectedSlot);

                    if(l.canCreate[l.selectedSlot] == 1)
                        if(InputHandler.rightMouseButton) {
                        	timer++;
                        	if(timer >= craftTimeCap) {
                        		createItem(l.selectedSlot().item);
                        		timer = 0;
                        		craftTimeCap = (short)Math.max(craftTimeCap - 1, 4);
                        		break;
                        	}
                        } else craftTimeCap = 10;
                }
            } catch (NullPointerException ignored) {}
        if(i == 0) recipeDisplay.renderRecipe(b, null,null, (byte)0);

    }

    public void createItem(Item item) {
        Recipe recipe = item.getRecipe();
        for(Map.Entry<String, Integer> ingredient : recipe.getRecipe().entrySet()) {
            int amountLeft = ingredient.getValue();
            for(Slot s : inventory.slots) {
                if(s.item == Item.getItemFromString(ingredient.getKey())) {
                    if(s.count - amountLeft < 0) {
                        amountLeft -= s.count;
                        inventory.editSlot(s, null, 0);
                        continue;
                    }

                    if(s.count - amountLeft == 0) {
                        inventory.editSlot(s, null, 0);
                        break;
                    }

                    if(s.count - amountLeft > 0) {
                        inventory.editSlot(s, s.item, s.count - amountLeft);
                        break;
                    }
                }
            }
        }
        inventory.add(item);
    }
}
