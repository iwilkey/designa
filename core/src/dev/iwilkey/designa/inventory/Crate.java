package dev.iwilkey.designa.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.ItemType;
import dev.iwilkey.designa.ui.ScrollableItemList;
import dev.iwilkey.designa.ui.UIText;

import java.awt.*;
import java.util.ArrayList;

public class Crate extends ComprehensiveInventory {

    ItemType.PlaceableTile.Crate crateType;
    public int tileX, tileY;
    public boolean open;

    Slot selected;

    Crate(ItemType.PlaceableTile.Crate crateType, int tileX, int tileY) {

        super(null, null, ((Game.WINDOW_WIDTH / 2) - (((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * 5) / 2)),
                Game.WINDOW_HEIGHT - 600,
                (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * 5,
                (crateType.space / 5) * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE));

        collider = new Rectangle(((Game.WINDOW_WIDTH / 2) - (((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * 5) / 2)),
                Game.WINDOW_HEIGHT - 600,
                (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * 5,
                (crateType.space / 5) * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE));
        relRect = collider;

        this.crateType = crateType;
        this.tileX = tileX; this.tileY = tileY;

        slots = new ArrayList<>();

        for(int i = 0; i < crateType.space; i++)
            slots.add(new Slot(null, collider, true));

        selected = slots.get(0);

        open = false;

        slots.get(0).item = Item.BLUESTONE;
        slots.get(0).count = 12;
        slots.get(0).display = new UIText("12", 22, 0, 0);

    }

    // Returns index of slot that needs to be selected
    byte gxx, gyy, index;
    @Override
    protected void selectSlot(float gpx, float gpy) {
        gxx = (byte)(Math.abs(gpx) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * XSCALE));
        gyy = (byte)(TABLE_HEIGHT - (Math.abs(gpy) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE)));
        index = (byte)(((int)gyy * TABLE_WIDTH) + gxx);

        if(index < slots.size()) selected = slots.get(index);

    }

    @Override
    protected void input() {
        if(InputHandler.leftMouseButtonDown) {
            Rectangle c = new Rectangle((int) InputHandler.cursorX, (int) InputHandler.cursorY, 1, 1),
                    d = new Rectangle(collider.x, (collider.y) + (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE),
                            collider.width, collider.height + 150);
            if (c.intersects(d)) {

                selectSlot(collider.x - InputHandler.cursorX, collider.y +
                        (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE) - InputHandler.cursorY);


            }
        }
    }

    byte c = 0;
    Color crt = new Color(0.35f, 0.88f, 0.12f, 1.0f);
    @Override
    public void render(Batch b) {

        c = 0;
        for(Slot s : slots) {

            gx = (byte) (c % TABLE_WIDTH);
            gy = (byte) (TABLE_HEIGHT - ((c - gx) / TABLE_WIDTH));
            pixX = (short) (relRect.x + (gx * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE)));
            pixY = (short) (relRect.y + (gy * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE)));

            b.setColor(crt);
            b.draw(Assets.inventorySlot, pixX, pixY, ScrollableItemList.SLOT_SIZE, ScrollableItemList.SLOT_SIZE);
            b.setColor(Color.WHITE);

            if(s.item != null && s != slotCurrentlyUp) {
                s.display.render(b, pixX + ScrollableItemList.SLOT_SIZE - 19, pixY + 1, 18);
                b.draw(s.item.getTexture(), pixX + ((ScrollableItemList.SLOT_SIZE - ScrollableItemList.ITEM_TEXTURE_SIZE) / 2f),
                        pixY + ((ScrollableItemList.SLOT_SIZE - ScrollableItemList.ITEM_TEXTURE_SIZE) / 2f),
                        ScrollableItemList.ITEM_TEXTURE_SIZE, ScrollableItemList.ITEM_TEXTURE_SIZE);
            }

            if(selected == s) b.draw(Assets.inventorySelector, pixX, pixY, ScrollableItemList.SLOT_SIZE, ScrollableItemList.SLOT_SIZE);

            c++;
        }



    }

}
