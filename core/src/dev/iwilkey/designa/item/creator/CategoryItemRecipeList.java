package dev.iwilkey.designa.item.creator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.inventory.ComprehensiveInventory;
import dev.iwilkey.designa.inventory.Slot;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.Recipe;
import dev.iwilkey.designa.ui.ScrollableItemList;
import dev.iwilkey.designa.ui.UIObject;
import dev.iwilkey.designa.ui.UIText;

import java.awt.Rectangle;
import java.util.Map;

public class CategoryItemRecipeList extends ScrollableItemList {

    Category category;
    public byte[] canCreate;
    Thread createProcessor;

    public CategoryItemRecipeList(final ItemCreator itemCreator, Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;

        for (int i = 0; i < category.getItems().size(); i++)
            add(category.getItems().get(i));

        canCreate = new byte[category.getItems().size()];

        createProcessor = new Thread() {
            short itemCount = 0;
            byte check = 0, c = 0;
            public void run() {
                while(true) {
                    if(ItemCreator.isActive) {
                        try {
                            c = 0;
                            for(Slot s : slots) {
                                Recipe recipe = s.item.getRecipe();
                                check = 0;
                                for (Map.Entry<String, Integer> ingredient : recipe.getRecipe().entrySet()) {
                                    itemCount = 0;
                                    for (Slot ss : itemCreator.inventory.slots)
                                        if (ss.item == Item.getItemFromString(ingredient.getKey()))
                                            itemCount += ss.count;
                                    if (itemCount >= ingredient.getValue()) check++;
                                }
                                canCreate[c] = (check == recipe.getRecipe().size()) ? (byte) 1 : 0;
                                c++;
                            }
                        } catch (NullPointerException ignored) {}
                    }
                    try { sleep(100); } catch (InterruptedException ignored) {}
                }
            }
        };

        createProcessor.start();

    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void add(Item item) {

        Rectangle itemRect = new Rectangle(
                collider.x + (sizeOfList() * (SLOT_SIZE + SLOT_SPACE)),
                y, SLOT_SIZE, SLOT_SIZE
        );

        slots.add(new Slot(item, itemRect, false, new UIText("1",
                18, itemRect.x + (SLOT_SIZE - 10), itemRect.y)));

    }

    @Override
    public void render(Batch b) {
        s1 = 0;
        for(Slot slot : slots) {
            selected = s1 == selectedSlot;

            if(slot.collider.x - xSlotOffset <= relRect.x - SLOT_SIZE - SLOT_SPACE) {
                s1++;
                continue;
            }
            if(slot.collider.x - xSlotOffset >= relRect.x + relRect.width) {
                s1++;
                continue;
            }

            if(slot.collider.x - xSlotOffset < relRect.x) {
                eval = (slot.collider.x - xSlotOffset) - (relRect.x - SLOT_SIZE - SLOT_SPACE); // 68 start -> 0
                percent = (eval * UIObject.XSCALE) / 68;
                b.draw(slotTexture, relRect.x, slot.collider.y, eval * UIObject.XSCALE, eval * UIObject.YSCALE);
                if (slot.item != null && slot != ComprehensiveInventory.slotCurrentlyUp) {

                    if(canCreate[s1] == 1) b.setColor(new Color(1,1,1, 1));
                    else b.setColor(new Color(1,1,1,0.25f));
                    b.draw(slot.item.getTexture(), relRect.x + (((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) * percent),
                            slot.collider.y + (((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) * percent),
                            ((float)ITEM_TEXTURE_SIZE / SLOT_SIZE) * (eval * UIObject.XSCALE),
                            ((float)ITEM_TEXTURE_SIZE / SLOT_SIZE) * (eval * UIObject.YSCALE));
                    b.setColor(new Color(1,1,1, 1));

                    if(slot.isCountable && slot.count != 0) slot.display.render(b, (int)(relRect.x - ((SLOT_SIZE - 10) * (1 - percent)) + (SLOT_SIZE - 10)),
                            slot.display.y, (int)Math.max(slot.display.size * percent, 1));
                }
                if(selected) b.draw(selectTexture, relRect.x, slot.collider.y, eval * UIObject.XSCALE, eval * UIObject.YSCALE);
                s1++;
                continue;
            } else if (slot.collider.x - xSlotOffset > relRect.x + relRect.width - SLOT_SIZE - SLOT_SPACE) {
                eval = slot.collider.x - xSlotOffset - relRect.x - relRect.width;
                percent = Math.abs((eval * UIObject.XSCALE) / 68);
                b.draw(slotTexture, slot.collider.x - xSlotOffset, slot.collider.y, -eval * UIObject.XSCALE, -eval * UIObject.YSCALE);
                if (slot.item != null && slot != ComprehensiveInventory.slotCurrentlyUp) {

                    if(canCreate[s1] == 1) b.setColor(new Color(1,1,1, 1));
                    else b.setColor(new Color(1,1,1,0.25f));
                    b.draw(slot.item.getTexture(), slot.collider.x - xSlotOffset + (((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) * percent),
                            slot.collider.y + (((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) * percent),
                            ((float)ITEM_TEXTURE_SIZE / SLOT_SIZE) * -(eval * UIObject.XSCALE),
                            ((float)ITEM_TEXTURE_SIZE / SLOT_SIZE) * -(eval * UIObject.YSCALE));
                    b.setColor(new Color(1,1,1, 1));

                    if(slot.isCountable && slot.count != 0) slot.display.render(b, relRect.x + relRect.width - 10,
                            slot.display.y, (int)Math.max(slot.display.size * percent, 1));
                }
                if(selected) b.draw(selectTexture, slot.collider.x - xSlotOffset, slot.collider.y, -eval * UIObject.XSCALE, -eval * UIObject.YSCALE);
                s1++;
                continue;
            }

            b.draw(slotTexture, slot.collider.x - xSlotOffset, slot.collider.y, slot.collider.width, slot.collider.height);
            if (slot.item != null && slot != ComprehensiveInventory.slotCurrentlyUp) {

                if (canCreate[s1] == 1) b.setColor(new Color(1, 1, 1, 1));
                else b.setColor(new Color(1, 1, 1, 0.25f));
                b.draw(slot.item.getTexture(), slot.collider.x + ((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) - xSlotOffset,
                        slot.collider.y + ((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f), ITEM_TEXTURE_SIZE, ITEM_TEXTURE_SIZE);
                b.setColor(new Color(1, 1, 1, 1));

                if(slot.isCountable && slot.count != 0) slot.display.render(b, slot.display.x - xSlotOffset - 10, slot.display.y, slot.display.size);
            }
            if(selected) b.draw(selectTexture, slot.collider.x - xSlotOffset, slot.collider.y, slot.collider.width, slot.collider.height);

            s1++;
        }

    }

}
