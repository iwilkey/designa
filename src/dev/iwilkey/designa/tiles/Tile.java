package dev.iwilkey.designa.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Tile {
	
	// Statics (The global tile array, the global size of each tile, a global instance of each tile)
	public static Tile[] tiles = new Tile[256];
	public static int TILE_SIZE = 16;
	
	public static Random random = new Random();
	
	public static Tile airTile = new AirTile(0);
	public static Tile grassTile = new GrassTile(1);
	public static Tile dirtTile = new DirtTile(2);
	
	public static int getStrength(int id) {
		return tiles[id].getStrength();
	}
	
	// Class (The constructor, the texture of the tile, and the id)
	
	protected BufferedImage texture;
	protected final int ID;
	protected int strength;
	
	public Tile(BufferedImage tex, int id, int strength) {
		this.texture = tex;
		this.ID = id;
		this.strength = strength;
		
		tiles[id] = this; // Update the tiles array at the ID with the instance of this tile.
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g, int x, int y) { // How do we render a tile? At the x, y set by the world or level.
		g.drawImage(texture, x, y, TILE_SIZE, TILE_SIZE, null);
	}
	
	public void render(Graphics g, float x, float y, float scale) { // How do we render a tile? At the x, y set by the world or level.
		g.drawImage(texture, (int)(x * scale), (int) (y * scale), (int)(TILE_SIZE * scale), (int) (TILE_SIZE * scale), null);
	}
	
	public boolean isSolid() { // Can you walk through the tile?
		return true;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getStrength() {
		return strength;
	}
	
}