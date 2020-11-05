package dev.iwilkey.designa.item.creator;

import dev.iwilkey.designa.inventory.Slot;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.ui.ScrollableItemList;
import dev.iwilkey.designa.ui.UIText;

import java.awt.*;

public class CategoryItemRecipeList extends ScrollableItemList {

    Category category;

    public CategoryItemRecipeList(Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;

        for (int i = 0; i < category.getItems().size(); i++)
            add(category.getItems().get(i));

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

}
