package dev.iwilkey.designa.inventory;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;

import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.ItemType;
import dev.iwilkey.designa.item.Tool;
import dev.iwilkey.designa.item.creator.ItemCreator;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.ui.ScrollableItemList;
import dev.iwilkey.designa.ui.UIObject;
import dev.iwilkey.designa.ui.UIText;
import dev.iwilkey.designa.world.World;

public class Inventory extends ScrollableItemList {

    public static final byte STORAGE_CAP = 99;

    Player player;
    public ArrayList<Tool> tools;
    public ArrayList<Crate> crates;
    ComprehensiveInventory compInv;
    World world;

    public Inventory(World world, Player player) {
        super(Game.WINDOW_WIDTH - ((SLOT_SIZE + SLOT_SPACE) * 5) - 10,
                Game.WINDOW_HEIGHT - SLOT_SIZE - 10, (SLOT_SIZE + SLOT_SPACE) * 5, SLOT_SIZE);

        this.player = player;
        this.world = world;

        tools = new ArrayList<>();
        crates = new ArrayList<>();

        world.uiManager.addScrollableItemList(this);

        for (byte i = 0; i < 20; i++) super.add(null);

        this.compInv = new ComprehensiveInventory(slots, this, Game.WINDOW_WIDTH - ((SLOT_SIZE + SLOT_SPACE) * 5) - 10,
                -200, (SLOT_SIZE + SLOT_SPACE) * 5, (SLOT_SIZE + SLOT_SPACE) * 4);

        this.add(Item.STONE_CRATE);
        this.add(Item.COPPER_CRATE);
        this.add(Item.SILVER_CRATE);
        this.add(Item.IRON_CRATE);
        this.add(Item.DIAMOND_CRATE);

        addCrate(new Crate(new ItemType.PlaceableTile.Crate(Tile.STONE_CRATE, 36), 40, 40));
    }

    @Override
    public void tick() {
    	updateDisplay();
        super.tick();
        
        for(Slot slot : slots) {
        	if(slot.tool != null) {
        		if(slot.tool.calculate() <= 0) {
        			slot.item = null;
        			slot.tool = null;
        			break;
        		}
        	} else if (slot.count <= 0) slot.item = null;
        }
        
        if(ItemCreator.isActive) {
            compInv.tick();
            for(Crate c : crates) c.tick();
        }
    }
    
    // TODO Make tools so they aren't this arraylist thing, rather every slot has a tool or null.
    
    private void updateDisplay() {
        for(Slot slot : slots) {
        	if(slot.item != null) {
	        	if(slot.tool != null) {
	        		slot.display.message = "" + slot.tool.calculate() + "%";
	        		continue;
	        	} else slot.display.message = Integer.toString(slot.count);
        	}
        }
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
    	
    	if(!item.getType().stackable) {
    		for(Slot slot : slots) {
    			if(slot.item == null) {
    				slot.item = item;
    				slot.count = 1;
    				if(item.getType() instanceof ItemType.CreatableItem.Tool) 
    					slot.tool = new Tool((ItemType.CreatableItem.Tool)(item.getType()), slot);
    				return;
    			}
    			else continue;	
            }
    	}
    
        for(Slot slot : slots) {
            if(slot.item == item && slot.count + 1 <= STORAGE_CAP) {
                slot.count++;
                return;
            }
        }
        
        for(Slot slot : slots) {
        	if(slot.item == null) {
                slot.item = item;
                slot.count++;
                return;
            }
        }
    }

    public void addCrate(Crate crate) {
        crates.add(crate);
    }
    
    public void addTool(Tool tool) {
    	for(Slot slot : slots) {
			if(slot.item == null) {
				slot.item = Item.getItemFromID(((ItemType.CreatableItem.Tool)tool.toolType).itemID);
				slot.count = 1;
				slot.tool = tool;
				slot.tool.timesUsed = tool.timesUsed;
				return;
			} else continue;
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
        if(ItemCreator.isActive) {
            compInv.render(b);
            for(Crate c : crates) c.render(b);
        }

    }

    @Override
    public void onResize(int width, int height) {
        super.onResize(width, height);
        compInv.onResize(width, height);
        for(Crate c : crates) c.onResize(width, height);
    }

}
