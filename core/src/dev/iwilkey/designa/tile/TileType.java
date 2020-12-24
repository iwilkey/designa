package dev.iwilkey.designa.tile;

import dev.iwilkey.designa.item.Item;

public abstract class TileType {
    public static class Natural extends TileType {
        public static class Ore extends Natural {
        	public Item drop;
        	
        	public Ore(Item drop) {
        		this.drop = drop;
        	}
        	
        }
    }
}
