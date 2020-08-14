package com.iwilkey.designa.blueprints.sections;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.blueprints.BlueprintSection;
import com.iwilkey.designa.blueprints.Blueprints;
import com.iwilkey.designa.blueprints.ItemRepresentation;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.items.Item;

public class ToolSection extends BlueprintSection {
    public ToolSection(Blueprints workbench, int x, int y) {
        super("Tools", workbench, x, y);
        items.add(new ItemRepresentation(this, Item.torchItem, items.size() - 1));
        items.add(new ItemRepresentation(this, Item.simpleDrill, items.size() - 1));

    }

    @Override
    public void tick() {
        if(items.size() > 0) {
            for(ItemRepresentation ir : items) {
                ir.tick();
            }
        }

        input();
    }

    @Override
    public void render(Batch b) {
        b.draw(Assets.craftingTabs[0], tabX, tabY, w, h);

        if(isSelected) {
            Text.draw(b, "Tools", tabX + 96 - 27, tabY - 24, 11);

            if (items.size() > 0) {
                for (ItemRepresentation ir : items) {
                    ir.render(b);
                }
            }
        }
    }
}
