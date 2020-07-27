package dev.iwilkey.designa.ui;

import java.awt.Graphics;

import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.entities.creature.Player;
import dev.iwilkey.designa.tiles.Tile;

public class HUD {
	
	private Player player;
	
	private int screenW, screenH;
	
	private int healthX;
	private int healthY;
	private int heartSpacing = 18;
	
	public HUD(Player player) {
		this.player = player;
		screenW = this.player.getAppBuffer().getGame().getWidth();
		screenH = this.player.getAppBuffer().getGame().getHeight();
		healthX = (screenW / 2) - ((10 * 18) / 2);
		healthY = (screenH - 90);
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		renderHealth(g);
	}
	
	private void renderHealth(Graphics g) {
		for(int i = 0; i < 10; i++) {
			if(player.getHealth() >= i + 1) {
				g.drawImage(Assets.heart[0], healthX + (i * heartSpacing), healthY, Tile.TILE_SIZE, Tile.TILE_SIZE, null);
			} else {
				g.drawImage(Assets.heart[1], healthX + (i * heartSpacing), healthY, Tile.TILE_SIZE, Tile.TILE_SIZE, null);
			}
		}
	}
}
