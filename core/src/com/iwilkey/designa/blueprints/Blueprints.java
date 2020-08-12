package com.iwilkey.designa.blueprints;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.blueprints.sections.MachineSection;
import com.iwilkey.designa.blueprints.sections.ToolSection;
import com.iwilkey.designa.blueprints.sections.WeaponSection;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;

import java.awt.*;

public class Blueprints {

    private final GameBuffer gb;
    private final Inventory inventory;
    public static boolean active = false;
    public int sectionSelected;

    public BlueprintSection[] sections;

    public int x, y;
    public final int w = 300, h = 300;

    public Blueprints(GameBuffer gb, Inventory i, int x, int y) {
        this.gb = gb;
        this.inventory = i;
        this.x = x; this.y = y;

        sections = new BlueprintSection[3];
        sections[0] = new ToolSection( this, x, y);
        sections[1] = new WeaponSection(this, x + 64, y);
        sections[2] = new MachineSection(this, x + (64 * 2), y);

        sectionSelected = 0;
        updateSelector(sectionSelected);
    }

    public void tick() {
        active = inventory.isActive();

        for(BlueprintSection cs : sections) {
            cs.tick();
        }

        input();

    }

    private void input() {
        if (InputHandler.leftMouseButtonDown) {
            Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
            for(int i = 0; i < sections.length; i++) {
                if(rect.intersects(sections[i].collider)) updateSelector(i);
            }
        }
    }

    private void clearSelection() {
        for(BlueprintSection cs : sections) {
            cs.isSelected = false;
        }
    }

    private void updateSelector(int select) {
        clearSelection();
        switch(select) {
            case 0:
                sections[0].isSelected = true;
                break;
            case 1:
                sections[1].isSelected = true;
                break;
            case 2:
                sections[2].isSelected = true;
                break;
        }

        sectionSelected = select;
    }

    public void render(Batch b) {

        Text.draw(b, "Blueprints", x + 41, y + 78, 11);
        for(BlueprintSection cs : sections) {
            cs.render(b);

            if(cs.isSelected) b.draw(Assets.inventorySelector, cs.tabX, cs.tabY, 64, 64);
        }

    }

}