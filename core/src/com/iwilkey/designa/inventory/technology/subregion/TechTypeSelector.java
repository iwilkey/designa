package com.iwilkey.designa.inventory.technology.subregion;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.inventory.technology.ui.SelectableItem;
import com.iwilkey.designa.inventory.technology.ui.SelectableTextButton;

import java.util.ArrayList;

public class TechTypeSelector extends SubRegion {

    ArrayList<SelectableTextButton> buttons = new ArrayList<>();

    public TechTypeSelector(int x, int y, int w, int h) {
        super("tech-type-selector", x, y, w, h);
        int space = 55;
        int startingY = h + 300;
        buttons.add(new SelectableTextButton("Resources", x - 114,  startingY, "Resources".length() * 14, 14));
        buttons.add(new SelectableTextButton("Tools", x - 114,  startingY - (space), "Tools".length() * 14, 14));
        buttons.add(new SelectableTextButton("Utilities", x - 114,  startingY - (space * 2), "Utilities".length() * 14, 14));
        buttons.add(new SelectableTextButton("Tiles", x - 114,  startingY - (space * 3), "Tiles".length() * 14, 14));
        buttons.add(new SelectableTextButton("Defense", x - 114,  startingY - (space * 4), "Defense".length() * 14, 14));
        buttons.add(new SelectableTextButton("Machines", x - 114,  startingY - (space * 5), "Machines".length() * 14, 14));
        buttons.add(new SelectableTextButton("Misc.", x - 114,  startingY - (space * 6), "Misc.".length() * 14, 14));
        buttons.get(0).isSelected = true;
    }

    @Override
    public void tick() {
        for(SelectableTextButton tb : buttons) {
            tb.tick(buttons);
        }
    }

    @Override
    public void render(Batch b) {
        renderBorder(b);
        for(SelectableItem si : buttons) si.render(b);
    }

    @Override
    public void renderBorder(Batch b) {
        b.draw(Assets.line, x + w, y, 2, h);
    }
}
