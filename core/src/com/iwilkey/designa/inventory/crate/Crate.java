package com.iwilkey.designa.inventory.crate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.InventorySlot;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

import java.awt.Rectangle;
import java.util.HashMap;

public class Crate {

    Texture bg = new Texture("textures/game/invbackground.png");

    private GameBuffer gb;
    private final Inventory playerInventory;
    public InventorySlot[][] storage;
    public int x, y;
    public static final int w = 400, h = 400, MAX_STACK = 99;
    public boolean isActive = true, itemUp = false;

    public int[][] selector;

    public Crate(Inventory inv, int x, int y) {
        this.playerInventory = inv;
        gb = this.playerInventory.getBuffer();
        this.x = x; this.y = y;

        selector = new int[w / InventorySlot.SLOT_WIDTH][h / InventorySlot.SLOT_HEIGHT];
        selector[0][h / InventorySlot.SLOT_HEIGHT - 1] = 1;

        storage = new InventorySlot[w / InventorySlot.SLOT_WIDTH][h / InventorySlot.SLOT_HEIGHT];
        int slot = 0;
        for(int yy = 0; yy < h / InventorySlot.SLOT_HEIGHT; yy++) {
            for(int xx = 0; xx < w / InventorySlot.SLOT_WIDTH; xx++) {
                storage[xx][yy] = new InventorySlot(inv, slot);
                slot++;
            }
        }

    }

    public void tick() {
        if(isActive) {
            for(int y = 0; y < w / InventorySlot.SLOT_HEIGHT; y++) {
                for(int x = 0; x < h / InventorySlot.SLOT_WIDTH; x++) {
                    storage[x][y].tick();
                    storage[x][y].isSelected = selector[x][y] == 1;
                }
            }
            crateInput();
        }
    }

