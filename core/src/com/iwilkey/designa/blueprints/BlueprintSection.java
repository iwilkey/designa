package com.iwilkey.designa.blueprints;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.input.InputHandler;

import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class BlueprintSection {

    protected Blueprints blueprints;
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
        for(ItemBlueprint ir : items) {
            ir.setSelected(false);
        }
    }

    public void input() {
        if (InputHandler.leftMouseButtonDown) {
            Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
            Rectangle cc = new Rectangle(892 + 32 + 22, 84, 82, 42);
            for (int i = 0; i < items.size(); i++) {
                if(rect.intersects(items.get(i).collider) &&
                        !(rect.intersects(cc))) {
                    clearSelection();
                    items.get(i).setSelected(true);
                    break;
                }

                if(rect.intersects(cc) && items.get(i).canCreate) {
                    items.get(i).create();
                    Assets.createItem[MathUtils.random(0,2)].play(0.35f);
                }
            }
        }
    }

    public void add(ItemBlueprint ir) {
        items.add(ir);
    }

}
