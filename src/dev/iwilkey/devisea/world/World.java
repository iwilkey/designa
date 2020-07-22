package dev.iwilkey.devisea.world;

import java.awt.Graphics;

import dev.iwilkey.devisea.AppBuffer;
import dev.iwilkey.devisea.entities.EntityManager;
import dev.iwilkey.devisea.entities.creature.Player;
import dev.iwilkey.devisea.tiles.AirTile;
import dev.iwilkey.devisea.tiles.Tile;
import dev.iwilkey.devisea.utils.Utils;

public class World {
	
	private AppBuffer ab;
	private int width, height;
	private int[][] tiles;
	
	// Entities
	private EntityManager entityManager;
	
	public World(AppBuffer ab, String path) {
		this.ab = ab;
		entityManager = new EntityManager(ab, new Player(ab, 100, 100));
		loadWorld(path);
	}
	
	public void tick() {
		entityManager.tick();
		
		// TODO: Make a building manager class that does this
		
		//Building
		if(ab.getInput().isRightMouseButtonDown()) {
			int x = ab.getInput().getMouseX() / Tile.TILE_SIZE;
			int y = ab.getInput().getMouseY() / Tile.TILE_SIZE;
			if(getTile(x, y) instanceof AirTile) {
				tiles[x][y] = 2;
			} else {
				return;
			}
		}
		
		// Destroying
		if(ab.getInput().isLeftMouseButtonDown()) {
			int x = ab.getInput().getMouseX() / Tile.TILE_SIZE;
			int y = ab.getInput().getMouseY() / Tile.TILE_SIZE;
			if(!(getTile(x, y) instanceof AirTile)) {
				tiles[x][y] = 0;
			} else {
				return;
			}
		}
		
	}
	
	public void render(Graphics g) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				getTile(x, y).render(g, (int) x * Tile.TILE_SIZE, (int) y * Tile.TILE_SIZE);
			}
		}
		
		entityManager.render(g);
	}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) {
			return Tile.dirtTile;
		}
		
		Tile t = Tile.tiles[tiles[x][y]];
		
		if(t == null) {
			return Tile.dirtTile;
		}
		
		return t;
	}
	
	private void loadWorld(String path) {
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+"); // Split into array at any white space
		
		width = Utils.parseInt(tokens[0]);
		height = Utils.parseInt(tokens[1]);
		
		tiles = new int[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				tiles[x][y] = Utils.parseInt(tokens[x + y * width + 2]);
			}
		}
		
	}

	public AppBuffer getAppBuffer() {
		return ab;
	}

	public int getWorldWidth() {
		return width;
	}

	public int getWorldHeight() {
		return height;
	}
	
	
	
}
