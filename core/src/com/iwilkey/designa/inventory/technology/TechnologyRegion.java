package com.iwilkey.designa.inventory.technology;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.inventory.technology.subregion.ItemRepresentationGrid;
import com.iwilkey.designa.inventory.technology.subregion.SubRegion;
import com.iwilkey.designa.inventory.technology.subregion.TechTypeSelector;

import java.util.ArrayList;

public class TechnologyRegion {

    Texture bg = new Texture("textures/game/invbackground.png");
    public int x, y, w, h; // In pixels
    public ArrayList<SubRegion> subregions = new ArrayList<>();
    public static String itemTypeLookat = "Resources";

    public TechnologyRegion(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
        subregions.add(new TechTypeSelector(x - (w/2) + 170, y - 232, 30, h - 40));

        subregions.add(new ItemRepresentationGrid("Resources", Assets.resourceRepList,
                x - (w/2) + 170 + 30, y - 76, 560, 300));
        subregions.add(new ItemRepresentationGrid("Tools", Assets.toolRepList,
                x - (w/2) + 170 + 30, y - 76, 560, 300));
        subregions.add(new ItemRepresentationGrid("Utilities", Assets.utilitiesRepList,
                x - (w/2) + 170 + 30, y - 76, 560, 300));
        subregions.add(new ItemRepresentationGrid("Tiles", Assets.tilesRepList,
                x - (w/2) + 170 + 30, y - 76, 560, 300));
        subregions.add(new ItemRepresentationGrid("Defense", Assets.defenseRepList,
                x - (w/2) + 170 + 30, y - 76, 560, 300));
        subregions.add(new ItemRepresentationGrid("Machines", Assets.machinesRepList,
                x - (w/2) + 170 + 30, y - 76, 560, 300));
        subregions.add(new ItemRepresentationGrid("Misc.", Assets.miscRepList,
                x - (w/2) + 170 + 30, y - 76, 560, 300));
    }

    public static void setItemTypeLookat(String itemTypeLookat) {
        TechnologyRegion.itemTypeLookat = itemTypeLookat;
    }

    public void tick() {
        for(SubRegion sr : subregions) {
            sr.tick();
        }
    }

    public void render(Batch b) {
        b.draw(bg, x - (w / 2), y - (h / 2), w, h);
        for(SubRegion sr : subregions) sr.render(b);
    }

}
