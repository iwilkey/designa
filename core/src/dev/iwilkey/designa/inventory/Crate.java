package dev.iwilkey.designa.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.ItemType;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.ui.ScrollableItemList;
import dev.iwilkey.designa.ui.UIText;

import java.awt.*;
import java.util.ArrayList;

public class Crate extends ComprehensiveInventory {

    private final Color[] guiColors;
    public static Rectangle crateArea;

    Player observer;
    ItemType.PlaceableTile.Crate crateType;
    public int tileX, tileY;
    public boolean open,
        itemUp;

    Slot selected;

    Crate(ItemType.PlaceableTile.Crate crateType, int tileX, int tileY) {

        super(null, null, ((Game.WINDOW_WIDTH / 2) - (((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * 5) / 2)),
                Game.WINDOW_HEIGHT - 800,
                (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * 5,
                (crateType.space / 5) * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE));

        guiColors = new Color[] {
                new Color(135 / 255f, 135 / 255f, 135 / 255f, 1.0f), // Stone
                new Color(181 / 255f, 134 / 255f, 45 / 255f, 1.0f), // Copper
                new Color(237 / 255f, 237 / 255f, 237 / 255f, 1.0f), // Silver
                new Color(235 / 255f, 133 / 255f, 0, 1.0f), // Iron
                new Color(0, 198 / 255f, 237 / 255f, 1.0f) // Diamond
        };

        TABLE_HEIGHT = (byte)(crateType.space / 5);
        System.out.println(TABLE_HEIGHT);

        collider = new Rectangle(((Game.WINDOW_WIDTH / 2) - (((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * 5) / 2)),
                Game.WINDOW_HEIGHT - 800,
                (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * 5,
                (TABLE_HEIGHT) * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE));
        relRect = collider;

        this.crateType = crateType;
        this.tileX = tileX; this.tileY = tileY;

        slots = new ArrayList<>();

        for(int i = 0; i < crateType.space; i++) {
            slots.add(new Slot(null, collider, true));
            slots.get(i).display = new UIText("", 22, 0, 0);
        }

        selected = slots.get(0);

        open = true; itemUp = false;

        add(Item.DIAMOND_CRATE, 2);
        add(Item.IRON_CRATE, 2);
        add(Item.CONDENSED_SLAB, 45);
        add(Item.DIAMOND_CRATE, 3);

        this.observer = null;

    }

    public void setObserver(Player player) {
        this.observer = player;
    }

    @Override
    public void tick() {
        super.tick();

        for(Slot s : slots)
            if(s.item != null) {
                s.display.message = "" + s.count;
            }

    }

    public void add(Item item, int amount) {
        for (Slot slot : slots) {
            if (slot.item == item && slot.count + 1 <= Inventory.STORAGE_CAP) {
                slot.count += amount;
                return;
            }
            if (slot.item == null) {
                slot.item = item;
                slot.count += amount;
                return;
            }
        }

    }

    // Returns index of slot that needs to be selected
    byte gxx, gyy, index;
    @Override
    protected Slot selectSlot(float gpx, float gpy) {

        gxx = (byte)(Math.abs(gpx) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * XSCALE));
        gyy = (byte)(TABLE_HEIGHT - (Math.abs(gpy) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE)));
        index = (byte)(((int)gyy * TABLE_WIDTH) + gxx);

        if (index < slots.size()) {
            selected = slots.get(index);
            return slots.get(index);
        }

        return null;

    }

