package com.iwilkey.designa.inventory.blueprints.sections;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.inventory.blueprints.BlueprintSection;
import com.iwilkey.designa.inventory.blueprints.Blueprints;
import com.iwilkey.designa.inventory.blueprints.ItemBlueprint;
import com.iwilkey.designa.gfx.Text;

public class WeaponSection extends BlueprintSection {

    public WeaponSection(Blueprints workbench, int x, int y) {
        super("Weapons", workbench, x, y);
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
