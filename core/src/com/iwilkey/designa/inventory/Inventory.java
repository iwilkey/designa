package com.iwilkey.designa.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.blueprints.Blueprints;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.items.Item;

import java.awt.Rectangle;

public class Inventory {

    public final int MAX_STACK = 99;

    private GameBuffer gb;
    private Blueprints blueprints;
    public static boolean active = false;
    public InventorySlot[][] slots;
    public final int invX, invY, invWidth, invHeight;
    public int[][] selector;
    public boolean itemUp = false;

    private final int INV_SLOT_X = 120, INV_SLOT_Y = (Gdx.graphics.getHeight() / 2) - 250;
    private final static int bw = 300 + 64, bh = 300 + 200 + 150;
    public final static int BLUEPRINT_X = (Gdx.graphics.getWidth() / 2) + ((Gdx.graphics.getWidth() / 2) - (int)(bw / 0.85f)),
            BLUEPRINT_Y = (Gdx.graphics.getHeight() / 2) + ((Gdx.graphics.getHeight() / 2) - (int)(bh / 2.59f));
    public static final Rectangle BLUEPRINT_SIZE = new Rectangle(BLUEPRINT_X - 52 - 32, BLUEPRINT_Y - 370 - 140,
            bw, bh);

    public Inventory(GameBuffer gb) {
        this.gb = gb;

        invX = 10;
        invY = Gdx.graphics.getHeight() / 2 - (700 / 2);
        invWidth = 700; invHeight = 500;

        slots = new InventorySlot[invWidth / InventorySlot.SLOT_WIDTH][invHeight / InventorySlot.SLOT_HEIGHT];
        selector = new int[invWidth / InventorySlot.SLOT_WIDTH][invHeight / InventorySlot.SLOT_HEIGHT];
        selector[0][invHeight / InventorySlot.SLOT_HEIGHT - 1] = 1;

        blueprints = new Blueprints(gb, this, BLUEPRINT_X, BLUEPRINT_Y);

        int slot = 0;
        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for(int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                slots[x][y] = new InventorySlot(this, slot);
                slot++;
            }
        }

        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                slots[x][y].isSelected = selector[x][y] == 1;
            }
        }
    }

    public void tick() {

        if(InputHandler.inventoryRequest) {
            if(!active) Assets.openInv[MathUtils.random(0,2)].play(0.3f);
            else Assets.closeInv[MathUtils.random(0,2)].play(0.3f);

            active = !active;
        }
        InputHandler.inventoryRequest = false;
        if(!active) return;

        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for(int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                slots[x][y].tick();
                slots[x][y].isSelected = selector[x][y] == 1;
            }
        }

        blueprints.tick();

        // Input handling
        input();
    }

    private void input() {
        if(!itemUp) {
            if (InputHandler.leftMouseButtonDown) {
                clearSelector();
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for (int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                        if (slots[x][y].getCollider().intersects(rect)) {
                            selector[x][y] = 1;

                            Assets.invClick.play(0.3f);

                            break;
                        }
                    }
                }
            }

            if (InputHandler.itemPickupRequest) {
                clearSelector();
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for (int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                        if (slots[x][y].getCollider().intersects(rect)) {
                            if(slots[x][y].getItem() != null) {
                                slots[x][y].itemUp = true;
                                itemUp = true;
                                Assets.invClick.play(0.3f);
                            }
                            return;
                        }
                    }
                }

                InputHandler.itemPickupRequest = false;
            }

        } else {
            if(InputHandler.leftMouseButtonDown) {
                // Step I: Extract the previous inventory items.
                InventorySlot is = null;
                Item i = null;
                int count = 0;
                for (int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {

                        if (slots[x][y].getItem() != null) {
                            if (slots[x][y].itemUp) {
                                is = slots[x][y];
                                i = slots[x][y].getItem();
                                count = slots[x][y].itemCount;
                                slots[x][y].putItem(null, 0);
                                slots[x][y].itemUp = false;
                            }
                        }
                    }
                }

                // Step II: Clear the selector and find which slot was just clicked on.
                clearSelector();
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for (int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                        if (slots[x][y].getCollider().intersects(rect)) {

                            // Step III: If the slot has an item in it already and it's the same item...
                            if (slots[x][y].getItem() != null) {
                                if(slots[x][y].getItem() == i) {

                                    // If the new slots itemCount plus the previous item count is greater than
                                        // the max stack, we need to make the new slot 99 and the old slot count what wasn't used
                                        //  which is (count + slots[x][y].itemCount) - 99
                                    if (slots[x][y].itemCount + count > MAX_STACK && slots[x][y].itemCount != MAX_STACK) {

                                        int remain = (count + slots[x][y].itemCount) - 99;
                                        slots[x][y].itemCount = MAX_STACK;
                                        is.putItem(slots[x][y].getItem(), remain);
                                        itemUp = false;
                                        selector[x][y] = 1;
                                        return;

                                    // This is the same as above, but will swap them if the itemCount is already max stack.
                                    } else if (slots[x][y].itemCount + count > MAX_STACK && slots[x][y].itemCount == MAX_STACK) {
                                        is.putItem(slots[x][y].getItem(), slots[x][y].itemCount);
                                        slots[x][y].putItem(i, count);
                                        itemUp = false;
                                        selector[x][y] = 1;
                                        return;

                                    // If the slots itemCount plus the previous item count is less than or equal to the max stack,
                                        // then we need to make the new slot count + slots[x][y].itemCount and the
                                        // other slot needs to stay null, which it already is.
                                    } else if (count + slots[x][y].itemCount <= MAX_STACK) {
                                        slots[x][y].itemCount += count;
                                        itemUp = false;
                                        selector[x][y] = 1;
                                        return;
                                    }

                                // Otherwise, the new slot doesn't have the same item, but is not null. In this case, we need
                                    // to swap the two like regular.
                                } else {
                                    is.putItem(slots[x][y].getItem(), slots[x][y].itemCount);
                                    slots[x][y].putItem(i, count);
                                    itemUp = false;
                                    selector[x][y] = 1;
                                    return;
                                }

                            // This means the slot was null already, in that case, just place the item and count there.
                            } else {
                                slots[x][y].putItem(i, count);
                                itemUp = false;
                                selector[x][y] = 1;
                                return;
                            }
                        // It was outside a slot
                        } else {
                            is.putItem(i, count);
                            itemUp = false;
                            return;
                        }
                    }
                }

            } else {
                clearSelector();
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for (int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                        if (slots[x][y].getCollider().intersects(rect)) {
                            selector[x][y] = 1;
                        }
                    }
                }
            }
        }
    }

    public void render(Batch b) {
        if(!active) return;

        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for(int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                slots[x][y].render(b, (x * InventorySlot.SLOT_WIDTH) + INV_SLOT_X, (y * InventorySlot.SLOT_HEIGHT) + INV_SLOT_Y);
            }
        }

        blueprints.render(b);
    }

    // Inventory methods

    private void clearSelector() {
        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                selector[x][y] = 0;
            }
        }
    }

    public int addItem(Item i) {
        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            y = Math.abs((invHeight / InventorySlot.SLOT_HEIGHT) - y) - 1;
            for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                if (slots[x][y].getItem() == i) {
                    if (slots[x][y].itemCount + 1 <= MAX_STACK) {
                        slots[x][y].putItem(i, slots[x][y].itemCount + 1);
                        return 1;
                    }
                }
            }
        }

        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            y = Math.abs((invHeight / InventorySlot.SLOT_HEIGHT) - y) - 1;
            for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                if(slots[x][y].getItem() != null) { // If the slot isn't null...
                    if(slots[x][y].itemCount < MAX_STACK) {
                        if (i.getItemID() == slots[x][y].getItem().getItemID()) { // And if the item we're adding has the same ID as the item in the slot...
                            if (slots[x][y].itemCount + 1 <= MAX_STACK) {
                                slots[x][y].itemCount++;
                                return 1;
                            }
                        }
                    }
                } else {
                    slots[x][y].putItem(i, 1);
                    return 1;
                }
            }
        }

        return -1; // There's no more room
    }

    // Getters and setters

    public GameBuffer getBuffer() { return gb; }
    public InventorySlot[][] getSlots() { return slots; }
    public void setGameBuffer(GameBuffer gb) { this.gb = gb; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

}
