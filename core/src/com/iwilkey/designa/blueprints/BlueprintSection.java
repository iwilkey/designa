package com.iwilkey.designa.blueprints;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.input.InputHandler;

import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class BlueprintSection {

    protected Blueprints workbench;
    protected String name;
    protected int tabX, tabY;
    protected final int w = 64, h = 64;
    protected boolean isSelected = false;
    protected Rectangle collider;

    protected ArrayList<ItemRepresentation> items;

    public BlueprintSection(String name, Blueprints workbench, int tabX, int tabY) {
        this.workbench = workbench;
        this.name = name;
        this.tabX = tabX; this.tabY = tabY;
        collider = new Rectangle(tabX, tabY, w, h);

        items = new ArrayList<>();
    }

    public abstract void tick();
    public abstract void render(Batch b);

    private void clearSelection() {
        for(ItemRepresentation ir : items) {
            ir.setSelected(false);
        }
    }

    public void input() {
        if (InputHandler.leftMouseButtonDown) {
            clearSelection();
            Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
            for (int i = 0; i < items.size(); i++) {
                if(rect.intersects(items.get(i).collider)) items.get(i).setSelected(true);
            }
        }
    }

    public void add(ItemRepresentation ir) {
        items.add(ir);
    }

}
