package com.iwilkey.designa.inventory.blueprints.sections;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.inventory.blueprints.Blueprints;
import com.iwilkey.designa.inventory.blueprints.ItemBlueprint;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.inventory.blueprints.BlueprintSection;

public class UtilitiesSection extends BlueprintSection {
    public UtilitiesSection(Blueprints workbench, int x, int y) {
        super("Tools", workbench, x, y);
        items.add(new ItemBlueprint(this, Assets.plywoodTileItem, items.size() - 1));
        items.add(new ItemBlueprint(this, Assets.hardwoodTileItem, items.size() - 1));
        items.add(new ItemBlueprint(this, Assets.reinforcedHardwoodTileItem, items.size() - 1));
        items.add(new ItemBlueprint(this, Assets.strongwoodTileItem, items.size() - 1));
        items.add(new ItemBlueprint(this, Assets.reinforcedStrongwoodTileItem, items.size() - 1));
        items.add(new ItemBlueprint(this, Assets.torchItem, items.size() - 1));
        items.add(new ItemBlueprint(this, Assets.simpleDrillItem, items.size() - 1));
        items.add(new ItemBlueprint(this, Assets.crateItem, items.size() - 1));

        items.get(0).isSelected = true;
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
        b.draw(Assets.craftingTabs[0], tabX, tabY, w, h);

        if(isSelected) {
            Text.draw(b, "Utilities", tabX + 96 - 27 - 22, tabY - 24, 11);

            if (items.size() > 0) {
                for (ItemBlueprint ir : items) {

                    if(ir.canCreate) {
                        Blueprints.renderUnderneath.removeIf(i -> i == ir);
                        ir.renderRep(b);
                    }
                    else Blueprints.renderUnderneath.add(ir);

                    ir.render(b);

                }
            }
        }
    }
}
