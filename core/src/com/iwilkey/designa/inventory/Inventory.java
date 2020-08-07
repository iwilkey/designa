package com.iwilkey.designa.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.items.Item;

import java.awt.Rectangle;

public class Inventory {

    public final int MAX_STACK = 100;

    private GameBuffer gb;
    public static boolean active = false;
    private InventorySlot[][] slots;
    public final int invX, invY, invWidth, invHeight;
    public int[][] selector;

    public Inventory(GameBuffer gb) {
        this.gb = gb;

        invX = 10;
        invY = Gdx.graphics.getHeight() / 2 - (700 / 2);
        invWidth = 700; invHeight = 700;

        slots = new InventorySlot[invWidth / InventorySlot.SLOT_WIDTH][invHeight / InventorySlot.SLOT_HEIGHT];
        selector = new int[invWidth / InventorySlot.SLOT_WIDTH][invHeight / InventorySlot.SLOT_HEIGHT];
        selector[0][invWidth / InventorySlot.SLOT_WIDTH - 1] = 1;

        int slot = 0;
        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for(int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                slots[x][y] = new InventorySlot(this, slot);
                slot++;
            }
        }
    }

    public void tick() {
        if(InputHandler.inventoryRequest) active = !active;
        InputHandler.inventoryRequest = false;
        if(!active) return;

        for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
            for(int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                slots[x][y].tick();
                slots[x][y].isSelected = selector[x][y] == 1;
            }
        }

        // Input handling
        input();
    }

    private void input() {
        if(InputHandler.leftMouseButtonDown) {
            clearSelector();
            if(InputHandler.cursorX < invX + invWidth) {
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for(int y = 0; y < invHeight / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < invWidth / InventorySlot.SLOT_WIDTH; x++) {
                        if(slots[x][y].getCollider().intersects(rect)) {
                            selector[x][y] = 1;
                            break;
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
                        if (i.getID() == slots[x][y].getItem().getID()) { // And if the item we're adding has the same ID as the item in the slot...
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
    public void setGameBuffer(GameBuffer gb) { this.gb = gb; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

}
