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

    public Inventory(GameBuffer gb) {
        this.gb = gb;

        invX = 10;
        invY = Gdx.graphics.getHeight() / 2 - (700 / 2);
        invWidth = 700; invHeight = 500;

        slots = new InventorySlot[invWidth / InventorySlot.SLOT_WIDTH][invHeight / InventorySlot.SLOT_HEIGHT];
        selector = new int[invWidth / InventorySlot.SLOT_WIDTH][invHeight / InventorySlot.SLOT_HEIGHT];
        selector[0][invHeight / InventorySlot.SLOT_HEIGHT - 1] = 1;

        blueprints = new Blueprints(gb, this, invWidth + 192, 550);

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
                if (InputHandler.cursorX < invX + invWidth) {
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
            }

            if (InputHandler.itemPickupRequest) {
                clearSelector();
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for (int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                        if (slots[x][y].getCollider().intersects(rect)) {
                            slots[x][y].itemUp = true;
                            itemUp = true;
                            Assets.invClick.play(0.3f);
                            return;
                        }
                    }
                }

                InputHandler.itemPickupRequest = false;
            }

        } else {
            if(InputHandler.leftMouseButtonDown) {
                // System.out.println("Click");
                InventorySlot is = null;
                Item i = null;
                int count = 0;
                for (int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {

                        if (slots[x][y].getItem() != null) {
                            if (slots[x][y].itemUp) {
                                System.out.println("Found item up");
                                is = slots[x][y];
                                i = slots[x][y].getItem();
                                count = slots[x][y].itemCount;
                                slots[x][y].putItem(null, 0);
                                slots[x][y].itemUp = false;
                            }
                        }
                    }
                }

                clearSelector();
                if (InputHandler.cursorX < invX + invWidth) {
                    Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                    for (int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                        for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                            if (slots[x][y].getCollider().intersects(rect)) {
                                if (slots[x][y].getItem() != null) {
                                    if(slots[x][y].getItem() == i) {

                                        // TODO: Fix this.

                                        if (slots[x][y].itemCount + count >= MAX_STACK) {
                                            int remain = MAX_STACK - slots[x][y].itemCount;
                                            slots[x][y].itemCount = MAX_STACK;
                                            is.putItem(slots[x][y].getItem(), count - remain);
                                            itemUp = false;
                                            selector[x][y] = 1;
                                            return;
                                        } else if (count < MAX_STACK) {
                                            is.putItem(slots[x][y].getItem(), slots[x][y].itemCount);
                                            slots[x][y].putItem(i, count);
                                            itemUp = false;
                                            selector[x][y] = 1;
                                            return;
                                        }
                                    } else {
                                        is.putItem(slots[x][y].getItem(), slots[x][y].itemCount);
                                        slots[x][y].putItem(i, count);
                                        itemUp = false;
                                        selector[x][y] = 1;
                                        return;
                                    }
                                } else {
                                    slots[x][y].putItem(i, count);
                                    itemUp = false;
                                    selector[x][y] = 1;
                                    return;
                                }
                            }
                        }
                    }
                }

            } else {
                clearSelector();
                if (InputHandler.cursorX < invX + invWidth) {
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
    }

    public void render(Batch b) {
        if(!active) return;

        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for(int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                slots[x][y].render(b, x * InventorySlot.SLOT_WIDTH, y * InventorySlot.SLOT_HEIGHT);
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
            for(int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
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