    private void mouseNotOver() {
        for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
            for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                storage[x][y].mouseOver = false;
            }
        }
    }

    public void clearSelector() {
        for(int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
            for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                selector[x][y] = 0;
            }
        }
    }

    private void crateInput() {

        if(InputHandler.exitCrateRequest) {
            isActive = false;
            InputHandler.exitCrateRequest = false;
            return;
        }

        if(!itemUp) {
            mouseNotOver();
            Rectangle r = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
            for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
                for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                    if (storage[x][y].getCollider().intersects(r)) {
                        mouseNotOver();
                        storage[x][y].mouseOver = true;
                        break;
                    }
                }
            }

            if (InputHandler.leftMouseButtonDown) {

                clearSelector();
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                        if (storage[x][y].getCollider().intersects(rect)) {
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
                for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                        if (storage[x][y].getCollider().intersects(rect)) {
                            if(storage[x][y].getItem() != null) {
                                storage[x][y].itemUp = true;
                                itemUp = true;
                                Assets.invClick.play(0.3f);
                            }
                            return;
                        }
                    }
                }
            }

        } else {
            if(InputHandler.leftMouseButtonDown) {

                for (int y = 0; y < 500 / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < 700 / InventorySlot.SLOT_WIDTH; x++) {
                        try {
                            if (playerInventory.getSlots()[x][y].mouseOver) {
                                if (transferToInventory()) return;
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) {}
                    }
                }

                // Step I: Extract the previous inventory items.
                InventorySlot is = null;
                Item i = null;
                int count = 0, tempX = 0, tempY = 0;
                for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {

                        if (storage[x][y].getItem() != null) {
                            if (storage[x][y].itemUp) {
                                is = storage[x][y];
                                i = storage[x][y].getItem();
                                count = storage[x][y].itemCount;
                                tempX = x; tempY = y;
                                storage[x][y].putItem(null, 0);
                                storage[x][y].itemUp = false;
                            }
                        }
                    }
                }

                // Step II: Clear the selector and find which slot was just clicked on.
                clearSelector();
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                        if (storage[x][y].getCollider().intersects(rect)) {

                            // Step III: If the slot has an item in it already and it's the same item...
                            if (storage[x][y].getItem() != null) {
                                if (storage[x][y].getItem() == i) {

                                    // If the new slots itemCount plus the previous item count is greater than
                                    // the max stack, we need to make the new slot 99 and the old slot count what wasn't used
                                    //  which is (count + slots[x][y].itemCount) - 99
                                    if (storage[x][y].itemCount + count > MAX_STACK && storage[x][y].itemCount != MAX_STACK) {

                                        int remain = (count + storage[x][y].itemCount) - MAX_STACK;
                                        storage[x][y].itemCount = MAX_STACK;
                                        is.putItem(storage[x][y].getItem(), remain);
                                        itemUp = false;
                                        selector[x][y] = 1;
                                        return;

                                        // This is the same as above, but will swap them if the itemCount is already max stack.
                                    } else if (storage[x][y].itemCount + count > MAX_STACK && storage[x][y].itemCount == MAX_STACK) {
                                        is.putItem(storage[x][y].getItem(), storage[x][y].itemCount);
                                        storage[x][y].putItem(i, count);
                                        itemUp = false;
                                        selector[x][y] = 1;
                                        return;

                                        // If the slots itemCount plus the previous item count is less than or equal to the max stack,
                                        // then we need to make the new slot count + slots[x][y].itemCount and the
                                        // other slot needs to stay null, which it already is.
                                    } else if (count + storage[x][y].itemCount <= MAX_STACK) {
                                        storage[x][y].itemCount += count;
                                        itemUp = false;
                                        selector[x][y] = 1;
                                        return;
                                    }

                                    // Otherwise, the new slot doesn't have the same item, but is not null. In this case, we need
                                    // to swap the two like regular.
                                } else {
                                    try {
                                        is.putItem(storage[x][y].getItem(), storage[x][y].itemCount);
                                        storage[x][y].putItem(i, count);
                                        itemUp = false;
                                        selector[x][y] = 1;
                                    } catch (NullPointerException ignored) {
                                    }
                                    return;
                                }

                                // This means the slot was null already, in that case, just place the item and count there.
                            } else {
                                storage[x][y].putItem(i, count);
                                itemUp = false;
                                selector[x][y] = 1;
                                return;
                            }
                        }
                    }
                }

                for(int ii = 0; ii < count; ii++) {
                    int abb = MathUtils.random(-2,2);

                    int xx = (World.getEntityHandler().getPlayer().facingLeft()) ?
                            (int) World.getEntityHandler().getPlayer().getX() - (Tile.TILE_SIZE - 6) + abb :
                            (int) World.getEntityHandler().getPlayer().getX() + (2 * Tile.TILE_SIZE - 6) + abb;
                    int yyAbb = MathUtils.random(6, 14);

                    World.getItemHandler().addItem(i.createNew(
                            xx, (int) World.getEntityHandler().getPlayer().getY() + (Tile.TILE_SIZE) + yyAbb));
                }

                if(is != null) is.putItem(null, 0);
                itemUp = false;
                selector[tempX][tempY] = 1;

            } else if (InputHandler.rightMouseButtonDown) {
                Item i = null;
                InventorySlot is = null;
                for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {

                        if (storage[x][y].getItem() != null) {
                            if (storage[x][y].itemUp) {
                                i = storage[x][y].getItem();
                                is = storage[x][y];
                                selector[x][y] = 1;
                                if (is.itemCount < 2) {
                                    is.itemUp = false;
                                    itemUp = false;
                                    return;
                                }
                            }
                        }
                    }
                }

                clearSelector();
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                        if (storage[x][y].getCollider().intersects(rect)) {
                            if (storage[x][y].getItem() == null) {
                                storage[x][y].putItem(i, 1);
                                is.itemCount--;
                            } else if (storage[x][y].getItem() == i) {
                                if (storage[x][y].itemCount + 1 > MAX_STACK) return;
                                else {
                                    storage[x][y].itemCount++;
                                    is.itemCount--;
                                }
                            } else return;
                            return;
                        }
                    }
                }
            } else {
                clearSelector();
                Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
                for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
                    for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                        if (storage[x][y].getCollider().intersects(rect)) {
                            selector[x][y] = 1;
                        }
                    }
                }
            }
        }
    }

    private boolean transferToInventory() {

        // Step I: Extract the previous inventory items.
        InventorySlot is = null;
        Item i = null;
        int count = 0;
        for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
            for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {

                if (storage[x][y].getItem() != null) {
                    if (storage[x][y].itemUp) {
                        is = storage[x][y];
                        i = storage[x][y].getItem();
                        count = storage[x][y].itemCount;
                        storage[x][y].putItem(null, 0);
                        storage[x][y].itemUp = false;
                    }
                }
            }
        }

        playerInventory.clearSelector();
        Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
        for (int y = 0; y < 200 / InventorySlot.SLOT_HEIGHT; y++) {
            for (int x = 0; x < 400 / InventorySlot.SLOT_WIDTH; x++) {
                if (playerInventory.getSlots()[x][y].getCollider().intersects(rect)) {

                    // Step III: If the slot has an item in it already and it's the same item...
                    if (playerInventory.getSlots()[x][y].getItem() != null) {
                        if(playerInventory.getSlots()[x][y].getItem() == i) {

                            // If the new slots itemCount plus the previous item count is greater than
                            // the max stack, we need to make the new slot 99 and the old slot count what wasn't used
                            //  which is (count + slots[x][y].itemCount) - 99
                            if (playerInventory.getSlots()[x][y].itemCount + count > MAX_STACK && playerInventory.getSlots()[x][y].itemCount != MAX_STACK) {

                                int remain = (count + playerInventory.getSlots()[x][y].itemCount) - MAX_STACK;
                                playerInventory.getSlots()[x][y].itemCount = MAX_STACK;
                                is.putItem(playerInventory.getSlots()[x][y].getItem(), remain);
                                itemUp = false;
                                playerInventory.selector[x][y] = 1;
                                return true;

                                // This is the same as above, but will swap them if the itemCount is already max stack.
                            } else if (playerInventory.getSlots()[x][y].itemCount + count > MAX_STACK && playerInventory.getSlots()[x][y].itemCount == MAX_STACK) {
                                is.putItem(playerInventory.getSlots()[x][y].getItem(), playerInventory.getSlots()[x][y].itemCount);
                                playerInventory.getSlots()[x][y].putItem(i, count);
                                itemUp = false;
                                playerInventory.selector[x][y] = 1;
                                return true;

                                // If the slots itemCount plus the previous item count is less than or equal to the max stack,
                                // then we need to make the new slot count + slots[x][y].itemCount and the
                                // other slot needs to stay null, which it already is.
                            } else if (count + playerInventory.getSlots()[x][y].itemCount <= MAX_STACK) {
                                playerInventory.getSlots()[x][y].itemCount += count;
                                itemUp = false;
                                playerInventory.selector[x][y] = 1;
                                return true;
                            }

                            // Otherwise, the new slot doesn't have the same item, but is not null. In this case, we need
                            // to swap the two like regular.
                        } else {
                            try {
                                is.putItem(playerInventory.getSlots()[x][y].getItem(), playerInventory.getSlots()[x][y].itemCount);
                                playerInventory.getSlots()[x][y].putItem(i, count);
                                itemUp = false;
                                playerInventory.selector[x][y] = 1;
                            } catch (NullPointerException ignored) {}
                            return true;
                        }

                        // This means the slot was null already, in that case, just place the item and count there.
                    } else {
                        playerInventory.getSlots()[x][y].putItem(i, count);
                        itemUp = false;
                        playerInventory.selector[x][y] = 1;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public int addItem(Item i) {
        for(int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
            for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                if (storage[x][y].getItem() == i) {
                    if (storage[x][y].itemCount + 1 <= MAX_STACK) {
                        storage[x][y].putItem(i, storage[x][y].itemCount + 1);
                        return 1;
                    }
                }
            }
        }

        for(int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
            // y = Math.abs((invHeight / InventorySlot.SLOT_HEIGHT) - y);
            for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                if(storage[x][y].getItem() != null) { // If the slot isn't null...
                    if(storage[x][y].itemCount < MAX_STACK) {
                        if (i.getItemID() == storage[x][y].getItem().getItemID()) { // And if the item we're adding has the same ID as the item in the slot...
                            if (storage[x][y].itemCount + 1 <= MAX_STACK) {
                                storage[x][y].itemCount++;
                                return 1;
                            }
                        }
                    }
                } else {
                    storage[x][y].putItem(i, 1);
                    return 1;
                }
            }
        }

        return -1; // There's no more room
    }

    public HashMap<Item, Integer> destroy() {
        HashMap<Item, Integer> items = new HashMap<>();
        for(int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
            for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                if(storage[x][y] != null) {
                    if(!(items.containsKey(storage[x][y].getItem()))) {
                        items.put(storage[x][y].getItem(), storage[x][y].itemCount);
                    } else {
                        items.put(storage[x][y].getItem(),
                                items.get(storage[x][y].getItem()) + storage[x][y].itemCount);
                    }
                }
            }
        }
        return items;
    }

    public void render(Batch b) {
        if (isActive) {
            b.draw(bg, (Gdx.graphics.getWidth() / 2) - 228, Game.h - 300 - 20, 480, 275);
            Text.draw(b, "Inventory", (Gdx.graphics.getWidth() / 2) - 40, (Game.h - 300) + 220, 11);
            playerInventory.renderPlayerInventory(b, 0);
            b.draw(bg, Inventory.CRATE_X - 30, Inventory.CRATE_Y - 20, w + 90, h + 82);
            Text.draw(b, "Crate", Inventory.CRATE_X + (4 * InventorySlot.SLOT_WIDTH) - 18,
                    Inventory.CRATE_Y + (8 * InventorySlot.SLOT_HEIGHT) + 24, 11);
            for (int y = 0; y < h / InventorySlot.SLOT_HEIGHT; y++) {
                for (int x = 0; x < w / InventorySlot.SLOT_WIDTH; x++) {
                    storage[x][Math.abs((h / InventorySlot.SLOT_HEIGHT) - y) - 1].render(b, (x * InventorySlot.SLOT_WIDTH) + Inventory.CRATE_X,
                            (y * InventorySlot.SLOT_HEIGHT) + Inventory.CRATE_Y);
                }
            }
        }
    }

    public void setActive(boolean active) { isActive = active; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
}
