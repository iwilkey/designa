package dev.iwilkey.designa.layer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.tiles.Tile;

public class UndergroundLayer extends Layer {

	public UndergroundLayer(AppBuffer ab, BufferedImage im) {
		super(ab, im);
	}

	public void tick() {
	}

	public void render(Graphics g) {
		int xStart = (int) Math.max(0, ab.getGame().getCamera().getxOffset() / Tile.TILE_SIZE);
		int xEnd = (int) Math.min(ab.getGame().getWidth(), ((ab.getGame().getCamera().getxOffset() + ab.getGame().getWidth()) / Tile.TILE_SIZE) + 1);
		int yStart = (int) Math.max(0, ab.getGame().getCamera().getyOffset() / Tile.TILE_SIZE);
		int yEnd = (int) Math.min(ab.getGame().getHeight(), ((ab.getGame().getCamera().getyOffset() + ab.getGame().getHeight()) / Tile.TILE_SIZE) + 1);
		
		for(int y = yStart; y < yEnd; y++) {
			for(int x = xStart; x < xEnd; x++) {
				int xx = (int) (x * Tile.TILE_SIZE - ab.getCamera().getxOffset());
				int yy = (int) (y * Tile.TILE_SIZE - ab.getCamera().getyOffset());
				
				if(y > 15) g.drawImage(image, xx, yy, Tile.TILE_SIZE, Tile.TILE_SIZE, null);
			}
		}
	}
	
	
	
}
