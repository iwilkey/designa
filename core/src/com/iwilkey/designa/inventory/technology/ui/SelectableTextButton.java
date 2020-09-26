package com.iwilkey.designa.inventory.technology.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.technology.TechnologyRegion;

import java.awt.*;
import java.util.ArrayList;

public class SelectableTextButton extends SelectableItem {

    public String label;

    public SelectableTextButton(String label, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.label = label;
    }

    public void tick(ArrayList<SelectableTextButton> buttons) {
        input(buttons);
    }

    @Override
    public void onClick() {
        TechnologyRegion.setItemTypeLookat(label);
    }

    public void input(ArrayList<SelectableTextButton> buttons) {
        Rectangle mouse = new Rectangle();
        if(InputHandler.leftMouseButton) {
            mouse = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
        }
        if (mouse.intersects(collider) && InputHandler.leftMouseButtonDown) {
            for(SelectableTextButton sic : buttons) sic.isSelected = false;
            isSelected = true;
            onClick();
            Assets.invClick.play(0.35f);
        }
    }

    @Override
    public void render(Batch b) {
        Text.draw(b, label, collider.x, collider.y, collider.height);
        if(isSelected) b.draw(Assets.line, collider.x, collider.y - 4, collider.width, 2);
    }
}
