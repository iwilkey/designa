package dev.iwilkey.designa.world;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.entities.EntityHandler;
import dev.iwilkey.designa.entities.creature.Player;
import dev.iwilkey.designa.items.Item;
import dev.iwilkey.designa.items.ItemHandler;
import dev.iwilkey.designa.layer.Layer;
import dev.iwilkey.designa.layer.SkyLayer;
import dev.iwilkey.designa.tiles.Tile;
import dev.iwilkey.designa.utils.Utils;

public class World {
	
	private AppBuffer ab;
	private int width, height;
	public int[][] tiles;
	public int[][] tileBreakLevel;
	
	// Layers
	private List<Layer> layers = new ArrayList<Layer>();
	
	// Entities
	private EntityHandler entityHandler;
	
	// Items
	private ItemHandler itemHandler;
	
	public World(AppBuffer ab, String path) {
		this.ab = ab;
		entityHandler = new EntityHandler(ab, new Player(ab, 100, 200));
		itemHandler = new ItemHandler(ab);
		layers.add(new SkyLayer(ab, Assets.air));
		
		
		itemHandler.addItem(Item.dirtItem.createNew(50, 50));
		
		loadWorld(path);
	}
	
	public void tick() {
		itemHandler.tick();
		entityHandler.tick();
		
		for(Layer l : layers) {
			l.tick();
		}
	}
	
	public void render(Graphics g) {
		
		for(Layer l : layers) {
			l.render(g);
		}
		
		int xStart = (int) Math.max(0, ab.getGame().getCamera().getxOffset() / Tile.TILE_SIZE);
		int xEnd = (int) Math.min(width, ((ab.getGame().getCamera().getxOffset() + ab.getGame().getWidth()) / Tile.TILE_SIZE) + 1);
		int yStart = (int) Math.max(0, ab.getGame().getCamera().getyOffset() / Tile.TILE_SIZE);
		int yEnd = (int) Math.min(height, ((ab.getGame().getCamera().getyOffset() + ab.getGame().getHeight()) / Tile.TILE_SIZE) + 1);
		
		for(int y = yStart; y < yEnd; y++) {
			for(int x = xStart; x < xEnd; x++) {
				int xx = (int) (x * Tile.TILE_SIZE - ab.getCamera().getxOffset());
				int yy = (int) (y * Tile.TILE_SIZE - ab.getCamera().getyOffset());
				
				getTile(x, y).render(g, xx, yy);
			}
		}
		
		itemHandler.render(g);
		
		entityHandler.render(g);
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
		tileBreakLevel = new int[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int id = Utils.parseInt(tokens[x + y * width + 2]);
				tiles[x][y] = id;
				tileBreakLevel[x][y] = Tile.getStrength(id);
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
	
	public EntityHandler getEntityHandler() {
		return entityHandler;
	}
	
	
	
}
