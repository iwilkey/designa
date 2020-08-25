package com.iwilkey.designa.inventory.blueprints;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.blueprints.ItemBlueprint;

import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class BlueprintSection {

    protected com.iwilkey.designa.inventory.blueprints.Blueprints blueprints;
    protected String name;
    protected int tabX, tabY;
    protected final int w = 64, h = 64;
    protected boolean isSelected = false;
    protected Rectangle collider;

    protected ArrayList<ItemBlueprint> items;

    public BlueprintSection(String name, Blueprints workbench, int tabX, int tabY) {
        this.blueprints = workbench;
        this.name = name;
        this.tabX = tabX; this.tabY = tabY;
        collider = new Rectangle(tabX, tabY, w, h);

        items = new ArrayList<>();
    }

    public abstract void tick();
    public abstract void render(Batch b);

    private void clearSelection() {
        Assets.invClick.play(0.15f);
        for(com.iwilkey.designa.inventory.blueprints.ItemBlueprint ir : items) {
            ir.setSelected(false);
        }
    }

    private void mouseNotOver() {
        for (ItemBlueprint item : items) item.mouseOver = false;
    }

    public void input() {

        mouseNotOver();
        Rectangle r = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
        for (ItemBlueprint item : items) {
            if(item.collider.intersects(r)) {
                mouseNotOver();
                item.mouseOver = true;
                break;
            }
        }

        if (InputHandler.leftMouseButtonDown) {
            Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
            Rectangle cc = new Rectangle(Inventory.BLUEPRINT_X + 32 + 22, Inventory.BLUEPRINT_Y - 360 - 116, 82, 42);

            for (ItemBlueprint item : items) {
                if (rect.intersects(item.collider)) {
                    clearSelection();
                    item.setSelected(true);

                    break;
                }

                if (rect.intersects(cc) && item.canCreate && item.isSelected) {
                    item.create();
                    Assets.createItem[MathUtils.random(0, 2)].play(0.35f);
                    break;
                }
            }
        }
    }

    public void add(ItemBlueprint ir) {
        items.add(ir);
    }

}
