package dev.iwilkey.designa.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.audio.Audio;
import dev.iwilkey.designa.gfx.Camera;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.ItemType;
import dev.iwilkey.designa.item.Tool;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.ui.ScrollableItemList;
import dev.iwilkey.designa.ui.UIText;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;

// This will show the inventory in grid form and allow the player to move around things inside the inventory.
public class ComprehensiveInventory {

    public static Rectangle inventoryArea;

    public byte TABLE_WIDTH = 5, TABLE_HEIGHT = 4;
    private final short Y_OFF = 680;
    private UIText inventoryLabel, currentItemLabel;

    ArrayList<Slot> slots;
    Rectangle collider, relRect;
    Inventory inventory;
    boolean itemUp = false;
    public static Slot slotCurrentlyUp = null;

    ComprehensiveInventory(ArrayList<Slot> slots, Inventory inventory, int x, int y, int width, int height) {
        this.inventory = inventory;
        this.slots = slots;
        collider = new Rectangle(x, y + Y_OFF, width, height);
        relRect = new Rectangle(x, y + Y_OFF, width, height);
        
        inventoryLabel = new UIText("Inventory", 22, collider.x, collider.y + collider.height);
        currentItemLabel = new UIText("", 22, collider.x, collider.y + collider.height);
    }

    public void tick() {
        input();
    }

