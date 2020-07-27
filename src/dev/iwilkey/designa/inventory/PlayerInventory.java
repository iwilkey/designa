package dev.iwilkey.designa.inventory;

import java.awt.Graphics;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.tiles.Tile;

public class PlayerInventory extends Inventory {

	public PlayerInventory(AppBuffer ab, int sX, int sY) {
		super(ab, 4, sX, sY);
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		
		// Render the slots
		for(int i = 0; i < totalSlots; i++) {
			g.drawImage(Assets.invSlot, startX + (i * (Tile.TILE_SIZE * 3 + slotDistBuffer)), 
					startY, Tile.TILE_SIZE * 3, Tile.TILE_SIZE * 3, null);
		}
		
		// Render the items in the slots based off the index that they are in (items list)
		
		
	}
	
	
	
}
