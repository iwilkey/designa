package dev.iwilkey.designa.item;

import dev.iwilkey.designa.tile.Tile;

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
    }

    public abstract static class CreatableItem extends ItemType {
    	
        public static class Resource extends CreatableItem {
        	Resource() { stackable = true; }
        }
        
        public abstract static class Tool extends CreatableItem {
       
        	public final int strength;
        	public final int efficiency;
        	
        	public Tool(int strength, int efficiency) {
        		stackable = false;
				this.strength = strength;
				this.efficiency = efficiency;
        	}
        	
            public static class Sickle extends Tool{
				public Sickle(int strength, int efficiency) {
					super(strength, efficiency);
				}
			}
        }
    }

}
