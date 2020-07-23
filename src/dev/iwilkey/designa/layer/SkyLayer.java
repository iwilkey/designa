package dev.iwilkey.designa.layer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.tiles.Tile;

public class SkyLayer extends Layer {
	
	public SkyLayer(AppBuffer ab, BufferedImage im) {
		super(ab, im);
	}
	
	public void tick() {

	}
	
	public void render(Graphics g) {
		for(int x = 0; x < ab.getWorld().getWorldWidth() * Tile.TILE_SIZE; x++) {
			for(int y = 0; y < ab.getWorld().getWorldHeight() * Tile.TILE_SIZE; y++) {
				if(x % image.getWidth() == 0 && y % image.getHeight() == 0) {
					
					int rr = 135 - (y / 3);
					int gg = 206 - (y / 3);
					int bb = 255 - (y / 3);
					
					if(rr < 0) rr = 0; if(gg < 0) gg = 0; if(bb < 0) bb = 0;
					
					Color color = new Color(rr, gg, bb);
					g.setColor(color);
					
					g.fillRect(x + (int) -ab.getCamera().getxOffset(), y + (int)-ab.getCamera().getyOffset(), 16, 16);
					
				}
			}
		}
	}
}
