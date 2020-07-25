package dev.iwilkey.designa.building;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.entities.creature.Player;
import dev.iwilkey.designa.tiles.AirTile;
import dev.iwilkey.designa.tiles.Tile;

public class BuildingHandler {

	private AppBuffer ab;
	private Player player;
	private float selectorX = 0, selectorY = 0;
	private boolean inBreakRange = false;
	
	private Rectangle collider;
	
	public BuildingHandler(Player player, AppBuffer ab) {
		this.player = player;
		this.ab = ab;
		
		collider = new Rectangle((int) (selectorX - ab.getCamera().getxOffset()), 
				(int) (selectorY - ab.getCamera().getyOffset()), Tile.TILE_SIZE, Tile.TILE_SIZE);
	}
	
	public void tick() {
		selectorX = (pointerOnTileX() * Tile.TILE_SIZE);
		selectorY = (pointerOnTileY() * Tile.TILE_SIZE);
		
		if(inBreakRange) {
			
			//Building, TODO: Make sure that "2" is changed to whatever block is selected in the inventory (if applicable)
			if(ab.getInput().justClicked(3)) placeTile(2, pointerOnTileX(), pointerOnTileY());
			
			// Damaging
			if(ab.getInput().justClicked(1)) damageTile(pointerOnTileX(), pointerOnTileY());
			
			// If 'L' is pressed, a light will be placed on that tile.
			if(ab.getInput().keyJustPressed(KeyEvent.VK_L)) ab.getWorld().getLightManager().addLight(pointerOnTileX(), pointerOnTileY(), 6);
			
			// If 'X' is pressed, a light will be removed on that tile if it exists
			if(ab.getInput().keyJustPressed(KeyEvent.VK_X)) ab.getWorld().getLightManager().removeLight(pointerOnTileX(), pointerOnTileY());
					
		}
		
		collider.x = (int) (selectorX - ab.getCamera().getxOffset());
		collider.y = (int) (selectorY - ab.getCamera().getyOffset());
	}
	
	private void placeTile(int id, int x, int y) {
		if(ab.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof AirTile) {
			ab.getWorld().tiles[x][y] = id;
			ab.getWorld().tileBreakLevel[x][y] = Tile.getStrength(id);
		} else {
			return;
		}
	}
	
	private void damageTile(int x, int y) {
		if(!(ab.getWorld().getTile(pointerOnTileX(), pointerOnTileY()) instanceof AirTile)) {
			ab.getWorld().tileBreakLevel[x][y]--;
			
			if(ab.getWorld().tileBreakLevel[x][y] <= 0) {
				ab.getWorld().tiles[x][y] = 0;
				ab.getWorld().tileBreakLevel[x][y] = Tile.getStrength(0);
			}
		} else {
			return;
		}
	}
	
	private int pointerOnTileX() {
		return (int) (ab.getInput().getMouseX() + ab.getCamera().getxOffset()) / Tile.TILE_SIZE;
	}
	
	private int pointerOnTileY() {
		return (int) (ab.getInput().getMouseY() + ab.getCamera().getyOffset()) / Tile.TILE_SIZE;
	}
	
	public void render(Graphics g) {
		// TODO: Maybe don't render the selector if the player doesn't have a block selected in the inventory.
		
		if(Math.abs(selectorX - player.getX()) < 1.5 * Tile.TILE_SIZE && 
				Math.abs(selectorY - player.getY()) < 2 * Tile.TILE_SIZE) {
			inBreakRange = true;
			
			
			if(!ab.getWorld().getEntityHandler().getPlayer().getCollisionBounds(0f, 0f).intersects(collider)) {
				g.drawImage(Assets.selector, (int)(selectorX - ab.getCamera().getxOffset()), 
					(int) (selectorY - ab.getCamera().getyOffset()), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
			} else {
				inBreakRange = false;
				g.drawImage(Assets.errorSelector, (int)(selectorX - ab.getCamera().getxOffset()), 
						(int) (selectorY - ab.getCamera().getyOffset()), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
			}
		} else {
			inBreakRange = false;
			g.drawImage(Assets.transSelector, (int)(selectorX - ab.getCamera().getxOffset()), 
					(int) (selectorY - ab.getCamera().getyOffset()), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
		}
		
	}
}
