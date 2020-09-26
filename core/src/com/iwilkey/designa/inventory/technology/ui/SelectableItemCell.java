package com.iwilkey.designa.inventory.technology.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.items.Item;

import java.awt.Rectangle;
import java.util.ArrayList;

public class SelectableItemCell extends SelectableItem {

    Item itemRep;
    public float offY = 0;

    public SelectableItemCell(Item item, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.itemRep = item;
    }

    public void tick(ArrayList<SelectableItemCell> cells, int y, int h) {
        input(cells, y, h);
    }

    @Override
    public void onClick() {

    }

    public void input(ArrayList<SelectableItemCell> cells, int y, int h) {
        Rectangle mouse = new Rectangle();
        if(collider.y + offY < y || collider.y + offY > y + h - 64) return;
        if(InputHandler.leftMouseButton) {
            mouse = new Rectangle(InputHandler.cursorX, InputHandler.cursorY - (int)offY, 1, 1);
        }

        if (mouse.intersects(collider) && InputHandler.leftMouseButtonDown) {
            for(SelectableItemCell sic : cells) sic.isSelected = false;
            isSelected = true;
            onClick();
            Assets.invClick.play(0.35f);
        }

    }

    @Override
    public void render(Batch b) {}

    public void render(Batch b, int x, int y, int h, int w) {
        if(collider.y + offY < y || collider.y + offY > y + h - 64) return;
        b.draw(Assets.inventorySlot, collider.x, collider.y + offY, collider.width, collider.height);
        b.draw(itemRep.getTexture(), collider.x + 12, collider.y + offY + 12, 24, 24);
        if(isSelected) {
            b.draw(Assets.inventorySelector, collider.x, collider.y + offY, collider.width, collider.height);
            Text.draw(b, itemRep.getName(), (x + w) - (w / 2) - ((itemRep.getName().length() * 12) / 2), y - 22, 12);

            String[] descriptionTokens = itemRep.getDescription().split("\n");
            for(int i = 0; i < descriptionTokens.length; i++) {
                Text.draw(b, descriptionTokens[i], (x + w) - (w / 2) - ((descriptionTokens[i].length() * 8) / 2) - 8, y - (10 * (i + 4)), 8);
            }

        }
    }
}
