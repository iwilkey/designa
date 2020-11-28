package dev.iwilkey.designa.inventory;

import com.badlogic.gdx.graphics.g2d.Batch;

import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.creator.ItemCreator;
import dev.iwilkey.designa.ui.ScrollableItemList;
import dev.iwilkey.designa.ui.UIObject;
import dev.iwilkey.designa.ui.UIText;
import dev.iwilkey.designa.world.World;

public class Inventory extends ScrollableItemList {

    public static final byte STORAGE_CAP = 99;
    public UIText currentItemLabel;

    Player player;
    ComprehensiveInventory compInv;
    World world;

    public Inventory(World world, Player player) {
        super(Game.WINDOW_WIDTH - ((SLOT_SIZE + SLOT_SPACE) * 5) - 10,
                Game.WINDOW_HEIGHT - SLOT_SIZE - 10, (SLOT_SIZE + SLOT_SPACE) * 5, SLOT_SIZE);

        this.player = player;
        this.world = world;

        world.uiManager.addScrollableItemList(this);

        for(byte i = 0; i < 20; i++) super.add(null);

        this.compInv = new ComprehensiveInventory(slots,this, Game.WINDOW_WIDTH - ((SLOT_SIZE + SLOT_SPACE) * 5) - 10,
                0, (SLOT_SIZE + SLOT_SPACE) * 5, (SLOT_SIZE + SLOT_SPACE) * 4);

        currentItemLabel = new UIText("", 22, collider.x, collider.y + 64);

        add(Item.ROCK, 99);
    }

    @Override
    public void tick() {
        super.tick();
        for(Slot slot : slots)
            if (slot.count <= 0) slot.item = null;
        if(ItemCreator.isActive) compInv.tick();
    }

    byte s = 0;
    int center = 0;
    float distance = 0;
    @Override
    protected void selectSlot() {
        if(!ItemCreator.isActive) super.selectSlot();
        else {
            s = 0;
            for (Slot slot : slots) {

                if(s == selectedSlot) {
                    center = (int) (((collider.x + (collider.width / 2)) / UIObject.XSCALE));
                    distance = Math.abs((slot.collider.x - xSlotOffset) - (center - ((SLOT_SIZE / 2f))));
                    velocityX = (distance) / 15f;
                    if(slot.collider.x - xSlotOffset < center - ((SLOT_SIZE / 2))) {
                        xSlotOffset -= velocityX;
                        return;
                    } else if (slot.collider.x - xSlotOffset > center - ((SLOT_SIZE / 2))) {
                        xSlotOffset += velocityX;
                        return;
                    }
                }

                s++;
            }
        }
    }

    @Override
    public void add(Item item) {
        for(Slot slot : slots) {
            if(slot.item == item && slot.count + 1 <= STORAGE_CAP) {
                slot.count++;
                return;
            }
            if(slot.item == null) {
                slot.item = item;
                slot.count++;
                return;
            }
        }
    }

    public int amountOf(Item item) {
        int count = 0;
        for(Slot s : slots)
            if(s.item == item) count += s.count;
        return count;
    }

    public void add(Item item, int amount) {
        for (Slot slot : slots) {
            if (slot.item == item && slot.count + 1 <= STORAGE_CAP) {
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

    public void requestSlot(int slot) {
        selectedSlot = slot;
    }

    byte c = 0;
    public Slot getSlotFromIndex(int slot) {
        c = 0;
        for(Slot s : slots) {
            if(slot == c) return s;
            c++;
        }
        return null;
    }

    @Override
    public void render(Batch b) {
        super.render(b);
        if(ItemCreator.isActive) compInv.render(b);
        currentItemLabel.render(b, currentItemLabel.x, currentItemLabel.y, 22);
    }

    @Override
    public void onResize(int width, int height) {
        super.onResize(width, height);
        compInv.onResize(width, height);
    }

}