    protected void input() {

        if(InputHandler.leftMouseButtonDown) {

            inventoryArea = new Rectangle(collider.x, collider.y + (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE),
                    collider.width, collider.height);

            Rectangle c = new Rectangle((int) InputHandler.cursorX, (int) InputHandler.cursorY, 1, 1),
                    d = new Rectangle(collider.x, collider.y + (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE),
                            collider.width, collider.height);
            if (c.intersects(d)) {
                Audio.playSFX(Assets.invClick, 0.3f);
                selectSlot(collider.x - InputHandler.cursorX, collider.y +
                        (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE) - InputHandler.cursorY);
                if(itemUp) {
                
                    Slot slotToMove = inventory.getSlotFromIndex(index);
                   
                    if(slotCurrentlyUp == slotToMove) {
                        itemUp = false;
                        slotCurrentlyUp = null;
                        return;
                    }
                    
                    if(slotToMove.item == null) {
                    	
                    	if(slotCurrentlyUp.item.getType() instanceof ItemType.CreatableItem.Tool) {
                    		slotToMove.tool = slotCurrentlyUp.tool;
                    		slotCurrentlyUp.tool = null;
                    	}
                    	
                        inventory.editSlot(slotToMove, slotCurrentlyUp.item, slotCurrentlyUp.count);
                        inventory.editSlot(slotCurrentlyUp, null, 0);
                        itemUp = false;
                        slotCurrentlyUp = null;
                        return;
                    }
                    if(slotToMove.item != slotCurrentlyUp.item) {
                    	
                    	// If the two items are both tools...
                    	if(slotCurrentlyUp.item.getType() instanceof ItemType.CreatableItem.Tool
                    			&& slotToMove.item.getType() instanceof ItemType.CreatableItem.Tool) {
                    		Tool t = slotCurrentlyUp.tool;
                    		slotCurrentlyUp.tool = slotToMove.tool;
                    		slotToMove.tool = t;
                    	} else if(slotCurrentlyUp.item.getType() instanceof ItemType.CreatableItem.Tool) {
                    		slotToMove.tool = slotCurrentlyUp.tool;
                    		slotCurrentlyUp.tool = null;
                    	} else if(slotToMove.item.getType() instanceof ItemType.CreatableItem.Tool) {
                    		slotCurrentlyUp.tool = slotToMove.tool;
                    		slotToMove.tool = null;
                    	}
                    	
                        Item i = slotToMove.item;
                        byte count = slotToMove.count;
                        inventory.editSlot(slotToMove, slotCurrentlyUp.item, slotCurrentlyUp.count);
                        slotCurrentlyUp.item = i;
                        slotCurrentlyUp.count = count;
                    } else {
                    	
                    	if(slotCurrentlyUp.item.getType() instanceof ItemType.CreatableItem.Tool) {
                    		itemUp = false;
                            slotCurrentlyUp = null;
                    		return;
                    	}

                        short totalCountAfter = (short)(slotCurrentlyUp.count + slotToMove.count);
                        if(totalCountAfter > Inventory.STORAGE_CAP) {
                            inventory.editSlot(slotToMove, slotToMove.item, Inventory.STORAGE_CAP);
                            slotCurrentlyUp.count = (byte)(Inventory.STORAGE_CAP - totalCountAfter);
                        } else {
                            inventory.editSlot(slotToMove, slotToMove.item, totalCountAfter);
                            inventory.editSlot(slotCurrentlyUp, null, 0);
                            slotCurrentlyUp = null;
                            itemUp = false;
                        }
                    }
                }
            } else if (inventory.currentOpenCrate != null && c.intersects(Crate.crateArea)) {

                if(itemUp) {

                    Slot crateSlot = inventory.currentOpenCrate.autoSelectSlot();

                    if(crateSlot.item == null) {
                        inventory.currentOpenCrate.editSlot(crateSlot, slotCurrentlyUp.item, slotCurrentlyUp.count);
                        inventory.editSlot(slotCurrentlyUp, null, 0);
                        itemUp = false;
                        slotCurrentlyUp = null;
                        return;
                    }


                }


            } else {

                int xx = (int) (((((InputHandler.cursorX / XSCALE) - Camera.position.x) /
                        Tile.TILE_SIZE) / Camera.scale.x) * Tile.TILE_SIZE),
                        yy = (int) ((((InputHandler.cursorY / YSCALE) - Camera.position.y) /
                                Tile.TILE_SIZE) / Camera.scale.y) * Tile.TILE_SIZE;

                Audio.playSpacialSFX(Assets.invClick, new Point(xx, yy), inventory.player);

                if(slotCurrentlyUp == null) {
                    itemUp = false;
                    return;
                }
                
                if(slotCurrentlyUp.item.getType() instanceof ItemType.CreatableItem.Tool) {
                	inventory.player.world.activeItemHandler.spawnTool(slotCurrentlyUp.item,
                			slotCurrentlyUp.tool, xx + MathUtils.random(-2, 2), yy);
                } else {
                	for(int i = 0; i < slotCurrentlyUp.count; i++) {
                        inventory.player.world.activeItemHandler.spawn(slotCurrentlyUp.item,
                                xx + MathUtils.random(-2, 2), yy);
                    }
                }

                inventory.editSlot(slotCurrentlyUp, null, 0);
                slotCurrentlyUp = null;
                itemUp = false;

            }


        } else if (InputHandler.rightMouseButtonDown) {
            if(!itemUp) itemUp = true;
            Rectangle c = new Rectangle((int)InputHandler.cursorX, (int)InputHandler.cursorY, 1, 1),
                    d = new Rectangle(collider.x, collider.y + (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE),
                            collider.width, collider.height);
            if(c.intersects(d)) {
                Audio.playSFX(Assets.invClick, 0.3f);
                selectSlot(collider.x - InputHandler.cursorX, collider.y +
                        (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE) - InputHandler.cursorY);
                if(slotCurrentlyUp != null) {
                    Slot slotToDrop = inventory.getSlotFromIndex(index);
                    if(slotToDrop.item != slotCurrentlyUp.item && slotToDrop.item != null) return;
                    if (slotCurrentlyUp == slotToDrop) {
                        itemUp = false;
                        slotCurrentlyUp = null;
                        return;
                    }
                    short totalCountAfter = (short)(slotToDrop.count + 1);
                    boolean canDrop = slotCurrentlyUp.count - 1 > 0;
                    if (slotToDrop.item == null)
                        if (totalCountAfter <= Inventory.STORAGE_CAP)
                            inventory.editSlot(slotToDrop, slotCurrentlyUp.item, totalCountAfter);
                    if (slotCurrentlyUp.item == slotToDrop.item)
                        if (totalCountAfter <= Inventory.STORAGE_CAP)
                            inventory.editSlot(slotToDrop, slotToDrop.item, totalCountAfter);
                    slotCurrentlyUp.count--;
                    if (!canDrop) {
                        itemUp = false;
                        slotCurrentlyUp = null;
                        return;
                    }
                }

                if(slotCurrentlyUp == null) slotCurrentlyUp = (inventory.getSlotFromIndex(index).item != null) ? inventory.getSlotFromIndex(index) : null;
                if(slotCurrentlyUp == null) itemUp = false;

            } else {

                int xx = (int) (((((InputHandler.cursorX / XSCALE) - Camera.position.x) /
                        Tile.TILE_SIZE) / Camera.scale.x) * Tile.TILE_SIZE),
                yy = (int) ((((InputHandler.cursorY / YSCALE) - Camera.position.y) /
                        Tile.TILE_SIZE) / Camera.scale.y) * Tile.TILE_SIZE;

                Audio.playSpacialSFX(Assets.invClick, new Point(xx, yy), inventory.player);

                if(slotCurrentlyUp == null) {
                    itemUp = false;
                    return;
                }
                boolean canDrop = slotCurrentlyUp.count - 1 > 0;
                inventory.player.world.activeItemHandler.spawn(slotCurrentlyUp.item,
                        xx + MathUtils.random(-2, 2), yy);
                slotCurrentlyUp.count--;
                if (!canDrop) {
                    itemUp = false;
                    slotCurrentlyUp = null;
                }
            }
        }
    }