    public Slot autoSelectSlot() {

        int gpx = (int)(collider.x - InputHandler.cursorX);
        int gpy = (int)(collider.y + (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE) - InputHandler.cursorY);

        gxx = (byte)(Math.abs(gpx) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * XSCALE));
        gyy = (byte)(TABLE_HEIGHT - (Math.abs(gpy) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE)));
        index = (byte)(((int)(gyy - 1) * TABLE_WIDTH) + gxx);
        if (index < slots.size()) {
            selected = slots.get(index);
            return slots.get(index);
        }

        return null;
    }

    public boolean editSlot(Slot s, Item i, int count) {
        for(Slot slot : slots) {
            if(slot == s) {
                slot.item = i;
                slot.count = (byte)count;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void input() {

        crateArea = new Rectangle(collider.x, (collider.y) + (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE),
                collider.width, collider.height);

        if(InputHandler.leftMouseButtonDown) {
            Rectangle c = new Rectangle((int) InputHandler.cursorX, (int) InputHandler.cursorY, 1, 1),
                    d = crateArea;
            if (c.intersects(d)) {

                if(itemUp) {

                    Slot beforeSelected = selected;
                    selectSlot(collider.x - InputHandler.cursorX, collider.y +
                            (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE) - InputHandler.cursorY);

                    // If the slot drop has no item
                    if(selected.item == null) {
                        editSlot(selected, beforeSelected.item, beforeSelected.count);
                        editSlot(beforeSelected, null, 0);
                        itemUp = false;
                        return;
                    }

                    itemUp = false;

                }

                selectSlot(collider.x - InputHandler.cursorX, collider.y +
                        (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE) - InputHandler.cursorY);

            } else if (c.intersects(ComprehensiveInventory.inventoryArea)) {
                if(itemUp) {
                    Slot beforeSelected = selected;
                    Slot selected = observer.inventory.compInv.autoSelect();

                    if (selected.item == null) {
                        observer.inventory.editSlot(selected, beforeSelected.item, beforeSelected.count);
                        editSlot(beforeSelected, null, 0);
                        itemUp = false;
                        return;
                    }

                    itemUp = false;

                }
            }
        } else if (InputHandler.rightMouseButtonDown) {
            Rectangle c = new Rectangle((int) InputHandler.cursorX, (int) InputHandler.cursorY, 1, 1),
                    d = new Rectangle(collider.x, (collider.y) + (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE),
                            collider.width, collider.height);
            if (c.intersects(d)) {
                if (!itemUp) {
                    itemUp = true;
                    selectSlot(collider.x - InputHandler.cursorX, collider.y +
                            (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE) - InputHandler.cursorY);
                    if(selected.item == null) itemUp = false;
                }
            }
        }
    }

    byte c = 0;
    Color crt;
    @Override
    public void render(Batch b) {

        c = 0;
        for(Slot s : slots) {

            gx = (byte) (c % TABLE_WIDTH);
            gy = (byte) (TABLE_HEIGHT - ((c - gx) / TABLE_WIDTH));
            pixX = (short) (relRect.x + (gx * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE)));
            pixY = (short) (relRect.y + (gy * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE)));

            crt = deferGUIColor();

            b.setColor(crt);
            b.draw(Assets.inventorySlot, pixX, pixY, ScrollableItemList.SLOT_SIZE, ScrollableItemList.SLOT_SIZE);
            b.setColor(Color.WHITE);

            if(s.item != null) {
                if(!itemUp) {
                    s.display.render(b, pixX + ScrollableItemList.SLOT_SIZE - 19, pixY + 1, 18);
                    b.draw(s.item.getTexture(), pixX + ((ScrollableItemList.SLOT_SIZE - ScrollableItemList.ITEM_TEXTURE_SIZE) / 2f),
                            pixY + ((ScrollableItemList.SLOT_SIZE - ScrollableItemList.ITEM_TEXTURE_SIZE) / 2f),
                            ScrollableItemList.ITEM_TEXTURE_SIZE, ScrollableItemList.ITEM_TEXTURE_SIZE);
                } else {
                    if(s != selected) {
                        s.display.render(b, pixX + ScrollableItemList.SLOT_SIZE - 19, pixY + 1, 18);
                        b.draw(s.item.getTexture(), pixX + ((ScrollableItemList.SLOT_SIZE - ScrollableItemList.ITEM_TEXTURE_SIZE) / 2f),
                                pixY + ((ScrollableItemList.SLOT_SIZE - ScrollableItemList.ITEM_TEXTURE_SIZE) / 2f),
                                ScrollableItemList.ITEM_TEXTURE_SIZE, ScrollableItemList.ITEM_TEXTURE_SIZE);
                    }
                }
            }

            if(selected == s) b.draw(Assets.inventorySelector, pixX, pixY, ScrollableItemList.SLOT_SIZE, ScrollableItemList.SLOT_SIZE);

            c++;
        }

        for(Slot s : slots) {
            if(selected == s && itemUp) {
                b.draw(s.item.getTexture(), (InputHandler.cursorX - (ScrollableItemList.ITEM_TEXTURE_SIZE / 2f)) / XSCALE,
                        (InputHandler.cursorY - (ScrollableItemList.ITEM_TEXTURE_SIZE / 2f)) / YSCALE,
                        ScrollableItemList.ITEM_TEXTURE_SIZE, ScrollableItemList.ITEM_TEXTURE_SIZE);
                s.display.render(b, (int)(InputHandler.cursorX / XSCALE) + (int)(ScrollableItemList.ITEM_TEXTURE_SIZE / 2f) - 1,
                        (int)(InputHandler.cursorY / YSCALE) - (int)(ScrollableItemList.ITEM_TEXTURE_SIZE / 2f) - 6, 18);
            }
        }



    }

    private Color deferGUIColor() {
        if (crateType.correspondingTile == Tile.STONE_CRATE) return guiColors[0];
        else if (crateType.correspondingTile == Tile.COPPER_CRATE) return guiColors[1];
        else if (crateType.correspondingTile == Tile.SILVER_CRATE) return guiColors[2];
        else if (crateType.correspondingTile == Tile.IRON_CRATE) return guiColors[3];
        else if (crateType.correspondingTile == Tile.DIAMOND_CRATE) return guiColors[4];
        return null;
    }

}
