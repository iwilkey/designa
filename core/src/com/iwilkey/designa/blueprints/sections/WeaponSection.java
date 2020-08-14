package com.iwilkey.designa.blueprints.sections;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.blueprints.BlueprintSection;
import com.iwilkey.designa.blueprints.Blueprints;
import com.iwilkey.designa.blueprints.ItemBlueprint;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.items.Item;

public class WeaponSection extends BlueprintSection {

    public WeaponSection(Blueprints workbench, int x, int y) {
        super("Weapons", workbench, x, y);
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));
        items.add(new ItemBlueprint(this, Item.simpleDrill, items.size() - 1));

    }

    @Override
    public void tick() {
        if(items.size() > 0) {
            for(ItemBlueprint ir : items) {
                ir.tick();
            }
        }

        input();
    }

    @Override
    public void render(Batch b) {
        b.draw(Assets.craftingTabs[1], tabX, tabY, w, h);

        if(isSelected) {
            Text.draw(b, "Weapons", (tabX + 64) - (32) - 38, tabY - 24, 11);

            if (items.size() > 0) {
                for (ItemBlueprint ir : items) {
                    ir.render(b);
                }
            }
        }
    }
}