    // Returns index of slot that needs to be selected
    byte gxx, gyy, index;
    protected Slot selectSlot(float gpx, float gpy) {
        gxx = (byte)(Math.abs(gpx) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * XSCALE));
        gyy = (byte)(TABLE_HEIGHT - (Math.abs(gpy) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE)));
        index = (byte)(((int)gyy * TABLE_WIDTH) + gxx);
        return inventory.requestSlot(index);
    }

    public Slot autoSelect() {

        int gpx = collider.x - (int)InputHandler.cursorX;
        int gpy = (int)(collider.y + (int) ((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE) - InputHandler.cursorY);

        gxx = (byte)(Math.abs(gpx) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * XSCALE));
        gyy = (byte)(TABLE_HEIGHT - (Math.abs(gpy) / (int)((ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE) * YSCALE)));
        index = (byte)(((int)(gyy - 1) * TABLE_WIDTH) + gxx);
        return inventory.requestSlot(index);
    }

    byte c = 0, gx, gy;
    short pixX, pixY;
    Color inv = new Color(252 / 255f, 194 / 255f, 3 / 255f, 1.0f);
    public void render(Batch b) {
        c = 0;
        for(Slot s : slots) {

            gx = (byte)(c % TABLE_WIDTH);
            gy = (byte)(TABLE_HEIGHT - ((c - gx) / TABLE_WIDTH));
            pixX = (short)(relRect.x + (gx * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE)));
            pixY = (short)(relRect.y + (gy * (ScrollableItemList.SLOT_SIZE + ScrollableItemList.SLOT_SPACE)));
            
            b.setColor(inv);
            b.draw(Assets.inventorySlot, pixX, pixY, ScrollableItemList.SLOT_SIZE, ScrollableItemList.SLOT_SIZE);
            b.setColor(Color.WHITE);

            if(inventory.selectedSlot() == s) b.draw(Assets.inventorySelector, pixX, pixY, ScrollableItemList.SLOT_SIZE, ScrollableItemList.SLOT_SIZE);

            if(s.item != null && s != slotCurrentlyUp) {
                s.display.render(b, pixX + ScrollableItemList.SLOT_SIZE - 19, pixY + 1, 18);
                b.draw(s.item.getTexture(), pixX + ((ScrollableItemList.SLOT_SIZE - ScrollableItemList.ITEM_TEXTURE_SIZE) / 2f),
                        pixY + ((ScrollableItemList.SLOT_SIZE - ScrollableItemList.ITEM_TEXTURE_SIZE) / 2f),
                        ScrollableItemList.ITEM_TEXTURE_SIZE, ScrollableItemList.ITEM_TEXTURE_SIZE);
            }

            c++;
        }

        if(itemUp && slotCurrentlyUp != null) {
            slotCurrentlyUp.display.render(b, (int)(InputHandler.cursorX / XSCALE) + (int)(ScrollableItemList.ITEM_TEXTURE_SIZE / 2f) + 4,
                    (int)(InputHandler.cursorY / YSCALE) - (int)(ScrollableItemList.ITEM_TEXTURE_SIZE / 2f) - 4, 18);
            try {
	            b.draw(slotCurrentlyUp.item.getTexture(), (InputHandler.cursorX / XSCALE) - (ScrollableItemList.ITEM_TEXTURE_SIZE / 2f),
	                    (InputHandler.cursorY / YSCALE) - (ScrollableItemList.ITEM_TEXTURE_SIZE / 2f),
	                    ScrollableItemList.ITEM_TEXTURE_SIZE, ScrollableItemList.ITEM_TEXTURE_SIZE);
            } catch (NullPointerException ignored) {}
        }
        
        inventoryLabel.render(b, (int)(((collider.x + (collider.width / 2)) / XSCALE) - 72), 
        		(int)((collider.y + collider.height) / YSCALE) + 135, 22);
        
        if(inventory.selectedSlot().item != null)
        	currentItemLabel.message = inventory.selectedSlot().item.getName();
        else currentItemLabel.message = "Empty Slot";
        
        currentItemLabel.render(b, (int)((collider.x + (collider.width / 2)) / XSCALE) - (currentItemLabel.message.length() * 8), 
        		(int)((collider.y + collider.height) / YSCALE) + 100, 22);
        
        

    }

    float XSCALE = 1.0f, YSCALE = 1.0f;
    public void onResize(int width, int height) {
        XSCALE = (float)width / Camera.GW;
        YSCALE = (float)height / Camera.GH;
        collider.height = (int)(relRect.height * YSCALE);
        collider.width = (int)(relRect.width * XSCALE);
        collider.x = (int)(relRect.x * XSCALE);
        collider.y = (int)(relRect.y * YSCALE);
    }

}
