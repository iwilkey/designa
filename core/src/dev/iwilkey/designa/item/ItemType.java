package dev.iwilkey.designa.item;

import dev.iwilkey.designa.inventory.Slot;
import dev.iwilkey.designa.tile.Tile;

import java.util.ArrayList;

public abstract class ItemType {
	
	public boolean stackable;

    public abstract static class NonCreatableItem extends ItemType {
    	NonCreatableItem() { stackable = true; }
    }
    
    public static class PlaceableTile extends ItemType {
        public final Tile correspondingTile;
        PlaceableTile(Tile correspondingTile) { 
        	this.correspondingTile = correspondingTile;
        	stackable = true;
        }

		public static class Crate extends PlaceableTile {
			public int space;
			public Crate(Tile correspondingTile, int space) {
				super(correspondingTile);
				this.space = space;
			}
		}
    }

    public abstract static class CreatableItem extends ItemType {
    	
        public static class Resource extends CreatableItem {
        	Resource() { stackable = true; }
        }
        
        public abstract static class Tool extends CreatableItem {
        	
        	public final int itemID;
        	public final int strength;
        	public final int efficiency;
        	
        	public Tool(int itemID, int strength, int efficiency) {
        		stackable = false;
        		this.itemID = itemID;
				this.strength = strength;
				this.efficiency = efficiency;
        	}
        	
            public static class Sickle extends Tool {
        		public int luck;
				public Sickle(int itemID, int strength, int efficiency, int luck) {
					super(itemID, strength, efficiency);
					this.luck = luck;
				}
			}
        }
    }

}
