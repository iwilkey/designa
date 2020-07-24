package dev.iwilkey.designa.world;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import dev.iwilkey.designa.AppBuffer;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.entities.EntityHandler;
import dev.iwilkey.designa.entities.creature.Player;
import dev.iwilkey.designa.gfx.Light;
import dev.iwilkey.designa.items.ItemHandler;
import dev.iwilkey.designa.layer.Layer;
import dev.iwilkey.designa.layer.SkyLayer;
import dev.iwilkey.designa.tiles.Tile;
import dev.iwilkey.designa.utils.Utils;

public class World {
	
	private AppBuffer ab;
	private int width, height;
	
	// Rendering
	public int[][] tiles;
	public int[][] tileBreakLevel;
	public int[][] lightMap;
	
	// Layers
	private List<Layer> layers = new ArrayList<Layer>();
	
	// Entities
	private EntityHandler entityHandler;
	
	// Items
	private ItemHandler itemHandler;
	
	// Light
	//private ArrayList<Light> lights = new ArrayList<Light>();
	
	public World(AppBuffer ab, String path) {
		this.ab = ab;
		entityHandler = new EntityHandler(ab, new Player(ab, 100, 200));
		itemHandler = new ItemHandler(ab);
		layers.add(new SkyLayer(ab, Assets.air));
		
		//itemHandler.addItem(Item.dirtItem.createNew(50, 50));
		
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
				
				getTile(x, y).render(g, xx, yy); // Render tile
				
				// Render the block broken level.
				// 5 images in the array, 0th element being no damage
				// Need to find a mathematical model for index to render the correct pattern based the tileBreakLevel.
				int index = Math.round((float)tileBreakLevel[x][y] / 5);
				g.drawImage(Assets.breakLevel[index], xx, yy, Tile.TILE_SIZE, Tile.TILE_SIZE, null);
				
				renderLight(g, x, y);
			}
		}
		
		
		
		itemHandler.render(g);
		
		entityHandler.render(g);
	}
	
	private void renderLight(Graphics g, int x, int y) {
		int xx = (int) (x * Tile.TILE_SIZE - ab.getCamera().getxOffset());
		int yy = (int) (y * Tile.TILE_SIZE - ab.getCamera().getyOffset());
		Color dark = new Color(0,0,0,0);
		switch(lightMap[x][y]) {
			case 6:
				dark = new Color(0,0,0, 0);
				break;
			case 5:
				dark = new Color(0,0,0, Math.round(255 / 4));
				break;
			case 4:
				dark = new Color(0,0,0, Math.round(255 / 2.5f));
				break;
			case 3:
				dark = new Color(0,0,0, Math.round(255 / 1.75f));
				break;
			case 2:
				dark = new Color(0,0,0, Math.round(255 / 1.40f));
				break;
			case 1:
				dark = new Color(0,0,0, Math.round(255 / 1.10f));
				break;
			
			default:
				dark = new Color(0,0,0, 255);

		}
		
		g.setColor(dark);
		g.fillRect(xx, yy, Tile.TILE_SIZE, Tile.TILE_SIZE);
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
		lightMap = new int[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int id = Utils.parseInt(tokens[x + y * width + 2]);
				tiles[x][y] = id;
				tileBreakLevel[x][y] = Tile.getStrength(id);
				
				// TODO: Whenever 16, or (16 - 1) is used, that has to do with the world ground level! That should be defined in all worlds for
				// ambient light to work properly.
				if(y > 15 && tiles[x][y] != 0) {
					lightMap[x][y] = 6 - (Math.abs(y - 16));
				} else if (tiles[x][y] == 0) {
					lightMap[x][y] = 6;
				}
			}
		}
		
		new Light(this,16, 19, 6);
		new Light(this,16, 23, 6);
		new Light(this,16, 28, 6);
		
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
